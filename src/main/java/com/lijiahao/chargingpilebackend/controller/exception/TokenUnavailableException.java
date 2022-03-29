package com.lijiahao.chargingpilebackend.controller.exception;

public class TokenUnavailableException extends  Exception{
    @Override
    public String getMessage() {
        return "token is wrong, please login again";
    }
}
