package com.lijiahao.chargingpilebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lijiahao.chargingpilebackend.entity.OpenDayInWeek;
import com.lijiahao.chargingpilebackend.mapper.OpenDayInWeekMapper;
import com.lijiahao.chargingpilebackend.service.IOpenDayInWeekService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
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
 * @since 2022-03-21
 */
@Service
@Slf4j
public class OpenDayInWeekServiceImpl extends ServiceImpl<OpenDayInWeekMapper, OpenDayInWeek> implements IOpenDayInWeekService {
        public Map<Integer, List<OpenDayInWeek>> getOpenDayInWeek(){
            List<OpenDayInWeek> openDayInWeeks = this.list();
            Map<Integer,List<OpenDayInWeek>> map = new HashMap<>();
            for (OpenDayInWeek openDayInWeek : openDayInWeeks) {
                int stationId = openDayInWeek.getStationId();
                if (map.containsKey(stationId)){
                    map.get(stationId).add(openDayInWeek);
                } else {
                    map.put(stationId,new ArrayList<>());
                    map.get(stationId).add(openDayInWeek);
                }
            }
            return map;
        }


    public Map<Integer, List<OpenDayInWeek>> getOpenDayInWeek(List<Integer> stationIds){
        List<OpenDayInWeek> openDayInWeeks = this.list(new QueryWrapper<OpenDayInWeek>().in("station_id",stationIds));
        Map<Integer,List<OpenDayInWeek>> map = new HashMap<>();
        for (OpenDayInWeek openDayInWeek : openDayInWeeks) {
            int stationId = openDayInWeek.getStationId();
            if (map.containsKey(stationId)){
                map.get(stationId).add(openDayInWeek);
            } else {
                map.put(stationId,new ArrayList<>());
                map.get(stationId).add(openDayInWeek);
            }
        }
        return map;
    }
}
