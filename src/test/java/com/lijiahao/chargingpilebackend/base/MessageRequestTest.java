package com.lijiahao.chargingpilebackend.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiahao.chargingpilebackend.controller.requestparam.MessageRequest;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

public class MessageRequestTest {
    public static void main(String[] args) throws JsonProcessingException {
        MessageRequest request = new MessageRequest(UUID.randomUUID().toString(), "IMAGE", "1", "2","", System.currentTimeMillis());
        ObjectMapper mapper = new ObjectMapper();
        String text = mapper.writeValueAsString(request);
        System.out.println(text);
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        System.out.println(bytes.length);

        HashMap<String, String> map = new HashMap<>();
        map.put("Hello", "World");
        String value = map.get("Workd");
        System.out.println(value  );

    }
}
