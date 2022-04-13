package com.lijiahao.chargingpilebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lijiahao.chargingpilebackend.service.IChargingPileService;
import com.lijiahao.chargingpilebackend.entity.ChargingPile;
import com.lijiahao.chargingpilebackend.mapper.ChargingPileMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ChargingPileServiceImpl extends ServiceImpl<ChargingPileMapper, ChargingPile> implements IChargingPileService {

    private final ChargingPileStationServiceImpl chargingPileStationService;

    @Autowired
    public ChargingPileServiceImpl(ChargingPileStationServiceImpl chargingPileStationService) {
        this.chargingPileStationService = chargingPileStationService;
    }

    public Map<Integer, List<ChargingPile>>  getStationChargingPile() {
        HashMap<Integer, List<ChargingPile>> map = new HashMap<>();
        list().forEach(chargingPile -> {
            int stationId = chargingPile.getStationId();
            if(map.containsKey(stationId)) {
                map.get(stationId).add(chargingPile);
            } else {
                map.put(stationId, new ArrayList<ChargingPile>(){{add(chargingPile);}});
            }
        });
        return map;
    }

    public Map<Integer, List<ChargingPile>>  getStationChargingPile(List<Integer> stationIds) {
        HashMap<Integer, List<ChargingPile>> map = new HashMap<>();
        list(new QueryWrapper<ChargingPile>().in("station_id", stationIds)).forEach(chargingPile -> {
            int stationId = chargingPile.getStationId();
            if(map.containsKey(stationId)) {
                map.get(stationId).add(chargingPile);
            } else {
                map.put(stationId, new ArrayList<ChargingPile>(){{add(chargingPile);}});
            }
        });
        return map;
    }

    public List<ChargingPile> getChargingPileByStationId(int stationId) {
        return list(new QueryWrapper<ChargingPile>().eq("station_id", stationId));
    }
}
