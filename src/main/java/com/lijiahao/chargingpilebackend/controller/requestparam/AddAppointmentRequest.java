package com.lijiahao.chargingpilebackend.controller.requestparam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddAppointmentRequest {
    public Integer id;
    public String date;
    public String beginTime;
    public String endTime;
    public Integer pileId;
    public Integer userId;
    public Integer stationId;
    public String state;
}
