package com.lijiahao.chargingpilebackend.controller.requestparam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    String uuid;
    String type;
    String sendUserId;
    String targetUserId;
    String text;
    Long timeStamp;
}
