package com.lijiahao.chargingpilebackend.controller.exception;

public class NoLoginException extends Exception {
    @Override
    public String getMessage() {
        return "您还没有登录，请先登录";
    }
}
