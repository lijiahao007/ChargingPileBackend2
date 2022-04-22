package com.lijiahao.chargingpilebackend.controller.requestparam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    public String text;
    public String star;
    public String userId;
    public String stationId;
    public String pileId;
}
