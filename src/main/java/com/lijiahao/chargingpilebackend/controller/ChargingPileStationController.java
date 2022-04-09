package com.lijiahao.chargingpilebackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiahao.chargingpilebackend.controller.requestparam.LatLngBound;
import com.lijiahao.chargingpilebackend.controller.requestparam.StationInfoRequest;
import com.lijiahao.chargingpilebackend.controller.response.StationAllInfo;
import com.lijiahao.chargingpilebackend.entity.*;
import com.lijiahao.chargingpilebackend.service.impl.*;
import com.lijiahao.chargingpilebackend.utils.FileUtils;
import com.lijiahao.chargingpilebackend.utils.TimeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lijiahao
 * @since 2022-03-14
 */
@RestController
@RequestMapping("/chargingPileStation")
@Api(value = "ChargingPileStationController")
@Slf4j
public class ChargingPileStationController {

    private final UserServiceImpl userService;
    private final ChargingPileServiceImpl chargingPileService;
    private final OpenTimeServiceImpl openTimeService;
    private final ChargingPileStationServiceImpl chargingPileStationService;
    private final StationPicServiceImpl stationPicService;
    private final TagsStationMapServiceImpl tagsStationMapService;
    private final OpenDayInWeekServiceImpl openDayInWeekService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public ChargingPileStationController(
            UserServiceImpl userService,
            ChargingPileServiceImpl chargingPileService,
            OpenTimeServiceImpl openTimeService,
            ChargingPileStationServiceImpl chargingPileStationService,
            StationPicServiceImpl stationPicService,
            TagsStationMapServiceImpl tagsStationMapService,
            OpenDayInWeekServiceImpl openDayInWeekService) {
        this.userService = userService;
        this.chargingPileService = chargingPileService;
        this.openTimeService = openTimeService;
        this.chargingPileStationService = chargingPileStationService;
        this.stationPicService = stationPicService;
        this.tagsStationMapService = tagsStationMapService;
        this.openDayInWeekService = openDayInWeekService;
    }

    private int index = 1;

    // 插入测试使用的User、ChargingPileStation
    @PostMapping("/test")
    @ApiOperation(value = "插入测试使用的User、ChargingPileStation")
    public String insertTestingUserAndStation(ChargingPileStation chargingPileStation) throws JsonProcessingException {
        // 先插入随机生成的User
        userService.save(new User(randomPhone(), "123456", "测试用户" + index, "C:\\Users\\10403\\Desktop\\imgs\\user_pic\\131196f0393c9deb7be095ab0f23708d.jpeg"));

        int id = userService.getOne(new QueryWrapper<User>().select("max(id) as id")).getId();

        // 插入测试用的ChargingPileStation
        chargingPileStation.setUserId(id);
        chargingPileStation.setName("测试充电桩" + String.valueOf(index));
        chargingPileStation.setParkFee(1.3f);
        chargingPileStation.setCollection(0);
        chargingPileStationService.save(chargingPileStation);
        int chargingStationId = chargingPileStationService.getOne(new QueryWrapper<ChargingPileStation>().select("max(id) as id")).getId();

        // 插入测试用的ChargingPile
        chargingPileService.save(new ChargingPile("交流", 7f, 1, chargingStationId));

        // 插入测试用的OpenTime
        openTimeService.save(new OpenTime(LocalTime.of(0, 0), LocalTime.of(23, 59), 1.3f, chargingStationId));

        // 插入测试用的StationPic
        stationPicService.save(new StationPic("C:\\Users\\10403\\Desktop\\imgs\\station_pic\\station_1.jpg", chargingStationId));

        index++;
        String response = "success userId:" + id + " stationId:" + chargingStationId;
        return mapper.writeValueAsString(response);
    }

    public String randomPhone() {
        Random random = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        sb.append(1);
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }


    @ApiOperation(value = "查询地图上矩形范围内的充电桩站点")
    @GetMapping("/getRangeStation")
    public List<ChargingPileStation> getRangeStationLocation(LatLngBound latLngBound) {

        double lon_min = latLngBound.getLongitude_southwest();
        double lon_max = latLngBound.getLongitude_northeast();

        double lat_min = latLngBound.getLatitude_southwest();
        double lat_max = latLngBound.getLatitude_northeast();

        // 根据经纬度范围查询充电桩站

        return chargingPileStationService.list(new QueryWrapper<ChargingPileStation>()
                .between("longitude", lon_min, lon_max)
                .between("latitude", lat_min, lat_max));
    }

    @ApiOperation(value = "获取所有充电桩站点")
    @GetMapping("/getStation")
    public List<ChargingPileStation> getStation() {
        return chargingPileStationService.list();
    }

