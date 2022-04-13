package com.lijiahao.chargingpilebackend.base;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class TimeTest {
    public static void main(String[] args) {
        LocalTime localTime = LocalTime.of(23, 59);
        System.out.println(localTime);
        String name = "hello" + 3;
        System.out.println(name);


        String time = "23:59";
        LocalTime localTime1 = LocalTime.parse(time);
        System.out.println(localTime1);
        System.out.println(System.currentTimeMillis());

        String localtimeStr = "2022-04-13T20:17:06";
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = LocalDateTime.parse(localtimeStr);
        System.out.println(now);
        System.out.println(localDateTime);
        String formatTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(formatTime);

        Duration duration = Duration.between(localDateTime, now);
        System.out.println(duration.toHours() + ":" + duration.toMinutes() + ":" + duration.toMillis()/1000%60);
        System.out.println();
        String hour = String.format("%02d", duration.toHours());
        String minute = String.format("%02d", duration.toMinutes());
        String second = String.format("%02d", duration.toMillis() / 1000 % 60);
        System.out.println(hour + ":" + minute + ":" + second);


        double c = 3.1415926525;
        String cStr = String.format("%.2f", c);
        System.out.println(cStr);

    }
}
