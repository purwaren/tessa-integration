package com.dailyinn.connect.dto;

public class CoovaUpdatePasswordRequest {
    private String room;
    private String password;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