    @ApiOperation("获取每个充电桩站点对应的标签")
    @GetMapping("/getStationTags")
    public Map<Integer, List<Tags>> getStationTags() {
        return tagsStationMapService.getStationTags();
    }

    @ApiOperation("获取每个站点对应的充电桩")
    @GetMapping("/getStationPiles")
    public Map<Integer, List<ChargingPile>> getStationPiles() {
        return chargingPileService.getStationChargingPile();
    }

    @ApiOperation("获取每个站点对应的开放时间")
    @GetMapping("/getStationOpenTime")
    public Map<Integer, List<OpenTime>> getStationOpenTime() {
        return openTimeService.getStationOpenTime();
    }

    @ApiOperation("获取每个站点开放的日子")
    @GetMapping("/getStationOpenDay")
    public Map<Integer, List<OpenDayInWeek>> getStationOpenDay() {
        return openDayInWeekService.getOpenDayInWeek();
    }

    @ApiOperation("获取所有充电站的所有信息")
    @GetMapping("/getStationInfo")
    public StationAllInfo getStationInfo(HttpServletRequest request) {
        List<ChargingPileStation> stations = chargingPileStationService.list();
        Map<Integer, List<Tags>> tagMap = tagsStationMapService.getStationTags();
        Map<Integer, List<ChargingPile>> pileMap = chargingPileService.getStationChargingPile();
        Map<Integer, List<OpenTime>> openTimeMap = openTimeService.getStationOpenTime();
        Map<Integer, List<OpenDayInWeek>> openDayInWeekMap = openDayInWeekService.getOpenDayInWeek();
        Map<Integer, List<String>> picMap = stationPicService.getStationPicUrl();
        picMap = stationPicService.getStationPicUrlWithPrefix(picMap, request);
        return new StationAllInfo(stations, tagMap, pileMap, openTimeMap, openDayInWeekMap, picMap);
    }

    @ApiOperation("获取某个用户相关的所有充电站的所有信息")
    @GetMapping("/getStationInfoByUserId")
    public StationAllInfo getStationInfoByUserId(@RequestParam("userId") int userId, HttpServletRequest request) {
        List<ChargingPileStation> stations = chargingPileStationService.list(new QueryWrapper<ChargingPileStation>().eq("user_id", userId));
        List<Integer> stationIds = chargingPileStationService.getUserStation(userId);
        Map<Integer, List<Tags>> tagMap = tagsStationMapService.getStationTags(stationIds);
        Map<Integer, List<ChargingPile>> pileMap = chargingPileService.getStationChargingPile(stationIds);
        Map<Integer, List<OpenTime>> openTimeMap = openTimeService.getStationOpenTime(stationIds);
        Map<Integer, List<OpenDayInWeek>> openDayInWeekMap = openDayInWeekService.getOpenDayInWeek(stationIds);
        Map<Integer, List<String>> picMap = stationPicService.getStationPicUrl(stationIds);
        picMap = stationPicService.getStationPicUrlWithPrefix(picMap, request);
        return new StationAllInfo(stations, tagMap, pileMap, openTimeMap, openDayInWeekMap, picMap);
    }


    @ApiOperation("获取每个站点对应的图片url")
    @GetMapping("/getStationPicUrl")
    public List<String> getStationPic(HttpServletRequest request, @RequestParam("stationId") int stationId) {
        // 返回 stationId 对应的所有图片的url
        List<String> urls = stationPicService.getStationPicUrlByStationId(stationId);
        // 处理一下所有的url，使其能够访问到图片
        String path = "http://" + request.getServerName() + ":" + request.getServerPort() + "/chargingPileStation/getStationPic?url=";
        for (int i = 0; i < urls.size(); i++) {
            urls.set(i, path + urls.get(i));
        }
        return urls;
    }

