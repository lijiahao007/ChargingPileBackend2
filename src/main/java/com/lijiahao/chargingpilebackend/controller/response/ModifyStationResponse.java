package com.lijiahao.chargingpilebackend.controller.response;

import com.lijiahao.chargingpilebackend.entity.ChargingPile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyStationResponse {
    List<ChargingPile> curChargingPiles;
}
