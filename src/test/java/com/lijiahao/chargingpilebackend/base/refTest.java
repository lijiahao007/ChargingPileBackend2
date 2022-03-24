package com.lijiahao.chargingpilebackend.base;


import com.lijiahao.chargingpilebackend.entity.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class refTest {
    public static void main(String[] args) {

        Double a = 3.4561215456;
        BigDecimal b = new BigDecimal(a);
        Double c = b.setScale(2, RoundingMode.HALF_UP).doubleValue();
        System.out.println(c);

        ArrayList<Integer> list = new ArrayList<Integer>(){{add(1);add(2);add(3);}};
        list.forEach((Integer item)->{item = item+1;});
        list.forEach((Integer item)->{System.out.println(item);});
    }
}
