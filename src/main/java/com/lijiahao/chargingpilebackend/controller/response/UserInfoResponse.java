package com.lijiahao.chargingpilebackend.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    String userId;
    String phone;
    String name;
    String avatarUrl;
    Map<String, String> extend; // 拓展信息
    String remark;
}
