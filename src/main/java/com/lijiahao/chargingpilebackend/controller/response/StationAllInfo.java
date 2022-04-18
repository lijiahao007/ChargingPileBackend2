package com.lijiahao.chargingpilebackend.controller.response;

import com.lijiahao.chargingpilebackend.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationAllInfo {
    List<ChargingPileStation> stations;
    Map<Integer, List<Tags>> tagMap;
    Map<Integer, List<ChargingPile>> pileMap;
    Map<Integer, List<OpenTime>> openTimeMap;
    Map<Integer, List<OpenDayInWeek>> openDayMap;
    Map<Integer, List<String>> picMap;
    Map<Integer, List<ElectricChargePeriod>> electricChargePeriodMap;
}
