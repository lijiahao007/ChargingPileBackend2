package com.lijiahao.chargingpilebackend.base;

import java.util.UUID;

public class UUIDTest {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            System.out.println(UUID.randomUUID().toString() + "  " + System.currentTimeMillis());
            Thread.sleep(100);
        }
    }
}
