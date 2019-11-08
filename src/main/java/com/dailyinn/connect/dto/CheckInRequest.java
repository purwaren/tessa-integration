package com.dailyinn.connect.dto;

/**
 * Created by purwa on 5/14/17.
 */
public class CheckInRequest extends PreCheckInRequest {
    private String fullName;
    public CheckInRequest(){
    }

    public String getFullName() {
        return fullName == null ? "dailyinn" : fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
