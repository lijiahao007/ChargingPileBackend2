package com.lijiahao.chargingpilebackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiahao.chargingpilebackend.controller.requestparam.AddAppointmentRequest;
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
import java.util.List;

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

    private ObjectMapper mapper = new ObjectMapper();

    @ApiOperation("根据stationId 获取对应充电站的未完成的充电信息")
    @GetMapping("/getAppointmentByStationId")
    public List<Appointment> getAppointmentByStationId(@RequestParam("stationId") Integer stationId) {
        return appointmentService.list(new QueryWrapper<Appointment>()
                .eq("station_id", stationId)
                .eq("state", Appointment.STATE_WAITING));
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
}
