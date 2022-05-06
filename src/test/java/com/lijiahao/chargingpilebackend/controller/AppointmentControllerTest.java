package com.lijiahao.chargingpilebackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lijiahao.chargingpilebackend.entity.Appointment;
import com.lijiahao.chargingpilebackend.service.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AppointmentControllerTest {

    @Autowired
    AppointmentServiceImpl appointmentService;

    @Test
    void getAppointmentByUserId() {
        int userId = 1;
        List<Appointment> list = appointmentService.list(new QueryWrapper<Appointment>()
                .eq("user_id", userId)
                .orderByDesc("begin_date_time")
        );

        list.forEach(System.out::println);
    }
}