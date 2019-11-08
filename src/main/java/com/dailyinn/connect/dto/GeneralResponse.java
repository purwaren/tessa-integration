package com.dailyinn.connect.dto;

/**
 * Created by purwa on 5/14/17.
 */
public class GeneralResponse {
    private String info;
    private String rawMsg;
    private String rawMsgHex;
    private String rawMsgBase64;
    private String userInternet;
    private String passwdInternet;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getRawMsg() {
        return rawMsg;
    }

    public void setRawMsg(String rawMsg) {
        this.rawMsg = rawMsg;
    }

    public String getRawMsgHex() {
        return rawMsgHex;
    }

    public void setRawMsgHex(String rawMsgHex) {
        this.rawMsgHex = rawMsgHex;
    }

    public String getRawMsgBase64() {
        return rawMsgBase64;
    }

    public void setRawMsgBase64(String rawMsgBase64) {
        this.rawMsgBase64 = rawMsgBase64;
    }

    public String getUserInternet() {
        return userInternet;
    }

    public void setUserInternet(String userInternet) {
        this.userInternet = userInternet;
    }

    public String getPasswdInternet() {
        return passwdInternet;
    }

    public void setPasswdInternet(String passwdInternet) {
        this.passwdInternet = passwdInternet;
    }
}
