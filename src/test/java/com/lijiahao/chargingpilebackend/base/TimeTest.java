package com.lijiahao.chargingpilebackend.base;


import java.time.LocalTime;

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
    }
}
