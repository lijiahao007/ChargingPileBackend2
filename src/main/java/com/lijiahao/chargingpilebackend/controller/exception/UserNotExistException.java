package com.lijiahao.chargingpilebackend.controller.exception;

public class UserNotExistException extends Exception {
    @Override
    public String getMessage() {
        return "the token user is not exists, please sign up ";
    }
}
