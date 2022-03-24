package com.lijiahao.chargingpilebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lijiahao.chargingpilebackend.entity.Tags;
import com.lijiahao.chargingpilebackend.entity.TagsStationMap;
import com.lijiahao.chargingpilebackend.mapper.TagsStationMapMapper;
import com.lijiahao.chargingpilebackend.service.ITagsStationMapService;
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
public class TagsStationMapServiceImpl extends ServiceImpl<TagsStationMapMapper, TagsStationMap> implements ITagsStationMapService {


    private final TagsServiceImpl tagsService;

    @Autowired
    public TagsStationMapServiceImpl(TagsServiceImpl tagsService) {
        this.tagsService = tagsService;
    }

    // 返回每个站点的所有标签
    public Map<Integer, List<Tags>> getStationTags() {
        List<TagsStationMap> list =  list(new QueryWrapper<TagsStationMap>());
        Map<Integer, List<Tags>> map = new HashMap<>();
        list.forEach((TagsStationMap tagsStationMap) -> {
            Tags tag = tagsService.getOne(new QueryWrapper<Tags>().eq("id", tagsStationMap.getTagsId()));
            if(map.containsKey(tagsStationMap.getStationId())) {
                map.get(tagsStationMap.getStationId()).add(tag);
            } else {
                map.put(tagsStationMap.getStationId(), new ArrayList<Tags>(){{add(tag);}});
            }
        });
        return map;
    }
}
