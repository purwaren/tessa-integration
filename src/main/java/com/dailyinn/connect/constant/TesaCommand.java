package com.dailyinn.connect.constant;

/**
 * @author purwa
 * @version 0.1
 */

public class TesaCommand {
    public final static byte STX = (byte) 0x02;
    public final static byte ETX = (byte) 0x03;
    public final static byte SEP = (byte) 0xB3;
    public final static byte ACK = (byte) 0x06;
    public final static byte NAK = (byte) 0x15;
    public final static byte ENQ = (byte) 0x05;
    public final static byte LRC = (byte) 0x13;
    public final static byte ERR = (byte) 0xE2;
    public final static String STR_SEP = "B3";

    public final static String READ_CARD = "RC";
    public final static String CHECK_IN = "CI";
    public final static String PRE_CHECK_IN = "PI";

    public final static byte[] ECHO_TEST = new byte[] {ENQ};
}
