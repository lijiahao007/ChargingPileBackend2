package com.lijiahao.chargingpilebackend.controller.requestparam;

import com.lijiahao.chargingpilebackend.entity.ChargingPile;
import com.lijiahao.chargingpilebackend.entity.ChargingPileStation;
import com.lijiahao.chargingpilebackend.entity.ElectricChargePeriod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StationInfoRequest {
    ArrayList<String> openDayInWeek;
    ArrayList<String> openTime;
    ChargingPileStation station;
    ArrayList<ChargingPile> chargingPiles;
    ArrayList<ElectricChargePeriod> electricChargePeriods;
    String userId;
}
