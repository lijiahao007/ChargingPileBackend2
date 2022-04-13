package com.lijiahao.chargingpilebackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author lijiahao
 * @since 2022-03-14
 */
@ApiModel(value = "Order对象", description = "")
@NoArgsConstructor
@TableName(value = "`order`")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String STATE_USING = "待完成";
    public static final String STATE_FINISH = "已完成";
    public static final String STATE_CANCEL = "已取消";


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("订单完成时间")
    private LocalDateTime completeTime;

    @ApiModelProperty("订单修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("开始充电时间")
    private LocalDateTime beginChargeTime;

    @ApiModelProperty("订单状态")
    private String state;

    @ApiModelProperty("订单价格")
    private Float price;

    @ApiModelProperty("充电站ID")
    private Integer pileId;

    @ApiModelProperty("订单用户")
    private Integer userId;

    @ApiModelProperty("订单UUID ，订单编号")
    private String uuid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(LocalDateTime completeTime) {
        this.completeTime = completeTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getPileId() {
        return pileId;
    }

    public void setPileId(Integer pileId) {
        this.pileId = pileId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getBeginChargeTime() {
        return beginChargeTime;
    }

    public void setBeginChargeTime(LocalDateTime beginChargeTime) {
        this.beginChargeTime = beginChargeTime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", completeTime=" + completeTime +
                ", updateTime=" + updateTime +
                ", beginChargeTime=" + beginChargeTime +
                ", state=" + state +
                ", price=" + price +
                ", stationId=" + pileId +
                ", userId=" + userId +
                ", uuid=" + uuid +
                "}";
    }
}