    @ApiOperation("根据getStationPicUrl中获取的url获取每个站点对应的图片")
    @GetMapping(value = "/getStationPic")
    public ResponseEntity<Resource> getStationPic(@RequestParam("url") String url) {
        // 根据url返回具体的图片
        File file = new File(url);
        FileSystemResource resource = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "private");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(FileUtils.getImageFileType(url))
                .body(resource);
    }

    @ApiOperation(value = "为某个充电站的收藏数量+1")
    @GetMapping("/addStationCollection")
    @Transactional
    public String addStationCollection(@RequestParam("stationId") int stationId) throws JsonProcessingException {
        ChargingPileStation station = chargingPileStationService.getOne(new QueryWrapper<ChargingPileStation>().eq("id", stationId));
        station.setCollection(station.getCollection() + 1);
        chargingPileStationService.updateById(station);
        return mapper.writeValueAsString("success");
    }

    @ApiOperation(value = "为某个充电站的收藏数量-1")
    @GetMapping("/subtractStationCollection")
    @Transactional
    public String substractStationCollection(@RequestParam("stationId") int stationId) throws JsonProcessingException {
        ChargingPileStation station = chargingPileStationService.getOne(new QueryWrapper<ChargingPileStation>().eq("id", stationId));
        station.setCollection(station.getCollection() - 1);
        chargingPileStationService.updateById(station);
        return mapper.writeValueAsString("success");
    }


    @ApiOperation(value = "上传充电站相关信息")
    @PostMapping("/uploadStationInfo")
    @Transactional
    public String uploadStationInfo(@RequestBody StationInfoRequest stationInfoRequest) throws JsonProcessingException, Exception {

        ChargingPileStation station = stationInfoRequest.getStation();
        List<String> openDayInWeek = stationInfoRequest.getOpenDayInWeek();
        List<String> openTime = stationInfoRequest.getOpenTime();
        List<Float> openTimeCharge = stationInfoRequest.getOpenTimeCharge();
        List<ChargingPile> chargingPiles = stationInfoRequest.getChargingPiles();
        String userId = stationInfoRequest.getUserId();

        log.warn("接收到的站点信息：" + station);
        log.warn("接收到的开放时间信息：" + openDayInWeek);
        log.warn("接收到的开放时间信息：" + openTime);
        log.warn("接收到的开放时间收费信息：" + openTimeCharge);
        log.warn("接收到的充电桩信息：" + chargingPiles);
        log.warn("接收到的用户id：" + userId);

        // 将station保存到数据库
        station.setUserId(Integer.valueOf(userId));
        station.setCollection(0); // 初始化收藏数
        chargingPileStationService.save(station);
        int stationId = station.getId();

        // 将openDayWeek保存到数据库
        for (String dayWeek : openDayInWeek) {
            openDayInWeekService.save(new OpenDayInWeek(dayWeek, stationId));
        }

        // 将openTime保存到数据库
        for (int i = 0; i < openTime.size(); i++) {
            List<LocalTime> localTimes = TimeUtils.stringToLocalTime(openTime.get(i));
            OpenTime time = new OpenTime(localTimes.get(0), localTimes.get(1), openTimeCharge.get(i), stationId);
            openTimeService.save(time);
        }

        // 将chargingPiles保存到数据库
        for (ChargingPile chargingPile : chargingPiles) {
            chargingPile.setState("空闲");
            chargingPile.setStationId(stationId);
            chargingPileService.save(chargingPile);
        }
        return mapper.writeValueAsString(stationId);
    }

    @ApiOperation(value = "上传充电站图片")
    @PostMapping("/uploadStationPic")
    @Transactional
    public String uploadStationPic(@RequestParam("stationPic") List<MultipartFile> files, @RequestParam("stationId") String stationId) throws Exception {
        for (MultipartFile file : files) {
            byte[] fileBytes = file.getBytes();
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            File outputFile = File.createTempFile("stationPic", FileUtils.getFileSuffix(fileName), new File("C:\\Users\\10403\\Desktop\\imgs\\station_pic\\"));
            FileUtils.writeByteArrayToFile(outputFile, fileBytes);
            String path = outputFile.getAbsolutePath();
            stationPicService.save(new StationPic(path, Integer.valueOf(stationId)));
            log.warn("上传图片成功");
        }
        return mapper.writeValueAsString("success filesize=" + files.size());
    }

    @ApiOperation(value = "修改充电站消息")
    @PostMapping("modifyStationInfo")
    @Transactional
    public String modifyStationInfo(@RequestParam("stationInfo") StationInfoRequest stationInfoRequest,
                                    @RequestParam("remotePicsUris") List<String> remotePicsUris,
                                    @RequestParam("newPics") List<MultipartFile> newImages) throws IOException {

        log.warn("stationInfo " + stationInfoRequest.toString());
        log.warn("remotePicsUris " + remotePicsUris);
        log.warn("newPics " + newImages);

        // 从表单获取的字符串会多了一对双引号！！！
        for (int i = 0; i < remotePicsUris.size(); i++) {
            remotePicsUris.set(i, remotePicsUris.get(i).replace("\\\\", "\\"));
            remotePicsUris.set(i, remotePicsUris.get(i).replace("\"", ""));
        }

        // 1. 更新充电站信息
        ChargingPileStation station = stationInfoRequest.getStation();
        chargingPileStationService.updateById(station);

        // 2. 更新OpenDayInWeek
        Integer stationId = station.getId();
        openDayInWeekService.remove(new QueryWrapper<OpenDayInWeek>().eq("station_id", stationId));
        stationInfoRequest.getOpenDayInWeek().forEach(day ->
                openDayInWeekService.save(new OpenDayInWeek(day, stationId)));

        // 3. 更新OpenTime
        openTimeService.remove(new QueryWrapper<OpenTime>().eq("station_id", stationId));
        List<String> openTime = stationInfoRequest.getOpenTime();
        List<Float> openTimeCharge = stationInfoRequest.getOpenTimeCharge();
        for (int i = 0; i < openTime.size(); i++) {
            List<LocalTime> localTimes = TimeUtils.stringToLocalTime(openTime.get(i));
            OpenTime time = new OpenTime(localTimes.get(0), localTimes.get(1), openTimeCharge.get(i), stationId);
            openTimeService.save(time);
        }

        // 4. 更新ChargingPile
        chargingPileService.remove(new QueryWrapper<ChargingPile>().eq("station_id", stationId));
        List<ChargingPile> piles = stationInfoRequest.getChargingPiles();
        piles.forEach(pile -> {
            pile.setStationId(stationId);
            chargingPileService.save(pile);
        });

        // 5. 删除不在remotePicsUris中的图片
        List<StationPic> targetList = stationPicService.list(new QueryWrapper<StationPic>().eq("station_id", stationId).notIn("url", remotePicsUris));
        log.warn(targetList.toString());
        stationPicService.remove(new QueryWrapper<StationPic>()
                        .eq("station_id", stationId)
                        .notIn("url", remotePicsUris));


        // 6. 添加新的照片
        for (MultipartFile file : newImages) {
            File localFile = FileUtils.writeMultipartFileToLocal(file, "C:\\Users\\10403\\Desktop\\imgs\\station_pic\\", "stationPic");
            stationPicService.save(new StationPic(localFile.getAbsolutePath(), stationId));
        }

        return mapper.writeValueAsString("success");
    }



    @ApiOperation(value = "修改充电站消息")
    @PostMapping("modifyStationInfoWithoutNewPic")
    @Transactional
    public String modifyStationInfo(@RequestParam("stationInfo") StationInfoRequest stationInfoRequest,
                                    @RequestParam("remotePicsUris") List<String> remotePicsUris) throws IOException {

        log.warn("stationInfo " + stationInfoRequest.toString());
        log.warn("remotePicsUris " + remotePicsUris);

        // 从表单获取的字符串会多了一对双引号！！！
        for (int i = 0; i < remotePicsUris.size(); i++) {
            remotePicsUris.set(i, remotePicsUris.get(i).replace("\\\\", "\\"));
            remotePicsUris.set(i, remotePicsUris.get(i).replace("\"", ""));
        }

        // 1. 更新充电站信息
        ChargingPileStation station = stationInfoRequest.getStation();
        chargingPileStationService.updateById(station);

        // 2. 更新OpenDayInWeek
        Integer stationId = station.getId();
        openDayInWeekService.remove(new QueryWrapper<OpenDayInWeek>().eq("station_id", stationId));
        stationInfoRequest.getOpenDayInWeek().forEach(day ->
                openDayInWeekService.save(new OpenDayInWeek(day, stationId)));

        // 3. 更新OpenTime
        openTimeService.remove(new QueryWrapper<OpenTime>().eq("station_id", stationId));
        List<String> openTime = stationInfoRequest.getOpenTime();
        List<Float> openTimeCharge = stationInfoRequest.getOpenTimeCharge();
        for (int i = 0; i < openTime.size(); i++) {
            List<LocalTime> localTimes = TimeUtils.stringToLocalTime(openTime.get(i));
            OpenTime time = new OpenTime(localTimes.get(0), localTimes.get(1), openTimeCharge.get(i), stationId);
            openTimeService.save(time);
        }

        // 4. 更新ChargingPile
        chargingPileService.remove(new QueryWrapper<ChargingPile>().eq("station_id", stationId));
        List<ChargingPile> piles = stationInfoRequest.getChargingPiles();
        piles.forEach(pile -> {
            pile.setStationId(stationId);
            chargingPileService.save(pile);
        });

        // 5. 删除不在remotePicsUris中的图片
        stationPicService.remove(new QueryWrapper<StationPic>()
                .eq("station_id", stationId)
                .notIn("url", remotePicsUris));


        return mapper.writeValueAsString("success");
    }


}

