package com.lijiahao.chargingpilebackend.utils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeUtils {
    // 从 00:00~15:20 转换成 两个LocalTime
    public static List<LocalTime> stringToLocalTime (String time) {
        String[] times = time.split("~");
        LocalTime startTime = LocalTime.parse(times[0]);
        LocalTime endTime = LocalTime.parse(times[1]);
        return new ArrayList<LocalTime>() {{add(startTime); add(endTime);}};
    }
}
