package com.lijiahao.chargingpilebackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lijiahao.chargingpilebackend.entity.Appointment;
import com.lijiahao.chargingpilebackend.service.impl.AppointmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduledTask {

    @Autowired
    AppointmentServiceImpl appointmentService;

    @Scheduled(fixedRate = 120000) // 两分钟刷新一次数据
    public void scheduleAppointmentStateTask() {
        List<Appointment> list = appointmentService.list(new QueryWrapper<Appointment>()
                .eq("state", Appointment.STATE_WAITING));
        LocalDateTime now = LocalDateTime.now();
        List<Appointment> updateList = new ArrayList<>();
        for (Appointment appointment : list) {
            if (appointment.getEndDateTime().isBefore(now)) {
                appointment.setState(Appointment.STATE_OUT_DATE);
                updateList.add(appointment);
            }
        }
        appointmentService.updateBatchById(updateList);
    }
}
