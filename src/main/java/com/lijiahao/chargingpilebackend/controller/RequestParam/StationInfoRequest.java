package com.lijiahao.chargingpilebackend.controller.RequestParam;

import com.lijiahao.chargingpilebackend.entity.ChargingPile;
import com.lijiahao.chargingpilebackend.entity.ChargingPileStation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StationInfoRequest {
    ArrayList<String> openDayInWeek;
    ArrayList<String> openTime;
    ArrayList<Float> openTimeCharge;
    ChargingPileStation station;
    ArrayList<ChargingPile> chargingPiles;
    String userId;
}
