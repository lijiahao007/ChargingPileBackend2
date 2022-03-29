package com.lijiahao.chargingpilebackend.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponse {
    String userId;
    String code;
}
