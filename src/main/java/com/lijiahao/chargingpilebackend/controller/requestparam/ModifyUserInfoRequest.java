package com.lijiahao.chargingpilebackend.controller.requestparam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyUserInfoRequest {
    String name;
    String remark;
    int userId;
}
