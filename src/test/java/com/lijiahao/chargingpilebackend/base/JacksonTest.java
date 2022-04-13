package com.lijiahao.chargingpilebackend.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiahao.chargingpilebackend.controller.requestparam.MessageRequest;
import com.lijiahao.chargingpilebackend.entity.User;

public class JacksonTest {
    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();


        TestFather father2 = new TestFather("father name");
        String json1 = mapper.writeValueAsString(father2);
        System.out.println("father2"+json1);

        String json = "{\"firstName\":\"father name\"}";
        TestFather father = mapper.readValue(json, TestFather.class);
        System.out.println(father.toString());




    }
}
