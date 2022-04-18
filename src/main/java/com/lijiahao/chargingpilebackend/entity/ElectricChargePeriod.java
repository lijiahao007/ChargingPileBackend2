package com.lijiahao.chargingpilebackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
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
 * @since 2022-04-15
 */
@TableName("electric_charge_period")
@ApiModel(value = "ElectricChargePeriod对象", description = "")
@AllArgsConstructor
@NoArgsConstructor
public class ElectricChargePeriod implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("时间段开始时间")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime beginTime;

    @ApiModelProperty("时间段结束时间")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime endTime;

    @ApiModelProperty("充电站ID")
    private Integer stationId;

    @ApiModelProperty("该时间段内电费，n元/度")
    private Double electricCharge;


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

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public Double getElectricCharge() {
        return electricCharge;
    }

    public void setElectricCharge(Double electricCharge) {
        this.electricCharge = electricCharge;
    }

    @Override
    public String toString() {
        return "ElectricChargePeriod{" +
        "id=" + id +
        ", beginTime=" + beginTime +
        ", endTime=" + endTime +
        ", stationId=" + stationId +
        ", electricCharge=" + electricCharge +
        "}";
    }
}
