package com.lijiahao.chargingpilebackend.databaseTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lijiahao.chargingpilebackend.entity.*;
import com.lijiahao.chargingpilebackend.service.impl.*;
import com.lijiahao.chargingpilebackend.utils.QRCodeUtils;
import org.hamcrest.object.HasToString;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
    private final CommentServiceImpl commentService;
    private final OrderServiceImpl orderService;
    private final ChargingPileStationServiceImpl chargingPileStationService;

    @Autowired
    public ServiceTest(
            UserServiceImpl userService,
            OpenTimeServiceImpl openTimeService,
            TagsStationMapServiceImpl tagsStationMapService,
            TagsServiceImpl tagsService,
            ChargingPileServiceImpl chargingPileService,
            StationPicServiceImpl stationPicService,
            CommentServiceImpl commentService,
            OrderServiceImpl orderService,
            ChargingPileStationServiceImpl chargingPileStationService) {
        this.userService = userService;
        this.openTimeService = openTimeService;
        this.tagsStationMapService = tagsStationMapService;
        this.tagsService = tagsService;
        this.chargingPileService = chargingPileService;
        this.stationPicService = stationPicService;
        this.commentService = commentService;
        this.orderService = orderService;
        this.chargingPileStationService = chargingPileStationService;
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
        List<TagsStationMap> list = tagsStationMapService.list(new QueryWrapper<TagsStationMap>());
        Map<Integer, List<Tags>> map = new HashMap<>();
        list.forEach((TagsStationMap tagsStationMap) -> {
            Tags tag = tagsService.getOne(new QueryWrapper<Tags>().eq("id", tagsStationMap.getTagsId()));
            if (map.containsKey(tagsStationMap.getStationId())) {
                map.get(tagsStationMap.getStationId()).add(tag);
            } else {
                map.put(tagsStationMap.getStationId(), new ArrayList<Tags>() {{
                    add(tag);
                }});
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
            if (map.containsKey(stationId)) {
                map.get(stationId).add(chargingPile);
            } else {
                map.put(stationId, new ArrayList<ChargingPile>() {{
                    add(chargingPile);
                }});
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
            if (map.containsKey(stationId)) {
                map.get(stationId).add(openTime);
            } else {
                map.put(stationId, new ArrayList<OpenTime>() {{
                    add(openTime);
                }});
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


    @Test
    public void generateQRCode() {
        // 为每一个没有二维码的充电桩创建二维码
        List<ChargingPile> list = chargingPileService.list(new QueryWrapper<ChargingPile>().isNull("qrcode_url"));
        list.forEach(chargingPile -> {
            try {
                QRCodeContent content = new QRCodeContent(chargingPile.getStationId().toString(), chargingPile.getId().toString());
                String prefix = chargingPile.getStationId() + "_" + chargingPile.getId() + "_";
                File file = File.createTempFile(prefix, ".png", new File("C:\\Users\\10403\\Desktop\\imgs\\pile_qrcode"));
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                QRCodeUtils.getQRCode(content.getJson(), fileOutputStream);
                chargingPile.setQrcodeUrl(file.getAbsolutePath());
                chargingPileService.updateById(chargingPile);
                System.out.println("成功写入：" + chargingPile.getId() + " " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void deleteBadQRCodeFile() {
        // 删除已经不存在的充电桩对应二维码
        File file = new File("C:\\Users\\10403\\Desktop\\imgs\\pile_qrcode");
        File[] childFiles = file.listFiles();
        List<ChargingPile> list = chargingPileService.list();
        List<String> goodPath = list.stream().map(ChargingPile::getQrcodeUrl).collect(Collectors.toList());
        assert childFiles != null;
        for (File tmpFile : childFiles) {
            if (goodPath.contains(tmpFile.getAbsolutePath())) {
                System.out.println(tmpFile.getAbsolutePath() + "   good");
            } else {
                boolean isDelete = tmpFile.delete();
                System.out.println(tmpFile.getAbsolutePath() + "   bad  isDelete:" + isDelete);
            }
        }
    }

    @Test
    public void updateScoreAndUsedTime() {
        // 更新ChargingPileStation的分数和使用次数
        List<Order> orders = orderService.list();
        List<Comment> comments = commentService.list();
        Map<Integer, List<Double>> scoreMap = new HashMap<>();
        Map<Integer, Integer> usedTimeMap = new HashMap<>();

        orders.forEach(order -> {
            Integer pileId = order.getPileId();
            Integer stationId = chargingPileService.getById(pileId).getStationId();
            if (usedTimeMap.containsKey(stationId)) {
                usedTimeMap.put(stationId, usedTimeMap.get(stationId) + 1);
            } else {
                usedTimeMap.put(stationId, 1);
            }
        });
        comments.forEach(comment -> {
            Integer stationId = comment.getStationId();
            if (scoreMap.containsKey(stationId)) {
                scoreMap.get(stationId).add(Integer.parseInt(comment.getStar()) * 1.0);
            } else {
                scoreMap.put(stationId, new ArrayList<Double>() {{
                    add(Integer.parseInt(comment.getStar()) * 1.0);
                }});
            }
        });

        scoreMap.forEach((stationId, scores) -> {
            double sum = scores.stream().mapToDouble(Double::doubleValue).sum();
            double avg = sum / scores.size();
            ChargingPileStation station = chargingPileStationService.getById(stationId);
            station.setScore(avg);
            chargingPileStationService.updateById(station);
        });

        usedTimeMap.forEach((stationId, usedTime) -> {
            ChargingPileStation station = chargingPileStationService.getById(stationId);
            station.setUsedTime(usedTime);
            chargingPileStationService.updateById(station);
        });

    }


    @Test
    public void testTimeStamp() {
        LocalDateTime beginTime = LocalDateTime.of(2022, 4, 20, 16, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2022, 4, 20, 20, 0, 0);

        List<Order> list = orderService.list(
                new QueryWrapper<Order>()
                        .not(orderQueryWrapper -> orderQueryWrapper.le("complete_time", beginTime)
                                .or().ge("create_time", endTime)));

        long count = orderService.count(new QueryWrapper<Order>()
                .not(orderQueryWrapper -> orderQueryWrapper.le("complete_time", beginTime)
                        .or().ge("create_time", endTime)));
        list.forEach (order -> {
            System.out.println("order!!!:=" + order);
        });
        System.out.println("count = " + count);
    }

    @Test
    public void testQueryNothing() {
        Order order = orderService.getById(0);
        System.out.println("order = " + order);
    }
}
