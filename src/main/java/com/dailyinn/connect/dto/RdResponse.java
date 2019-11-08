package com.dailyinn.connect.dto;

public class RdResponse {
    private RdBaseData data;
    private boolean success;

    public RdBaseData getData() {
        return data;
    }

    public void setData(RdBaseData data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
