package com.lijiahao.chargingpilebackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author lijiahao
 * @since 2022-03-21
 */
@TableName("open_day_in_week")
@ApiModel(value = "OpenDayInWeek对象", description = "")
public class OpenDayInWeek implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("充电站运行在一个星期的哪几天")
    private String day;

    private Integer stationId;

    public OpenDayInWeek(String day, Integer stationId) {
        this.day = day;
        this.stationId = stationId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    @Override
    public String toString() {
        return "OpenDayInWeek{" +
        "id=" + id +
        ", day=" + day +
        ", stationId=" + stationId +
        "}";
    }
}
