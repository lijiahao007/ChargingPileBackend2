package com.lijiahao.chargingpilebackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@ApiModel(value = "User对象", description = "")
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("电话、登录使用")
    private String phone;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("用户名字")
    private String name;

    @ApiModelProperty("头像本地URI")
    private String avatarUrl;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("用户信息更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("用户个性签名")
    private String remark;

    public User(Integer id, String phone, String name, String avatarUrl) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    public User(String phone, String name, String avatarUrl) {
        // 这个构造方法勇于用户注册
        this.phone = phone;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.id = 0;
    }

    public User(String phone, String name) {
        // 这个构造方法用于没有头像的用户注册
        this.phone = phone;
        this.name = name;
        this.avatarUrl = null;
        this.id = 0;
    }

    public User(String phone, String password, String name, String avatarUrl) {
        this.phone = phone;
        this.password = password;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", phone=" + phone +
                ", password=" + password +
                ", name=" + name +
                ", avatarUrl=" + avatarUrl +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
