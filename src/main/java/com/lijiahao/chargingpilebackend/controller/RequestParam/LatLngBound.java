package com.lijiahao.chargingpilebackend.controller.RequestParam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LatLngBound {
    Double latitude_northeast;
    Double longitude_northeast;
    Double latitude_southwest;
    Double longitude_southwest;
}
