package com.lijiahao.chargingpilebackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.lijiahao.chargingpilebackend.controller.requestparam.MessageRequest;
import com.lijiahao.chargingpilebackend.utils.TimeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author lijiahao
 * @since 2022-03-24
 */
@ApiModel(value = "Message对象", description = "")
@NoArgsConstructor
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("消息唯一标识")
    private String uuid;

    @ApiModelProperty("创建时间")
    private LocalDateTime createtime;

    @ApiModelProperty("消息类型")
    private String type;

    @ApiModelProperty("发送者ID")
    private Integer sendUserId;

    @ApiModelProperty("接收者ID")
    private Integer targetUserId;

    @ApiModelProperty("文本信息")
    private String text;

    @ApiModelProperty("消息状态")
    private String state;


    public Message(String uuid, LocalDateTime createtime, String type, Integer sendUserId, Integer targetUserId, String text, String state) {
        this.uuid = uuid;
        this.createtime = createtime;
        this.type = type;
        this.sendUserId = sendUserId;
        this.targetUserId = targetUserId;
        this.text = text;
        this.state = state;
    }


    public MessageRequest toMessageRequest() {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setUuid(this.uuid);
        messageRequest.setType(this.type);
        messageRequest.setSendUserId(String.valueOf(this.sendUserId));
        messageRequest.setTargetUserId(String.valueOf(this.targetUserId));
        messageRequest.setText(this.text);
        messageRequest.setTimeStamp(TimeUtils.localDateTimeToLong(this.createtime));
        return messageRequest;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getCreatetime() {
        return createtime;
    }

    public void setCreatetime(LocalDateTime createtime) {
        this.createtime = createtime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(Integer sendUserId) {
        this.sendUserId = sendUserId;
    }

    public Integer getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(Integer targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", createtime=" + createtime +
                ", type=" + type +
                ", sendUserId=" + sendUserId +
                ", targetUserId=" + targetUserId +
                ", text=" + text +
                ", state=" + state +
                "}";
    }
}
