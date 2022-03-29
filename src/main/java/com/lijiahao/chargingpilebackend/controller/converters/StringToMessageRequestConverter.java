package com.lijiahao.chargingpilebackend.controller.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiahao.chargingpilebackend.controller.requestparam.MessageRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class StringToMessageRequestConverter implements Converter<String, MessageRequest> {
    @Override
    public MessageRequest convert(String source) {
        ObjectMapper mapper = new ObjectMapper();
        MessageRequest request = null;
        try {
            request = mapper.readValue(source, MessageRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return request;
    }
}
