package com.lijiahao.chargingpilebackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiahao.chargingpilebackend.controller.response.GenerateOrderResponse;
import com.lijiahao.chargingpilebackend.controller.response.QueryOrderResponse;
import com.lijiahao.chargingpilebackend.entity.*;
import com.lijiahao.chargingpilebackend.service.impl.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lijiahao
 * @since 2022-03-14
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    ChargingPileStationServiceImpl chargingPileStationService;

    @Autowired
    ChargingPileServiceImpl chargingPileService;

    @Autowired
    OpenTimeServiceImpl openTimeService;

    @Autowired
    OrderServiceImpl orderService;

    @Autowired
    ElectricChargePeriodServiceImpl electricChargePeriodService;

    @Autowired
    AppointmentServiceImpl appointmentService;


    private ObjectMapper mapper = new ObjectMapper();

    @ApiOperation("请求生成订单")
    @GetMapping("/generateOrder")
    @Transactional
    public GenerateOrderResponse generateOrder(
            @RequestParam("pileId") String pileId,
            @RequestParam("userId") String userId) {

        GenerateOrderResponse response = new GenerateOrderResponse();

        // 1. 查看pile状态
        ChargingPile pile = chargingPileService.getById(pileId);
        String state = pile.getState();
        if (state.equals(ChargingPile.STATE_SUSPEND)) {
            // 暂停营业
            response.setCode(GenerateOrderResponse.SUSPEND);
            response.setOrder(new Order());
            return response;
        } else if (state.equals(ChargingPile.STATE_USING)) {
            // 使用中
            response.setCode(GenerateOrderResponse.USING);
            response.setOrder(new Order());
            return response;
        }

        // 1.1 查看是否处在预约状态中
        LocalDateTime now = LocalDateTime.now();
        long count = appointmentService.count(new QueryWrapper<Appointment>()
                .eq("pile_id", pileId)
                .eq("state", Appointment.STATE_WAITING)
                .le("begin_date_time", now)
                .ge("end_date_time", now)
        ); // beginTime <= now <= endTime
        if (count > 0) {
            response.setCode(GenerateOrderResponse.APPOINTMENT);
            response.setOrder(new Order());
            return response;
        }

        // 2. 更新充电桩状态
        pile.setState(ChargingPile.STATE_USING);
        chargingPileService.updateById(pile);


        // 3. 如果符合生成订单的条件，生成订单
        Order order = new Order();
        order.setPileId(Integer.valueOf(pileId));
        order.setState(Order.STATE_USING);
        order.setUserId(Integer.valueOf(userId));
        order.setUuid(UUID.randomUUID().toString());
        orderService.save(order);
        response.setCode(GenerateOrderResponse.SUCCESS);
        Order newOrder = orderService.getById(order.getId());
        response.setOrder(newOrder);

        // 4. 充电站使用次数加1
        ChargingPileStation station = chargingPileStationService.getById(pile.getStationId());
        station.setUsedTime(station.getUsedTime() + 1);
        chargingPileStationService.updateById(station);

        return response;
    }


    @ApiOperation("请求结束订单")
    @GetMapping("/finishOrder")
    @Transactional
    public GenerateOrderResponse finishOrder(@RequestParam("orderId") String orderId) throws Exception {

        // 1. 获取对应订单
        Order order = orderService.getById(orderId);
        order.setCompleteTime(LocalDateTime.now());
        order.setState(Order.STATE_UNPAID); // 订单状态：待支付

        // 2. 修改充电桩状态
        int pileId = order.getPileId();
        ChargingPile pile = chargingPileService.getById(pileId);
        pile.setState(ChargingPile.STATE_FREE);
        chargingPileService.updateById(pile);
        float powerRate = pile.getPowerRate(); // 该充电桩的功率

        // 3. 获取营业时间
        int stationId = pile.getStationId();
        List<ElectricChargePeriod> electricPeriods = electricChargePeriodService.list(new QueryWrapper<ElectricChargePeriod>().eq("station_id", stationId));
        // 排序，小的时间在前面
        electricPeriods.sort((o1, o2) -> {
            if (o1.getBeginTime().isBefore(o2.getBeginTime())) {
                return -1;
            } else if (o1.getBeginTime().equals(o2.getBeginTime())) {
                return 0;
            } else {
                return 1;
            }
        });

        // 4. 计算订单价格
        LocalDateTime chargeBeginTime = order.getBeginChargeTime();
        LocalDateTime chargeEndTime = order.getCompleteTime();
        double sumPrice = 0.0;
        sumPrice += calChargingFee(electricPeriods, chargeBeginTime, chargeEndTime, powerRate); // 充电费用
        ChargingPileStation station = chargingPileStationService.getById(stationId);
        double parkFee = station.getParkFee();
        double parkTime = Duration.between(chargeBeginTime, chargeEndTime).toMinutes();
        double totalParkFee = parkTime / 60.0 * parkFee; // 停车费用
        sumPrice += totalParkFee; // 停车费用

        // 5. 修改订单状态
        order.setPrice((float) sumPrice);
        orderService.updateById(order);

        // 6. 获取返回结果
        Order nerOrder = orderService.getById(orderId);
        GenerateOrderResponse response = new GenerateOrderResponse();
        response.setOrder(nerOrder);
        response.setCode(GenerateOrderResponse.SUCCESS);
        return response;
    }


    @ApiOperation("支付订单")
    @GetMapping("/payOrder")
    @Transactional
    public String payOrder(@RequestParam("orderId") String orderId) throws Exception {

        // 1. 获取对应订单
        Order order = orderService.getById(orderId);
        order.setState(Order.STATE_FINISH); // 订单状态：已支付

        // 2. 修改订单状态
        orderService.updateById(order);

        // 3. 获取返回结果
        return mapper.writeValueAsString("success");
    }

    @ApiOperation("获取个人相关订单")
    @GetMapping("/queryOrderByUserId")
    public QueryOrderResponse queryOrderByUserId(@RequestParam("userId") int userId) {

        // 1. 获取自身使用的订单
        List<Order> finishOrder = orderService.list(new QueryWrapper<Order>()
                .eq("user_id", userId)
                .eq("state", Order.STATE_FINISH));
        List<Order> processingOrder = orderService.list(new QueryWrapper<Order>()
                .eq("user_id", userId)
                .and(i -> i.eq("state", Order.STATE_USING).or().eq("state", Order.STATE_UNPAID)));


        QueryOrderResponse response = new QueryOrderResponse();
        response.setFinishOrder(finishOrder);
        response.setProcessingOrder(processingOrder);

        // 2. 获取提供服务的订单
        List<ChargingPileStation> stations = chargingPileStationService.list(
                new QueryWrapper<ChargingPileStation>()
                        .eq("user_id", userId)
        );

        HashMap<Integer, Map<Integer, List<Order>>> stationPileServiceOrderMap = new HashMap<>(); // <StationId, <PileId, Order>>
        List<Integer> stationIds = stations.stream().map(ChargingPileStation::getId).collect(Collectors.toList());
        Map<Integer, List<ChargingPile>> stationPileMap = chargingPileService.getStationChargingPile(stationIds);
        HashMap<Integer, Integer> pileStationMap = new HashMap<>(); // <PileId, StationId> 获取所有订单相关的充电桩、充电站对应关系
        for (Integer stationId : stationIds) {
            List<ChargingPile> piles = stationPileMap.get(stationId);
            if (piles != null) {
                HashMap<Integer, List<Order>> pileOrderMap = new HashMap<>();
                for (ChargingPile pile : piles) {
                    int pileId = pile.getId();
                    List<Order> orders = orderService.list(new QueryWrapper<Order>().eq("pile_id", pile.getId()));
                    if (orders.size() > 0) {
                        pileOrderMap.put(pileId, orders);
                        pileStationMap.put(pileId, stationId);
                    }
                }
                if (pileOrderMap.size() > 0) {
                    stationPileServiceOrderMap.put(stationId, pileOrderMap);
                }
            }
        }
        response.setServiceOrder(stationPileServiceOrderMap);


        // 3. 获取相关的ChargingPileStation
        HashSet<Integer> pileSet = new HashSet<>();
        processingOrder.forEach(order -> pileSet.add(order.getPileId()));
        finishOrder.forEach(order -> pileSet.add(order.getPileId()));
        HashSet<Integer> stationSet = new HashSet<>();
        for (Integer pileId : pileSet) {
            ChargingPile pile = chargingPileService.getById(pileId);
            stationSet.add(pile.getStationId());
            pileStationMap.put(pileId, pile.getStationId());
        }
        stationSet.addAll(stationPileServiceOrderMap.keySet());
        HashMap<Integer, ChargingPileStation> relateStationMap = new HashMap<>();
        if (stationSet.size() > 0) {
            List<ChargingPileStation> relateStations = chargingPileStationService.listByIds(stationSet);
            relateStations.forEach(station -> relateStationMap.put(station.getId(), station));
        }
        response.setStationInfoMap(relateStationMap);
        response.setPileStationMap(pileStationMap);

        return response;
    }

    @ApiOperation("获取某个订单的信息")
    @GetMapping("/queryOrderByOrderId")
    public Order queryOrderByOrderId(@RequestParam("orderId") int orderId) {
        return orderService.getById(orderId);
    }

    /**
     * 计算同一天 beginTime ~ endTime 的充电费用
     *
     * @param electricChargePeriods 充电桩营业时间
     * @param beginTime             充电开始时间
     * @param endTime               充电结束时间
     * @param powerRate             充电桩功率
     * @return
     */
    private double calChargingFee(List<ElectricChargePeriod> electricChargePeriods, LocalTime beginTime, LocalTime endTime, double powerRate) {
        double sumPrice = 0.0;

        // 1. 订单在同一天开始和结束
        int beginIndex = 0; // 开始时间段
        int endIndex = 0; // 结束时间段
        for (int i = 0; i < electricChargePeriods.size(); i++) {
            ElectricChargePeriod openTime = electricChargePeriods.get(i);
            if (isBetween(beginTime, openTime.getBeginTime(), openTime.getEndTime())) {
                beginIndex = i;
            }
            if (isBetween(endTime, openTime.getBeginTime(), openTime.getEndTime())) {
                endIndex = i;
            }
        }
        if (beginIndex != endIndex) {
            // 1.1 计算第一个时间段的价格
            ElectricChargePeriod firstOpenTime = electricChargePeriods.get(beginIndex);
            Duration firstPeriod = Duration.between(beginTime, firstOpenTime.getEndTime());
            long firstSecond = firstPeriod.toMillis() / 1000;
            double firstPrice = (firstSecond / 3600.0) * powerRate * firstOpenTime.getElectricCharge(); // hour * kw/h * 元/kw = 该时间段的
            sumPrice += firstPrice;

            // 1.2 计算最后一个时间段的价格
            ElectricChargePeriod lastOpenTime = electricChargePeriods.get(endIndex);
            Duration lastPeriod = Duration.between(lastOpenTime.getBeginTime(), endTime);
            long lastSecond = lastPeriod.toMillis() / 1000;
            double lastPrice = (lastSecond / 3600.0) * powerRate * lastOpenTime.getElectricCharge();
            sumPrice += lastPrice;

            // 1.3 计算中间时间段的价格
            for (int i = beginIndex + 1; i <= endIndex - 1; i++) {
                ElectricChargePeriod midOpenTime = electricChargePeriods.get(i);
                Duration midPeriod = Duration.between(midOpenTime.getBeginTime(), midOpenTime.getEndTime());
                long midSecond = midPeriod.toMillis() / 1000;
                double midPrice = (midSecond / 3600.0) * powerRate * midOpenTime.getElectricCharge();
                sumPrice += midPrice;
            }
        } else {
            ElectricChargePeriod openTime = electricChargePeriods.get(beginIndex);
            Duration duration = Duration.between(beginTime, endTime);
            long second = duration.toMillis() / 1000;
            double price = (second / 3600.0) * powerRate * openTime.getElectricCharge();
            sumPrice += price;
        }
        return sumPrice;
    }

    /**
     * 计算从 beginDateTime, endDateTime 时间段内充电费用
     *
     * @param electricChargePeriods 时间段收费情况
     * @param beginDateTime         开始时间
     * @param endDateTime           结束时间
     * @param powerRate             功率
     * @return 费用
     */
    private double calChargingFee(List<ElectricChargePeriod> electricChargePeriods, LocalDateTime beginDateTime, LocalDateTime endDateTime, double powerRate) {
        double sumPrice = 0.0;

        LocalTime beginTime = beginDateTime.toLocalTime();
        LocalTime endTime = endDateTime.toLocalTime();

        if (beginDateTime.getDayOfMonth() == endDateTime.getDayOfMonth()) {
            sumPrice += calChargingFee(electricChargePeriods, beginTime, endTime, powerRate);
        } else {
            // 2. 订单不在同一天(前提是该充电站营业时间是连续的)
            // 2.1 计算第一天的价格
            sumPrice += calChargingFee(electricChargePeriods, beginTime, LocalTime.of(23, 59, 59), powerRate);

            // 2.2 计算最后一天的价格
            sumPrice += calChargingFee(electricChargePeriods, LocalTime.of(0, 0, 0), endTime, powerRate);

            // 2.3 计算中间天的价格
            LocalDateTime tmpBegin = LocalDateTime.of(beginDateTime.getYear(), beginDateTime.getMonth(), beginDateTime.getDayOfMonth(), 0, 0, 0);
            LocalDateTime tmpEnd = LocalDateTime.of(endDateTime.getYear(), endDateTime.getMonth(), endDateTime.getDayOfMonth(), 0, 0, 0);
            Duration duration = Duration.between(tmpBegin, tmpEnd);
            long days = duration.toDays() - 1;
            for (int i = 0; i < days; i++) {
                sumPrice += calChargingFee(electricChargePeriods, LocalTime.of(0, 0, 0), LocalTime.of(23, 59, 59), powerRate);
            }
        }
        return sumPrice;
    }

    private Boolean isBetween(LocalTime target, LocalTime begin, LocalTime end) {
        return (target.equals(begin) || target.isAfter(begin)) && (target.equals(end) || target.isBefore(end));
    }
}

