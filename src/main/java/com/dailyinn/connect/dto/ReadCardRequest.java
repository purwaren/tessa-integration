package com.dailyinn.connect.dto;

import com.dailyinn.connect.constant.TesaCommand;

/**
 * Created by purwa on 5/14/17.
 */
public class ReadCardRequest extends GeneralRequest {
    private String message = "";
    private String format;
    private String track;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    /**
     * Build RC (Read Card) Command
     * STX¦[PCId]¦RC¦Technology¦Format¦Track¦Card Operation¦TESA INHOVA Encoder¦Message¦ETX LRC
     * @return byte array of the command
     */
    public byte[] buildCommand() {
        byte[] stx = new byte[] {TesaCommand.STX};
        byte[] etx = new byte[] {TesaCommand.ETX};
        byte[] pc = this.getPcId().getBytes();
        byte[] rc = this.getCmd().getBytes();
        byte[] tech = this.getTechnology().getBytes();
        byte[] format = this.getFormat().getBytes();
        byte[] track = this.getTrack().getBytes();
        byte[] cardOperation = this.getCardOperation().getBytes();
        byte[] encoder = this.getEncoder().getBytes();
        byte[] msg = this.getMessage().getBytes();

        int cmdLength = 8 + stx.length + etx.length + rc.length + tech.length + format.length
                + track.length + cardOperation.length + encoder.length + msg.length;
        if(pc.length > 0)
            cmdLength += 1 + pc.length;

        byte[] command = new byte[cmdLength];
        int offset = 0;

        //STX
        System.arraycopy(stx, 0, command, offset, stx.length);
        offset += stx.length;
        if(pc.length > 0) {
            //SEP
            command[offset] = TesaCommand.SEP;
            offset++;
            //PC_ID
            System.arraycopy(pc, 0, command, offset, pc.length);
            offset += pc.length;
        }
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //RC
        System.arraycopy(rc, 0, command, offset, rc.length);
        offset += rc.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //TECH
        System.arraycopy(tech, 0, command, offset, tech.length);
        offset += tech.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //FORMAT
        System.arraycopy(format, 0, command, offset, format.length);
        offset += format.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //TRACK
        System.arraycopy(track, 0, command, offset, track.length);
        offset += track.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //CARD OPERATION
        System.arraycopy(cardOperation, 0, command, offset, cardOperation.length);
        offset += cardOperation.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //ENCODER
        System.arraycopy(encoder, 0, command, offset, encoder.length);
        offset += encoder.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //MESSAGE
        System.arraycopy(msg, 0, command, offset, msg.length);
        offset +=  msg.length;
        //SEP
        command[offset] = TesaCommand.SEP;
        offset++;
        //ETX
        System.arraycopy(etx, 0, command, offset, etx.length);

        return command;
    }
}
