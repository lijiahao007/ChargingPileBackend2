package com.lijiahao.chargingpilebackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiahao.chargingpilebackend.controller.requestparam.AddAppointmentRequest;
import com.lijiahao.chargingpilebackend.controller.requestparam.ModifyAppointmentRequest;
import com.lijiahao.chargingpilebackend.controller.response.AddAppointmentResponse;
import com.lijiahao.chargingpilebackend.entity.Appointment;
import com.lijiahao.chargingpilebackend.entity.ChargingPile;
import com.lijiahao.chargingpilebackend.entity.ChargingPileStation;
import com.lijiahao.chargingpilebackend.entity.User;
import com.lijiahao.chargingpilebackend.service.impl.AppointmentServiceImpl;
import com.lijiahao.chargingpilebackend.service.impl.ChargingPileServiceImpl;
import com.lijiahao.chargingpilebackend.service.impl.ChargingPileStationServiceImpl;
import com.lijiahao.chargingpilebackend.service.impl.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lijiahao
 * @since 2022-03-21
 */
@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    AppointmentServiceImpl appointmentService;

    @Autowired
    ChargingPileStationServiceImpl chargingPileStationService;

    @Autowired
    ChargingPileServiceImpl chargingPileService;

    @Autowired
    UserServiceImpl userService;

    private final ObjectMapper mapper = new ObjectMapper();

    @ApiOperation("根据stationId 获取对应充电站的未完成的充电信息")
    @GetMapping("/getAppointmentByStationId")
    public List<Appointment> getAppointmentByStationId(@RequestParam("stationId") Integer stationId) {
        return appointmentService.list(new QueryWrapper<Appointment>()
                .eq("station_id", stationId)
                .eq("state", Appointment.STATE_WAITING)
                .ge("end_date_time", LocalDateTime.now()));
    }

    @ApiOperation("添加预约")
    @PostMapping("/addAppointment")
    public String addAppointment(@RequestBody AddAppointmentRequest request) throws JsonProcessingException {
        int userId = request.userId;
        int stationId = request.stationId;
        int pileId = request.pileId;
        User user = userService.getById(userId);
        ChargingPile pile = chargingPileService.getById(pileId);
        ChargingPileStation station = chargingPileStationService.getById(stationId);

        if ((user == null) || (pile == null) || (station == null) || (pile.getStationId() != stationId)) {
            return mapper.writeValueAsString(AddAppointmentResponse.FAIL);
        }

        Appointment appointment = new Appointment();
        LocalDate date = LocalDate.parse(request.date);
        LocalTime beginTime = LocalTime.parse(request.beginTime);
        LocalTime endTime = LocalTime.parse(request.endTime);
        LocalDateTime beginDateTime = LocalDateTime.of(date, beginTime);
        LocalDateTime endDateTime = LocalDateTime.of(date, endTime);
        appointment.setBeginDateTime(beginDateTime);
        appointment.setEndDateTime(endDateTime);
        appointment.setPileId(request.pileId);
        appointment.setUserId(request.userId);
        appointment.setStationId(request.stationId);
        appointment.setState(Appointment.STATE_WAITING);
        long count = appointmentService.count(
                new QueryWrapper<Appointment>()
                        .not(appointmentQueryWrapper -> appointmentQueryWrapper.le("end_date_time", beginDateTime)
                                .or()
                                .ge("begin_date_time", endDateTime)));

        if (count != 0) {
            return mapper.writeValueAsString(AddAppointmentResponse.CONFLICT);
        }

        appointmentService.save(appointment);
        return mapper.writeValueAsString(AddAppointmentResponse.SUCCESS);
    }


    @ApiOperation("获取某个用户的预约")
    @GetMapping("/getAppointmentByUserId")
    public List<Appointment> getAppointmentByUserId(@RequestParam("userId") Integer userId) {
        return appointmentService.list(new QueryWrapper<Appointment>()
                .eq("user_id", userId)
                .orderByDesc("begin_date_time")
        );
    }

    @ApiOperation("修改某个Appointment时间")
    @PostMapping("modifyAppointment")
    public String modifyAppointment(@RequestBody ModifyAppointmentRequest request) throws JsonProcessingException {

        int appointmentId = request.id;
        Appointment appointment = appointmentService.getById(appointmentId);

        if (appointment == null) {
            return mapper.writeValueAsString(AddAppointmentResponse.FAIL);
        }

        LocalDateTime beginDateTime = LocalDateTime.parse(request.beginDateTime);
        LocalDateTime endDateTime = LocalDateTime.parse(request.endDateTime);
        appointment.setBeginDateTime(beginDateTime);
        appointment.setEndDateTime(endDateTime);
        appointmentService.updateById(appointment);
        return mapper.writeValueAsString(AddAppointmentResponse.SUCCESS);
    }

    @ApiOperation("删除某个Appointment时间")
    @PostMapping("deleteAppointment")
    public String deleteAppointment(@RequestParam("stationId") Integer appointment) throws JsonProcessingException {
        appointmentService.removeById(appointment);
        return mapper.writeValueAsString(AddAppointmentResponse.SUCCESS);
    }

    @ApiOperation("获取所有未完成的Appointment")
    @GetMapping("getAllAppointment")
    public Map<Integer, List<Appointment>> getAllAppointment() {
        List<Appointment> appointments = appointmentService.list(new QueryWrapper<Appointment>().eq("state", Appointment.STATE_WAITING));
        HashMap<Integer, List<Appointment>> map = new HashMap<>();
        for (Appointment appointment: appointments) {
            int stationId = appointment.getStationId();
            if (map.containsKey(stationId)) {
                map.get(stationId).add(appointment);
            } else {
                List<Appointment> list = new ArrayList<>();
                list.add(appointment);
                map.put(stationId, list);
            }
        }
        return map;
    }
}
