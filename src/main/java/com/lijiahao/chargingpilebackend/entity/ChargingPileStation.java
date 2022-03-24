package com.lijiahao.chargingpilebackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("charging_pile_station")
@ApiModel(value = "ChargingPileStation对象", description = "")
@Data
@AllArgsConstructor
public class ChargingPileStation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("经度")
    private Double longitude;

    @ApiModelProperty("纬度	")
    private Double latitude;

    @ApiModelProperty("充电桩名字")
    private String name;

    @ApiModelProperty("充电桩详细位置")
    private String posDescription;

    @ApiModelProperty("停车费， -1表示未设置")
    private Float parkFee;

    @ApiModelProperty("收藏数量")
    private Integer collection;

    @ApiModelProperty("所属用户ID	")
    private Integer userId;

    @ApiModelProperty("充电桩创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("充电桩信息更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("充电站备注信息")
    private String remark;

    public ChargingPileStation(Double longitude, Double latitude, String name, String posDescription, Float parkFee, Integer collection, Integer userId) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.posDescription = posDescription;
        this.parkFee = parkFee;
        this.collection = collection;
        this.userId = userId;
    }

    public ChargingPileStation(Double longitude, Double latitude, String name, String posDescription, Float parkFee, Integer collection, String remark) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.posDescription = posDescription;
        this.parkFee = parkFee;
        this.collection = collection;
        this.remark = remark;
    }

    public ChargingPileStation() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosDescription() {
        return posDescription;
    }

    public void setPosDescription(String posDescription) {
        this.posDescription = posDescription;
    }

    public Float getParkFee() {
        return parkFee;
    }

    public void setParkFee(Float parkFee) {
        this.parkFee = parkFee;
    }

    public Integer getCollection() {
        return collection;
    }

    public void setCollection(Integer collection) {
        this.collection = collection;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ChargingPileStation{" +
                "id=" + id +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", name=" + name +
                ", posDescription=" + posDescription +
                ", parkFee=" + parkFee +
                ", collection=" + collection +
                ", userId=" + userId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", remarks" + remark +
                "}";
    }
}
