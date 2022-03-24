package com.lijiahao.chargingpilebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lijiahao.chargingpilebackend.entity.StationPic;
import com.lijiahao.chargingpilebackend.mapper.StationPicMapper;
import com.lijiahao.chargingpilebackend.service.IStationPicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class StationPicServiceImpl extends ServiceImpl<StationPicMapper, StationPic> implements IStationPicService {
    public List<StationPic> getStationPicByStationId(Integer stationId){
        return list(new QueryWrapper<StationPic>()
                .eq("station_id",stationId));
    }

    public List<String> getStationPicUrlByStationId(Integer stationId){
        List<StationPic> stationPicList = getStationPicByStationId(stationId);
        return new ArrayList<String>() {{
            stationPicList.forEach(stationPic -> {
                add(stationPic.getUrl());
            });
        }};
    }
}
