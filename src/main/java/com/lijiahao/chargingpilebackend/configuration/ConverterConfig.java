package com.lijiahao.chargingpilebackend.configuration;

import com.lijiahao.chargingpilebackend.controller.converters.*;
import com.lijiahao.chargingpilebackend.controller.interceptor.JwtAuthenticationInterceptor;
import com.lijiahao.chargingpilebackend.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;

@Configuration
public class ConverterConfig implements WebMvcConfigurer {
    @Autowired
    StringToMessageRequestConverter messageRequestConverter;

    @Autowired
    StringToModifyUserInfoRequestConverter modifyUserInfoRequestConverter;

    @Autowired
    StringToStationInfoRequestConverter stationInfoRequestConverter;

    @Autowired
    UserServiceImpl userService;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        WebMvcConfigurer.super.addFormatters(registry);
        registry.addConverter(messageRequestConverter);
        registry.addConverter(modifyUserInfoRequestConverter);
        registry.addConverter(modifyUserInfoRequestConverter);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtAuthenticationInterceptor(userService))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/swagger-ui/*",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/v2/**",
                        "/v3/**",
                        "/swagger-ui.html"); // 跳过swagger
    }

    @Bean
    public JwtAuthenticationInterceptor authenticationInterceptor() {
        return new JwtAuthenticationInterceptor(userService);
    }
}
