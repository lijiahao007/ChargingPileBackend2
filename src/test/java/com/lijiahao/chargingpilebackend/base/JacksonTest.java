package com.lijiahao.chargingpilebackend.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiahao.chargingpilebackend.entity.User;

public class JacksonTest {
    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String res = mapper.writeValueAsString(new User("135354984561", "lijiahao"));
        System.out.println(res);

    }
}
