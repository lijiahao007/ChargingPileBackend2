package com.lijiahao.chargingpilebackend.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class regexTest {
    public static void main(String[] args) {
        String str = "attachment; filename=\"1.jpeg\"";
        String regex = "\"(.*?)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            System.out.println(matcher.group(0));
            String fileName = matcher.group(0);
            fileName = fileName.substring(1, fileName.length() - 1);
            System.out.println(fileName);
        }

        System.out.println("C:\\Users\\10403\\Desktop\\imgs\\station_pic\\station_1.jpg");

    }
}
