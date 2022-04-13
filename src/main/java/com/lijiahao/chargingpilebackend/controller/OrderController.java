package com.lijiahao.chargingpilebackend.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiahao.chargingpilebackend.controller.response.GenerateOrderResponse;
import com.lijiahao.chargingpilebackend.entity.ChargingPile;
import com.lijiahao.chargingpilebackend.entity.Order;
import com.lijiahao.chargingpilebackend.service.impl.ChargingPileServiceImpl;
import com.lijiahao.chargingpilebackend.service.impl.OrderServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lijiahao
 * @since 2022-03-14
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    ChargingPileServiceImpl chargingPileService;

    @Autowired
    OrderServiceImpl orderService;

    private ObjectMapper mapper = new ObjectMapper();

    @ApiOperation("请求生成订单")
    @GetMapping("/generateOrder")
    @Transactional
    public GenerateOrderResponse generateOrder(
            @RequestParam("pileId") String pileId,
            @RequestParam("userId") String userId){

        GenerateOrderResponse response = new GenerateOrderResponse();

        // 1. 查看pile状态
        ChargingPile pile = chargingPileService.getById(pileId);
        String state = pile.getState();
        if (state.equals(ChargingPile.STATE_SUSPEND)){
            // 暂停营业
            response.setCode(GenerateOrderResponse.SUSPEND);
            response.setOrder(new Order());
            return response;
        } else if (state.equals(ChargingPile.STATE_USING)){
            // 使用中
            response.setCode(GenerateOrderResponse.USING);
            response.setOrder(new Order());
            return response;
        }

//         2. 更新充电桩状态
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

        return response;
    }


    @ApiOperation("请求结束订单")
    @GetMapping("/finishOrder")
    @Transactional
    public String finishOrder(@RequestParam("orderId") String orderId) throws JsonProcessingException {
        Order order = orderService.getById(orderId);
        order.setCompleteTime(LocalDateTime.now());
        order.setState(Order.STATE_FINISH);
        orderService.updateById(order);
        int pileId = order.getPileId();
        ChargingPile pile = chargingPileService.getById(pileId);
        pile.setState(ChargingPile.STATE_FREE);
        chargingPileService.updateById(pile);
        return mapper.writeValueAsString("success");
    }


}

