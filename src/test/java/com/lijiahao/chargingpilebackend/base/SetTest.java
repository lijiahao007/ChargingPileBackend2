package com.lijiahao.chargingpilebackend.base;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SetTest {
    public static void main(String[] args) {
        List<Integer> list1 = new ArrayList<Integer>();
        list1.add(1);
        List<Integer> list2 = new ArrayList(list1);
        System.out.println(list1 == list2);
        System.out.println(list1.equals(list2));
    }
}
