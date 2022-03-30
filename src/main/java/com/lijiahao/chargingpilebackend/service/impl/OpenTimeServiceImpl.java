package com.lijiahao.chargingpilebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lijiahao.chargingpilebackend.entity.OpenTime;
import com.lijiahao.chargingpilebackend.mapper.OpenTimeMapper;
import com.lijiahao.chargingpilebackend.service.IOpenTimeService;
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
 * @since 2022-03-14
 */
@Service
public class OpenTimeServiceImpl extends ServiceImpl<OpenTimeMapper, OpenTime> implements IOpenTimeService {
    public Map<Integer, List<OpenTime>> getStationOpenTime() {
        HashMap<Integer, List<OpenTime>> map = new HashMap<>();
        list().forEach(openTime -> {
            int stationId = openTime.getStationId();
            if(map.containsKey(stationId)) {
                map.get(stationId).add(openTime);
            } else {
                map.put(stationId, new ArrayList<OpenTime>(){{add(openTime);}});
            }
        });
        return map;
    }

    public Map<Integer, List<OpenTime>> getStationOpenTime(List<Integer> stationIds) {
        HashMap<Integer, List<OpenTime>> map = new HashMap<>();
        list(new QueryWrapper<OpenTime>().in("station_id", stationIds)).forEach(openTime -> {
            int stationId = openTime.getStationId();
            if(map.containsKey(stationId)) {
                map.get(stationId).add(openTime);
            } else {
                map.put(stationId, new ArrayList<OpenTime>(){{add(openTime);}});
            }
        });
        return map;
    }
}
