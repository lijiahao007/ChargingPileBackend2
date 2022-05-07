package com.lijiahao.chargingpilebackend.controller.response;

import com.lijiahao.chargingpilebackend.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationInfo {
    // 单个充电站的信息
    ChargingPileStation station;
    List<Tags> tagList;
    List<ChargingPile> pileList;
    List<OpenTime> openTimeList;
    List<OpenDayInWeek> openDayList;
    List<String> picList;
    List<ElectricChargePeriod> chargePeriodList;
    List<Appointment> appointmentList;
}
