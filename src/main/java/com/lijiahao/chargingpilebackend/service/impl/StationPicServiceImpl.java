package com.lijiahao.chargingpilebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lijiahao.chargingpilebackend.entity.StationPic;
import com.lijiahao.chargingpilebackend.mapper.StationPicMapper;
import com.lijiahao.chargingpilebackend.service.IStationPicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
 * @since 2022-03-14
 */
@Service
public class StationPicServiceImpl extends ServiceImpl<StationPicMapper, StationPic> implements IStationPicService {
    public List<StationPic> getStationPicByStationId(Integer stationId) {
        return list(new QueryWrapper<StationPic>()
                .eq("station_id", stationId));
    }

    public List<String> getStationPicUrlByStationId(Integer stationId) {
        List<StationPic> stationPicList = getStationPicByStationId(stationId);
        return new ArrayList<String>() {{
            stationPicList.forEach(stationPic -> {
                add(stationPic.getUrl());
            });
        }};
    }

    public Map<Integer, List<String>> getStationPicUrl() {
        Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
        list().forEach(stationPic -> {
            Integer stationId = stationPic.getStationId();
            if (map.containsKey(stationId)) {
                map.get(stationId).add(stationPic.getUrl());
            } else {
                map.put(stationId, new ArrayList<String>() {{
                    add(stationPic.getUrl());
                }});
            }
        });
        return map;
    }




    public Map<Integer, List<String>> getStationPicUrl(List<Integer> stationIds) {
        Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
        list(new QueryWrapper<StationPic>().in("station_id", stationIds)).forEach(stationPic -> {
            Integer stationId = stationPic.getStationId();
            if (map.containsKey(stationId)) {
                map.get(stationId).add(stationPic.getUrl());
            } else {
                map.put(stationId, new ArrayList<String>() {{
                    add(stationPic.getUrl());
                }});
            }
        });
        return map;
    }



    public Map<Integer, List<String>> getStationPicUrlWithPrefix(Map<Integer, List<String>> urlMap, HttpServletRequest request) {
        // 获取可以直接访问图片的url
        String urlPrefix = "http://" + request.getServerName() + ":" + request.getServerPort() + "/chargingPileStation/getStationPic?url=";
        for (Integer key : urlMap.keySet()) {
            List<String> urls = urlMap.get(key);
            for (int i = 0; i < urls.size(); i++) {
                urls.set(i, urlPrefix + urls.get(i));
            }
            urlMap.put(key, urls);
        }
        return urlMap;
    }

    public List<String> getStationPicUrlWithPrefix(List<String> urlList, HttpServletRequest request) {
        // 获取可以直接访问图片的url
        ArrayList<String> res = new ArrayList<>();
        String urlPrefix = "http://" + request.getServerName() + ":" + request.getServerPort() + "/chargingPileStation/getStationPic?url=";
        for (String s : urlList) {
            res.add(urlPrefix + s);
        }
        return res;
    }



}
