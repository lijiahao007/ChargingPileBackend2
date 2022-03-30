package com.lijiahao.chargingpilebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lijiahao.chargingpilebackend.service.IChargingPileStationService;
import com.lijiahao.chargingpilebackend.entity.ChargingPileStation;
import com.lijiahao.chargingpilebackend.mapper.ChargingPileStationMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lijiahao
 * @since 2022-03-14
 */
@Service
public class ChargingPileStationServiceImpl extends ServiceImpl<ChargingPileStationMapper, ChargingPileStation> implements IChargingPileStationService {

    public List<Integer> getUserStation(int userId) {
        ArrayList<Integer> stationIds = new ArrayList<>();
        list(new QueryWrapper<ChargingPileStation>().select("id").eq("user_id", userId))
                .forEach(chargingPileStation -> stationIds.add(chargingPileStation.getId()));
        return stationIds;
    }

}
