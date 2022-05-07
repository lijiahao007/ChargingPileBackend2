package com.lijiahao.chargingpilebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lijiahao.chargingpilebackend.entity.Appointment;
import com.lijiahao.chargingpilebackend.mapper.AppointmentMapper;
import com.lijiahao.chargingpilebackend.service.IAppointmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lijiahao
 * @since 2022-03-21
 */
@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements IAppointmentService {

    public List<Appointment> getAppointmentByStationId(int stationId) {
        return list(new QueryWrapper<Appointment>().eq("station_id", stationId).eq("state", Appointment.STATE_WAITING));
    }

    public Map<Integer, List<Appointment>> getAppointment() {
        List<Appointment> list = list(new QueryWrapper<Appointment>().eq("state", Appointment.STATE_WAITING));
        HashMap<Integer, List<Appointment>> map = new HashMap<>();
        for (Appointment appointment : list) {
            int stationId = appointment.getStationId();
            if (map.containsKey(stationId)) {
                map.get(stationId).add(appointment);
            } else {
                List<Appointment> appointments = new ArrayList<>();
                appointments.add(appointment);
                map.put(stationId, appointments);
            }
        }
        return map;
    }


    public Map<Integer, List<Appointment>> getAppointment(List<Integer> stationIds) {
        List<Appointment> list = list(new QueryWrapper<Appointment>().eq("state", Appointment.STATE_WAITING).in("station_id", stationIds));
        HashMap<Integer, List<Appointment>> map = new HashMap<>();
        for (Appointment appointment : list) {
            int stationId = appointment.getStationId();
            if (map.containsKey(stationId)) {
                map.get(stationId).add(appointment);
            } else {
                List<Appointment> appointments = new ArrayList<>();
                appointments.add(appointment);
                map.put(stationId, appointments);
            }
        }
        return map;
    }
}
