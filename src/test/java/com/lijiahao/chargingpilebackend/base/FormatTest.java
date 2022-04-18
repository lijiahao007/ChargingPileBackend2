package com.lijiahao.chargingpilebackend.base;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FormatTest {
    public static void main(String[] args) {
        ArrayList<Integer> a = new ArrayList() {{
            add(3);
            add(1);
            add(6);
        }};

        a.sort((o1, o2) -> {
            if (o1 < o2) {return -1;}
            else if (o1 == o2) {return 0;}
            else {return -1;}
        });

        ArrayList<LocalTime> times = new ArrayList<LocalTime> () {{
            add(LocalTime.of(0, 0));
            add(LocalTime.of(12, 0));
            add(LocalTime.of(8, 0));
        }};
        times.sort((o1, o2) -> {
           if (o1.isBefore(o2)) {return -1;}
           else if (o1.isAfter(o2)) {return 1;}
           else {return 0;}
        });

        times.forEach(System.out::println);


        System.out.println(LocalTime.now());
        System.out.println(LocalDateTime.now());
        LocalDateTime dateTime = LocalDateTime.now();
        String format = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd\'T\'HH:mm:ss"));
        System.out.println(format);
        LocalDateTime parseDateTime = LocalDateTime.parse(format);
        System.out.println();

        LocalTime time = LocalTime.parse("07:16");
        System.out.println(time.getHour() + " : " + time.getMinute());

        System.out.println(time.format(DateTimeFormatter.ofPattern("HH:mm")));
        String b = DateTimeFormatter.ofPattern("HH:mm").format(LocalTime.now());
        System.out.println("b:" + b);

        LocalDate date = LocalDate.now();

    }
}
