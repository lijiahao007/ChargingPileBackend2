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
 * @since 2022-03-14
 */
@TableName("station_pic")
@ApiModel(value = "StationPic对象", description = "")
public class StationPic implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("充电站图片")
    private String url;

    private Integer stationId;

    public StationPic() {
    }

    public StationPic(String url, Integer stationId) {
        this.url = url;
        this.stationId = stationId;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    @Override
    public String toString() {
        return "StationPic{" +
        "id=" + id +
        ", url=" + url +
        ", stationId=" + stationId +
        "}";
    }
}
