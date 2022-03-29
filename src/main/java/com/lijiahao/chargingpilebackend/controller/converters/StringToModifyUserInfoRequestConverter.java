package com.lijiahao.chargingpilebackend.controller.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiahao.chargingpilebackend.controller.requestparam.ModifyUserInfoRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class StringToModifyUserInfoRequestConverter implements Converter<String, ModifyUserInfoRequest> {
    @Override
    public ModifyUserInfoRequest convert(String source) {
        ObjectMapper mapper = new ObjectMapper();
        ModifyUserInfoRequest request = null;
        try {
            request = mapper.readValue(source, ModifyUserInfoRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return request;
    }

}

