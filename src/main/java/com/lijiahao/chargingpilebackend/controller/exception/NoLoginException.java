package com.lijiahao.chargingpilebackend.controller.exception;

public class NoLoginException extends Exception {
    @Override
    public String getMessage() {
        return "please login first, your token is null";
    }
}
