package com.lijiahao.chargingpilebackend.service.impl;

import com.lijiahao.chargingpilebackend.service.IChargingPileService;
import com.lijiahao.chargingpilebackend.entity.ChargingPile;
import com.lijiahao.chargingpilebackend.mapper.ChargingPileMapper;
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
public class ChargingPileServiceImpl extends ServiceImpl<ChargingPileMapper, ChargingPile> implements IChargingPileService {

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


}
