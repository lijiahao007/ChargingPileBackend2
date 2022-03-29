package com.lijiahao.chargingpilebackend.controller.exception;

public class UserNotExistException extends Exception {
    @Override
    public String getMessage() {
        return "用户不存在";
    }
}
