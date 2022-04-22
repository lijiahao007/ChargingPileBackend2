package com.lijiahao.chargingpilebackend.controller.response;

import com.lijiahao.chargingpilebackend.entity.ChargingPileStation;
import com.lijiahao.chargingpilebackend.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryOrderResponse {
    List<Order> processingOrder;
    List<Order> finishOrder;
    Map<Integer, Map<Integer, List<Order>>> serviceOrder;
    Map<Integer, ChargingPileStation> stationInfoMap;
    Map<Integer, Integer> pileStationMap;
}
