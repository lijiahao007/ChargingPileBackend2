package com.lijiahao.chargingpilebackend.base;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SetTest {
    public static void main(String[] args) {
        File file = new File("C:\\Users\\10403\\Desktop\\abc");
        if (file.mkdir()) {
            System.out.println("file.mkdir = true");
        } else {
            System.out.println("file.mkdir = false");
        }
    }
}
