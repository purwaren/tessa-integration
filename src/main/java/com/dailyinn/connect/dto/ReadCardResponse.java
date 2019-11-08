package com.dailyinn.connect.dto;

/**
 * Created by purwa on 5/16/17.
 */
public class ReadCardResponse extends GeneralResponse {
    private String diagnostic;
    private String user;
    private String activationDate;
    private String activationTime;
    private String expiryDate;
    private String expiryTime;
    private String grant;
    private String keypad;

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(String activationDate) {
        this.activationDate = activationDate;
    }

    public String getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(String activationTime) {
        this.activationTime = activationTime;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(String expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getGrant() {
        return grant;
    }

    public void setGrant(String grant) {
        this.grant = grant;
    }

    public String getKeypad() {
        return keypad;
    }

    public void setKeypad(String keypad) {
        this.keypad = keypad;
    }
}
