package com.lijiahao.chargingpilebackend.base;

import java.util.Random;

public class StringBuilderTest {
    public static void main(String[] args) {
        StringBuilderTest test = new StringBuilderTest();
        String res = test.randomPhone();
        System.out.println(res);

    }

    public  String randomPhone() {
        Random random = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        sb.append(1);
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
