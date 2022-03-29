package com.lijiahao.chargingpilebackend.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiahao.chargingpilebackend.controller.requestparam.MessageRequest;
import com.lijiahao.chargingpilebackend.entity.User;

public class JacksonTest {
    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TestFather father = new TestSon("son name");
        String str = mapper.writeValueAsString(father);
        System.out.println(str);
        TestSon son = mapper.readValue(str, TestSon.class);
        System.out.println(son);

        String json = "{\"firstName\":null,\"lastName\":\"son name\"}";
        TestFather father1 = mapper.readValue(json, TestFather.class);
        System.out.println("father1:" + father1);
    }
}
