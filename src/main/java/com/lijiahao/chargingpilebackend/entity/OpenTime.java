package com.lijiahao.chargingpilebackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author lijiahao
 * @since 2022-03-14
 */
@TableName("open_time")
@ApiModel(value = "OpenTime对象", description = "")
@AllArgsConstructor
@NoArgsConstructor
public class OpenTime implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("营业开始时间	")
    private LocalTime beginTime;

    @ApiModelProperty("营业结束时间")
    private LocalTime endTime;

    @ApiModelProperty("每度电收费")
    private Float electricCharge;

    private Integer stationId;


    public OpenTime(LocalTime beginTime, LocalTime endTime, Float electricCharge, Integer stationId) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.electricCharge = electricCharge;
        this.stationId = stationId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Float getElectricCharge() {
        return electricCharge;
    }

    public void setElectricCharge(Float electricCharge) {
        this.electricCharge = electricCharge;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    @Override
    public String toString() {
        return "OpenTime{" +
        "id=" + id +
        ", beginTime=" + beginTime +
        ", endTime=" + endTime +
        ", electricCharge=" + electricCharge +
        ", stationId=" + stationId +
        "}";
    }
}
