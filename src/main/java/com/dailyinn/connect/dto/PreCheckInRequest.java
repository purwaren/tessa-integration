package com.dailyinn.connect.dto;

import com.dailyinn.connect.constant.TesaCommand;

/**
 * Created by purwa on 5/14/17.
 */
public class PreCheckInRequest extends GeneralRequest {
    private String room;
    private String activationDate;
    private String activationTime;
    private String expiryDate;
    private String expiryTime;
    private String grant="";
    private String keypad="";
    private String operator="";
    private String track1="";
    private String track2="";
    private String room2="";
    private String room3="";
    private String room4="";
    private String returnCardId="";
    private String cardId="";

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTrack1() {
        return track1;
    }

    public void setTrack1(String track1) {
        this.track1 = track1;
    }

    public String getTrack2() {
        return track2;
    }

    public void setTrack2(String track2) {
        this.track2 = track2;
    }

    public String getRoom2() {
        return room2;
    }

    public void setRoom2(String room2) {
        this.room2 = room2;
    }

    public String getRoom3() {
        return room3;
    }

    public void setRoom3(String room3) {
        this.room3 = room3;
    }

    public String getRoom4() {
        return room4;
    }

    public void setRoom4(String room4) {
        this.room4 = room4;
    }

    public String getReturnCardId() {
        return returnCardId;
    }

    public void setReturnCardId(String returnCardId) {
        this.returnCardId = returnCardId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    /**
     * STX¦[PC Id¦]PI[x]¦Room¦Activation date¦Act. Time¦Expiration date ¦Exp .time
     ¦Grants ¦Keypad ¦Card Operation¦Operator¦InHova Encoder¦Track1 ¦Track2
     ¦Technology ¦ Room2 ¦ Room3 ¦ Room4 ¦ Return CardID ¦ Card ID¦ ETX LRC
     */
    public byte[] buildCommand() {
        byte[] pc = this.getPcId().getBytes();
        byte[] cmd = this.getCmd().getBytes();
        byte[] cardOperation = this.getCardOperation().getBytes();
        byte[] tech = this.getTechnology().getBytes();
        byte[] enc = this.getEncoder().getBytes();
        byte[] room = this.getRoom().getBytes();
        byte[] actDate = this.getActivationDate().getBytes();
        byte[] actTime = this.getActivationTime().getBytes();
        byte[] expDate = this.getExpiryDate().getBytes();
        byte[] expTime = this.getExpiryTime().getBytes();
        byte[] grant = this.getGrant().getBytes();
        byte[] key = this.getKeypad().getBytes();
        byte[] op = this.getOperator().getBytes();
        byte[] track1 = this.getTrack1().getBytes();
        byte[] track2 = this.getTrack2().getBytes();
        byte[] room2 = this.getRoom2().getBytes();
        byte[] room3 = this.getRoom3().getBytes();
        byte[] room4 = this.getRoom4().getBytes();
        byte[] retCard = this.getReturnCardId().getBytes();
        byte[] card = this.getCardId().getBytes();

        //seprator + stx + etx =23
        int cmdLength = 22 + cmd.length + cardOperation.length + tech.length + enc.length + room.length + actDate.length
                + actTime.length + expDate.length + expTime.length + grant.length + key.length + op.length + track1.length
                + track2.length + room2.length + room3.length + room4.length + retCard.length + card.length;
        if(pc.length > 0)
            cmdLength += 1 + pc.length;
        byte[] command = new byte[cmdLength];
        int offset = 0;
        //STX
        command[offset] = TesaCommand.STX;
        offset++;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        if(pc.length > 0) {
            //PC_ID
            System.arraycopy(pc, 0, command, offset, pc.length);
            offset += pc.length;
            //SEP
            command[offset] = TesaCommand.SEP;
            offset++;
        }
        //CMD - PI
        System.arraycopy(cmd, 0, command, offset, cmd.length);
        offset += cmd.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //Room
        System.arraycopy(room, 0, command, offset, room.length);
        offset += room.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //Act date
        System.arraycopy(actDate, 0, command, offset, actDate.length);
        offset += actDate.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //Act time
        System.arraycopy(actTime, 0, command, offset, actTime.length);
        offset += actTime.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //Exp Date
        System.arraycopy(expDate, 0, command, offset, expDate.length);
        offset += expDate.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //Exp Time
        System.arraycopy(expTime, 0, command, offset, expTime.length);
        offset += expTime.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //Grant
        System.arraycopy(grant, 0, command, offset, grant.length);
        offset += grant.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //keypad
        System.arraycopy(key, 0, command, offset, key.length);
        offset += key.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //Card Operation
        System.arraycopy(cardOperation, 0, command, offset, cardOperation.length);
        offset += cardOperation.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //Operator
        System.arraycopy(op, 0, command, offset, op.length);
        offset += op.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //Encoder
        System.arraycopy(enc, 0, command, offset, enc.length);
        offset += enc.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //Track1
        System.arraycopy(track1, 0, command, offset, track1.length);
        offset += track1.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //Track2
        System.arraycopy(track2, 0, command, offset, track2.length);
        offset += track2.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //Technology
        System.arraycopy(tech, 0, command, offset, tech.length);
        offset += tech.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //Room2
        System.arraycopy(room2, 0, command, offset, room2.length);
        offset += room2.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //Room3
        System.arraycopy(room3, 0, command, offset, room3.length);
        offset += room3.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //Room4
        System.arraycopy(room4, 0, command, offset, room4.length);
        offset += room4.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //Return Card ID
        System.arraycopy(retCard, 0, command, offset, retCard.length);
        offset += retCard.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //Card ID
        System.arraycopy(card, 0, command, offset, card.length);
        offset += card.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //ETX
        command[offset] = TesaCommand.ETX;

        return command;
    }
}
