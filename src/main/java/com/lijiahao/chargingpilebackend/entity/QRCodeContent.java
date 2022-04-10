package com.lijiahao.chargingpilebackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QRCodeContent {
    // 该类是充电桩二维码中附带的消息
    String stationID;
    String pileId;

    // 获取content json数据
    public String getJson() {
        return "{\"stationID\":\"" + stationID + "\",\"pileId\":\"" + pileId + "\"}";
    }
}
