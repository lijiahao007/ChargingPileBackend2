package com.lijiahao.chargingpilebackend.controller.exception;

public class TokenUnavailableException extends  Exception{
    @Override
    public String getMessage() {
        return "token已过期，请重新登录";
    }
}
