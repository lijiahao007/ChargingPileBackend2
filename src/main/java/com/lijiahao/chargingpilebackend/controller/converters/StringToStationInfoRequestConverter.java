package com.lijiahao.chargingpilebackend.controller.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiahao.chargingpilebackend.controller.requestparam.StationInfoRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class StringToStationInfoRequestConverter implements Converter<String, StationInfoRequest> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public StationInfoRequest convert(String source) {
        StationInfoRequest request = null;
        try {
            request =  mapper.readValue(source, StationInfoRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return request;
    }
}
