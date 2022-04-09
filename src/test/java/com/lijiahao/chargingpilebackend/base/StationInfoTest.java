package com.lijiahao.chargingpilebackend.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiahao.chargingpilebackend.controller.requestparam.StationInfoRequest;
import com.lijiahao.chargingpilebackend.entity.ChargingPile;
import com.lijiahao.chargingpilebackend.entity.ChargingPileStation;
import com.lijiahao.chargingpilebackend.entity.OpenDayInWeek;

import java.util.ArrayList;

public class StationInfoTest {
    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StationInfoRequest request = new StationInfoRequest();
        request.setStation(new ChargingPileStation());
        request.setChargingPiles(new ArrayList<ChargingPile>() {{
            add(new ChargingPile());
        }});
        request.setOpenDayInWeek(new ArrayList<String>() {{
            add("周一");
            add("周二");
        }});
        request.setUserId("1");
        request.setOpenTime(new ArrayList<String>() {{
            add("09:00");
            add("18:00");
        }});

        request.setOpenTimeCharge(new ArrayList<Float>() {{
            add(1.35f);
            add(3.22f);
        }});

        String res = mapper.writeValueAsString(request);
        System.out.println(res  );
    }
}
