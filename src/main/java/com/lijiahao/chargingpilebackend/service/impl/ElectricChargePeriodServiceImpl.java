package com.lijiahao.chargingpilebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lijiahao.chargingpilebackend.entity.ElectricChargePeriod;
import com.lijiahao.chargingpilebackend.mapper.ElectricChargePeriodMapper;
import com.lijiahao.chargingpilebackend.service.IElectricChargePeriodService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lijiahao
 * @since 2022-04-15
 */
@Service
public class ElectricChargePeriodServiceImpl extends ServiceImpl<ElectricChargePeriodMapper, ElectricChargePeriod> implements IElectricChargePeriodService {

    public Map<Integer, List<ElectricChargePeriod>> getElectricChargePeriod() {
        HashMap<Integer, List<ElectricChargePeriod>> map = new HashMap<Integer, List<ElectricChargePeriod>>();
        List<ElectricChargePeriod> list = list();
        for (ElectricChargePeriod electricChargePeriod : list) {
            Integer stationId = electricChargePeriod.getStationId();
            if (map.containsKey(stationId)) {
                map.get(stationId).add(electricChargePeriod);
            } else {
                ArrayList<ElectricChargePeriod> tmp = new ArrayList<>();
                tmp.add(electricChargePeriod);
                map.put(stationId, tmp);
            }
        }
        return map;
    }

    public Map<Integer, List<ElectricChargePeriod>> getElectricChargePeriod(List<Integer> stationIds) {
        List<ElectricChargePeriod> list = list(new QueryWrapper<ElectricChargePeriod>().in("station_id", stationIds));
        HashMap<Integer, List<ElectricChargePeriod>> map = new HashMap<Integer, List<ElectricChargePeriod>>();
        for (ElectricChargePeriod electricChargePeriod : list) {
            Integer stationId = electricChargePeriod.getStationId();
            if (map.containsKey(stationId)) {
                map.get(stationId).add(electricChargePeriod);
            } else {
                ArrayList<ElectricChargePeriod> tmp = new ArrayList<>();
                tmp.add(electricChargePeriod);
                map.put(stationId, tmp);
            }
        }
        return map;
    }

    public List<ElectricChargePeriod> getElectricChargePeriodByStationId(int stationId) {
        return list(new QueryWrapper<ElectricChargePeriod>().eq("station_id", stationId));
    }
}
