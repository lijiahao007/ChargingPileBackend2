package com.lijiahao.chargingpilebackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author lijiahao
 * @since 2022-03-21
 */
@ApiModel(value = "Appointment对象", description = "")
@NoArgsConstructor
public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String STATE_FINISH = "已完成";
    public static final String STATE_CANCEL = "已取消";
    public static final String STATE_WAITING = "待使用";
    public static final String STATE_OUT_DATE = "已过期";


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("预约开始时间")
    private LocalDateTime beginDateTime;

    @ApiModelProperty("预约结束时间")
    private LocalDateTime endDateTime;

    @ApiModelProperty("充电桩ID")
    private Integer pileId;

    @ApiModelProperty("预约用户")
    private Integer userId;

    @ApiModelProperty("充电站ID")
    private Integer stationId;

    @ApiModelProperty("预约状态")
    private String state;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(LocalDateTime beginTime) {
        this.beginDateTime = beginTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endTime) {
        this.endDateTime = endTime;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", beginTime=" + beginDateTime +
                ", endTime=" + endDateTime +
                ", pileId=" + pileId +
                ", userId=" + userId +
                ", stationId=" + stationId +
                ", state=" + state +
                "}";
    }
}
