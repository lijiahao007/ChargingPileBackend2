package com.lijiahao.chargingpilebackend.databaseTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lijiahao.chargingpilebackend.entity.*;
import com.lijiahao.chargingpilebackend.service.impl.*;
import org.hamcrest.object.HasToString;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.util.Arrays.asList;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServiceTest {
    private static Logger log = LoggerFactory.getLogger(ServiceTest.class);
    private final UserServiceImpl userService;
    private final OpenTimeServiceImpl openTimeService;
    private final TagsStationMapServiceImpl tagsStationMapService;
    private final TagsServiceImpl tagsService;
    private final ChargingPileServiceImpl chargingPileService;
    private final StationPicServiceImpl stationPicService;
    @Autowired
    public ServiceTest(
            UserServiceImpl userService,
            OpenTimeServiceImpl openTimeService,
            TagsStationMapServiceImpl tagsStationMapService,
            TagsServiceImpl tagsService,
            ChargingPileServiceImpl chargingPileService,
            StationPicServiceImpl stationPicService){
        this.userService = userService;
        this.openTimeService = openTimeService;
        this.tagsStationMapService = tagsStationMapService;
        this.tagsService = tagsService;
        this.chargingPileService = chargingPileService;
        this.stationPicService = stationPicService;
    }

    @Test
    @Transactional
    public void saveTest() {
        List<User> userList = asList(new User("lijiahao", "123456"), new User("lijiahao1", "123456"), new User("lijiahao2", "123456"));
        boolean res = userService.saveBatch(userList);
        log.warn("saveTest: {}", res);
        List<User> list = userService.list(new QueryWrapper<User>()
                .like("name", "Dan"));

        for (User user : list) {
            log.warn("user: {}", user);
        }
    }

    @Test
    public void getTest() {
        User user = userService.getOne(new QueryWrapper<User>().select("MAX(id) as id"));
        log.warn("user: {}, {}", user, user.getId());
    }

    @Test
    public void TimeTest() {
        OpenTime openTime = openTimeService.getOne(null);
        System.out.println(openTime.getBeginTime());
        System.out.println(openTime.getEndTime());
    }

    @Test
    public void mapListTest() {
        List<TagsStationMap> list =  tagsStationMapService.list(new QueryWrapper<TagsStationMap>());
        Map<Integer, List<Tags>> map = new HashMap<>();
        list.forEach((TagsStationMap tagsStationMap) -> {
            Tags tag = tagsService.getOne(new QueryWrapper<Tags>().eq("id", tagsStationMap.getTagsId()));
            if(map.containsKey(tagsStationMap.getStationId())) {
                map.get(tagsStationMap.getStationId()).add(tag);
            } else {
                map.put(tagsStationMap.getStationId(), new ArrayList<Tags>(){{add(tag);}});
            }
        });

        map.forEach((Integer key, List<Tags> value) -> {
            System.out.println("key=" + key);
            System.out.println("value=" + value);
            System.out.println("\n\n\n");
        });
    }

    @Test
    public void chargingPileMapTest() {
        HashMap<Integer, List<ChargingPile>> map = new HashMap<>();
        chargingPileService.list().forEach(chargingPile -> {
            int stationId = chargingPile.getStationId();
            if(map.containsKey(stationId)) {
                map.get(stationId).add(chargingPile);
            } else {
                map.put(stationId, new ArrayList<ChargingPile>(){{add(chargingPile);}});
            }
        });
        map.forEach((Integer key, List<ChargingPile> value) -> {
            System.out.println("key=" + key);
            System.out.println("value=" + value);
            System.out.println("\n\n\n");
        });
    }

    @Test
    public void openTimeMapTest() {
        HashMap<Integer, List<OpenTime>> map = new HashMap<>();
        openTimeService.list().forEach(openTime -> {
            int stationId = openTime.getStationId();
            if(map.containsKey(stationId)) {
                map.get(stationId).add(openTime);
            } else {
                map.put(stationId, new ArrayList<OpenTime>(){{add(openTime);}});
            }
        });
        map.forEach((Integer key, List<OpenTime> value) -> {
            System.out.println("key=" + key);
            System.out.println("value=" + value);
            System.out.println("\n\n\n");
        });
    }

    @Test
    public void imagePathTest() {
        List<StationPic> list = stationPicService.list();
        String url = list.get(0).getUrl();
        String target = "C:\\Users\\10403\\Desktop\\imgs\\station_pic\\station_1.jpg";
        System.out.println(url.equals(target));
        System.out.println(url);
        System.out.println(target);
    }

}
