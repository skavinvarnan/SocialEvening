package com.kavin.socialevening.server.dto;

/**
 * Copyright 2015 (C) Virtual Applets
 * Created on : 01/11/15
 * Author     : Kavin Varnan
 */
public class PushDto {
    private String message;
    private int pushType;


    public int getPushType() {
        return pushType;
    }

    public void setPushType(int pushType) {
        this.pushType = pushType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
