package com.lijiahao.chargingpilebackend.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiahao.chargingpilebackend.utils.TimeUtils;


import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;

public class JwtTest {
    public static void main(String[] args) throws UnsupportedEncodingException, JsonProcessingException {
        String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxIiwiZXhwIjoxNjUwNDQyMzE0LCJpYXQiOjE2NTAzNTU5MTR9.XPazDDvOGJhRFFKzzypqSYEzQZXVpeW7wRlJiV4jRss";
        String[] res = jwt.split("\\.");
        Arrays.stream(res).forEach(s -> {
                    try {
                        System.out.println(getJson(s));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
        );

        String payload = getJson(res[1]);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(payload);
        long time = node.get("exp").asLong() * 1000;
        System.out.println(time);
        LocalDateTime dateTime = TimeUtils.longToLocalDateTime(time);
        System.out.println(dateTime);
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.getDecoder().decode(strEncoded);
        return new String(decodedBytes, "UTF-8");
    }
}
