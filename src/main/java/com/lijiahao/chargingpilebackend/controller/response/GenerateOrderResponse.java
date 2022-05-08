package com.lijiahao.chargingpilebackend.controller.response;

import com.lijiahao.chargingpilebackend.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateOrderResponse {
    public static final String SUCCESS="success";
    public static final String USING = "using";
    public static final String SUSPEND = "suspend";
    public static final String APPOINTMENT = "appointment";

    String code;
    Order order;
}
