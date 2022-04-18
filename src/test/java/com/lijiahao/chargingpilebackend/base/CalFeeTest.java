package com.lijiahao.chargingpilebackend.base;

import com.lijiahao.chargingpilebackend.entity.ElectricChargePeriod;
import com.lijiahao.chargingpilebackend.entity.OpenTime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CalFeeTest {
    public static void main(String[] args) {
        ArrayList<ElectricChargePeriod> openTimes = new ArrayList<ElectricChargePeriod>() {{
            add(new ElectricChargePeriod(0, LocalTime.of(0, 0, 0), LocalTime.of(10, 0, 0), 1, 1.0));
            add(new ElectricChargePeriod(1, LocalTime.of(10, 0, 0), LocalTime.of(16, 0, 0), 1, 2.0));
            add(new ElectricChargePeriod(2, LocalTime.of(16, 0, 0), LocalTime.of(23, 59, 59),  1, 3.0));
        }};

        LocalTime beginTime = LocalTime.of(7, 45, 0);
        LocalTime endTime = LocalTime.of(18, 13, 20);
        double powerRate = 7;
        CalFeeTest calFee = new CalFeeTest();
        double sumFee = calFee.calChargingFee(openTimes, beginTime, endTime, powerRate);
        System.out.println("sumFee: " + sumFee);

        LocalDateTime beginDateTime = LocalDateTime.of(2020, 1, 1, 7, 45, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2020, 1, 3, 6, 13, 20);
        double sumFee1 = calFee.calChargingFee(openTimes, beginDateTime, endDateTime, powerRate);
        System.out.println("sumFee1: " + sumFee1);

        LocalDateTime beginDateTime1 = LocalDateTime.of(2020, 1, 1, 7, 45, 0);
        LocalDateTime endDateTime1 = LocalDateTime.of(2020, 1, 1, 18, 13, 40);
        double sumFee2 = calFee.calChargingFee(openTimes, beginDateTime1, endDateTime1, powerRate);
        System.out.println("sumFee1: " + sumFee2);
    }

    /**
     * 计算同一天 beginTime ~ endTime 的充电费用
     * @param electricChargePeriods 充电桩营业时间
     * @param beginTime 充电开始时间
     * @param endTime   充电结束时间
     * @param powerRate 充电桩功率
     * @return
     */
    private double calChargingFee(List<ElectricChargePeriod> electricChargePeriods, LocalTime beginTime, LocalTime endTime, double powerRate) {
        double sumPrice = 0.0;

        // 1. 订单在同一天开始和结束
        int beginIndex = 0; // 开始时间段
        int endIndex = 0; // 结束时间段
        for (int i = 0; i < electricChargePeriods.size(); i++) {
            ElectricChargePeriod openTime = electricChargePeriods.get(i);
            if (isBetween(beginTime, openTime.getBeginTime(), openTime.getEndTime())) {
                beginIndex = i;
            }
            if (isBetween(endTime, openTime.getBeginTime(), openTime.getEndTime())) {
                endIndex = i;
            }
        }
        if (beginIndex != endIndex) {
            // 1.1 计算第一个时间段的价格
            ElectricChargePeriod firstOpenTime = electricChargePeriods.get(beginIndex);
            Duration firstPeriod = Duration.between(beginTime, firstOpenTime.getEndTime());
            long firstSecond = firstPeriod.toMillis() / 1000;
            double firstPrice = (firstSecond / 3600.0) * powerRate * firstOpenTime.getElectricCharge(); // hour * kw/h * 元/kw = 该时间段的
            sumPrice += firstPrice;

            // 1.2 计算最后一个时间段的价格
            ElectricChargePeriod lastOpenTime = electricChargePeriods.get(endIndex);
            Duration lastPeriod = Duration.between(lastOpenTime.getBeginTime(), endTime);
            long lastSecond = lastPeriod.toMillis() / 1000;
            double lastPrice = (lastSecond / 3600.0) * powerRate * lastOpenTime.getElectricCharge();
            sumPrice += lastPrice;

            // 1.3 计算中间时间段的价格
            for (int i = beginIndex + 1; i <= endIndex - 1; i++) {
                ElectricChargePeriod midOpenTime = electricChargePeriods.get(i);
                Duration midPeriod = Duration.between(midOpenTime.getBeginTime(), midOpenTime.getEndTime());
                long midSecond = midPeriod.toMillis() / 1000;
                double midPrice = (midSecond / 3600.0) * powerRate * midOpenTime.getElectricCharge();
                sumPrice += midPrice;
            }
        } else {
            ElectricChargePeriod openTime = electricChargePeriods.get(beginIndex);
            Duration duration = Duration.between(beginTime, endTime);
            long second = duration.toMinutes();
            double price = (second / 3600.0) * powerRate * openTime.getElectricCharge();
            sumPrice += price;
        }
        return sumPrice;
    }

    /**
     * 计算从 beginDateTime, endDateTime 时间段内充电费用
     * @param electricChargePeriods 时间段收费情况
     * @param beginDateTime 开始时间
     * @param endDateTime 结束时间
     * @param powerRate 功率
     * @return 费用
     */
    private double calChargingFee(List<ElectricChargePeriod> electricChargePeriods, LocalDateTime beginDateTime, LocalDateTime endDateTime, double powerRate) {
        double sumPrice = 0.0;

        LocalTime beginTime = beginDateTime.toLocalTime();
        LocalTime endTime = endDateTime.toLocalTime();

        if (beginDateTime.getDayOfMonth() == endDateTime.getDayOfMonth()) {
            sumPrice += calChargingFee(electricChargePeriods, beginTime, endTime, powerRate);
        } else {
            // 2. 订单不在同一天(前提是该充电站营业时间是连续的)
            // 2.1 计算第一天的价格
            sumPrice += calChargingFee(electricChargePeriods, beginTime, LocalTime.of(23, 59, 59), powerRate);

            // 2.2 计算最后一天的价格
            sumPrice += calChargingFee(electricChargePeriods, LocalTime.of(0, 0, 0), endTime, powerRate);

            // 2.3 计算中间天的价格
            LocalDateTime tmpBegin = LocalDateTime.of(beginDateTime.getYear(), beginDateTime.getMonth(), beginDateTime.getDayOfMonth(), 0, 0, 0);
            LocalDateTime tmpEnd = LocalDateTime.of(endDateTime.getYear(), endDateTime.getMonth(), endDateTime.getDayOfMonth(), 0, 0, 0);
            Duration duration = Duration.between(tmpBegin, tmpEnd);
            long days = duration.toDays() - 1;
            for (int i = 0; i < days; i++) {
                sumPrice += calChargingFee(electricChargePeriods, LocalTime.of(0, 0, 0), LocalTime.of(23, 59, 59), powerRate);
            }
        }
        return sumPrice;
    }


    private Boolean isBetween(LocalTime target, LocalTime begin, LocalTime end) {
        return (target.equals(begin) || target.isAfter(begin)) && (target.equals(end) || target.isBefore(end));
    }

}
