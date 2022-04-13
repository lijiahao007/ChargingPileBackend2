package com.lijiahao.chargingpilebackend.base;

import java.text.DecimalFormat;

public class FormatTest {
    public static void main(String[] args) {
        float a = 2.3456789f;
        String res = String.format("%.2f", a);
        float b = Float.parseFloat(res);
        System.out.println(b);
        System.out.println(res);

        DecimalFormat format = new DecimalFormat("#.00");
        String res1 = format.format(a);
        System.out.println(res1);

    }
}
