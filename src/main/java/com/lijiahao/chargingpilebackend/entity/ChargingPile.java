package com.lijiahao.chargingpilebackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author lijiahao
 * @since 2022-03-14
 */
@TableName("charging_pile")
@ApiModel(value = "ChargingPile对象", description = "")
@AllArgsConstructor
@Data
public class ChargingPile implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("电流类型")
    private String electricType;

    @ApiModelProperty("功率")
    private Float powerRate;

    @ApiModelProperty(" 这种充电桩的数量")
    private Integer pileNum;

    @ApiModelProperty("所属充电站")
    private Integer stationId;

    @ApiModelProperty("充电桩当前状态")
    private String state;

    public ChargingPile() {
    }

    public ChargingPile(String electricType, Float powerRate, Integer pileNum, Integer stationId) {
        this.electricType = electricType;
        this.powerRate = powerRate;
        this.pileNum = pileNum;
        this.stationId = stationId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getElectriType() {
        return electricType;
    }

    public void setElectriType(String electriType) {
        this.electricType = electriType;
    }

    public Float getPowerRate() {
        return powerRate;
    }

    public void setPowerRate(Float powerRate) {
        this.powerRate = powerRate;
    }

    public Integer getPileNum() {
        return pileNum;
    }

    public void setPileNum(Integer pileNum) {
        this.pileNum = pileNum;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public String getElectricType() {
        return electricType;
    }

    public void setElectricType(String electricType) {
        this.electricType = electricType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "ChargingPile{" +
                "id=" + id +
                ", electriType=" + electricType +
                ", powerRate=" + powerRate +
                ", pileNum=" + pileNum +
                ", stationId=" + stationId +
                ", state=" + state +
                "}";
    }
}
