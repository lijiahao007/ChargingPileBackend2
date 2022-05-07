package com.lijiahao.chargingpilebackend.controller.requestparam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyAppointmentRequest {
    public int id;
    public String beginDateTime;
    public String endDateTime;
}
