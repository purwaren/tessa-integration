package com.dailyinn.connect.dto;

/**
 * @author purwa
 */

public class GeneralRequest {
    private String pcId = "";
    private String cmd;
    private String technology = "P";
    private String cardOperation = "EF";
    private String encoder = "1";

    public String getPcId() {
        return pcId;
    }

    public void setPcId(String pcId) {
        this.pcId = pcId;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getCardOperation() {
        return cardOperation;
    }

    public void setCardOperation(String cardOperation) {
        this.cardOperation = cardOperation;
    }

    public String getEncoder() {
        return encoder;
    }

    public void setEncoder(String encoder) {
        this.encoder = encoder;
    }

    public boolean validateParent() {
        if(cmd.length() > 0 && technology.length() > 0 && cardOperation.length() > 0 && encoder.length() > 0)
            return true;
        else return false;
    }
}
