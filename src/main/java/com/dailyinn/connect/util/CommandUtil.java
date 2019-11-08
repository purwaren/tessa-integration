package com.dailyinn.connect.util;

import com.dailyinn.connect.constant.TesaCommand;
import com.dailyinn.connect.dto.*;
import com.google.gson.Gson;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author purwa
 * @version 0.1
 */
public class CommandUtil {
    private static Logger logger = LoggerFactory.getLogger(CommandUtil.class);
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static byte[] calculateXor(byte[] arr) {
        int temp=0;
        for (byte ar : arr) {
            temp = temp ^ (int) ar;
        }
        byte[] calc = new byte[arr.length+1];
        System.arraycopy(arr, 0, calc, 0, arr.length);
        calc[calc.length-1] = (byte) temp;
        return calc;
    }

    /**
     * Present byte array as hex string
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static GeneralResponse decodeMessagePreCheckIn(PreCheckInRequest req, byte[] rawResp) {
        String rawMsgHex = bytesToHex(rawResp);
        String tmp[] = rawMsgHex.split(TesaCommand.STR_SEP);
        GeneralResponse resp = new GeneralResponse();
        resp.setRawMsg(new String(rawResp));
        resp.setRawMsgHex(rawMsgHex);
        resp.setRawMsgBase64(Base64.encodeBase64String(rawResp));
        if(rawResp.length > 1) {
            //check if message error or success
            if (req.getPcId().length() > 0 && tmp[1].equalsIgnoreCase(bytesToHex(req.getPcId().getBytes()))) {
                if (tmp[2].equalsIgnoreCase(bytesToHex(req.getCmd().getBytes()))) {
                    resp.setInfo("Success");
                } else resp.setInfo("Error");
            } else {
                if (tmp[1].equalsIgnoreCase(bytesToHex(req.getCmd().getBytes()))) {
                    resp.setInfo("Success");
                }
                else resp.setInfo("Error");
            }
        }
        else {
            resp.setInfo("Error");
        }
        return resp;
    }

    public static GeneralResponse decodeMessageCheckIn(CheckInRequest req, byte[] rawResp) {
        return decodeMessagePreCheckIn(req, rawResp);
    }

    public static ReadCardResponse decodeMessageReadCard(ReadCardRequest req, byte[] rawResp) throws DecoderException {
        String rawMsgHex = bytesToHex(rawResp);
        String tmp[] = rawMsgHex.split(TesaCommand.STR_SEP);

        System.out.println("tmp[] = " + tmp);

        ReadCardResponse resp = new ReadCardResponse();
        resp.setRawMsg(new String(rawResp));
        resp.setRawMsgHex(rawMsgHex);
        resp.setRawMsgBase64(Base64.encodeBase64String(rawResp));
        if(rawResp.length > 1) {
            //check if message error or success
            if (req.getPcId().length() > 0 && tmp[1].equalsIgnoreCase(bytesToHex(req.getPcId().getBytes()))) {
                if (tmp[2].equalsIgnoreCase(bytesToHex(req.getCmd().getBytes()))) {
                    resp.setInfo("Success with PC-ID");
                    resp.setDiagnostic(convertHexToString(tmp[3]));
                    resp.setUser(convertHexToString(tmp[4]));
                    resp.setActivationDate(convertHexToString(tmp[5]));
                    resp.setActivationTime(convertHexToString(tmp[6]));
                    resp.setExpiryDate(convertHexToString(tmp[7]));
                    resp.setExpiryTime(convertHexToString(tmp[8]));
                    resp.setGrant(convertHexToString(tmp[9]));
                    resp.setKeypad(convertHexToString(tmp[10]));
                } else resp.setInfo("Error");
            } else {
                if (tmp[1].equalsIgnoreCase(bytesToHex(req.getCmd().getBytes()))) {
                    resp.setInfo("Success withou PC-ID");
                    resp.setDiagnostic(convertHexToString(tmp[2]));
                    resp.setUser(convertHexToString(tmp[3]));
                    resp.setActivationDate(convertHexToString(tmp[3]));
                    resp.setActivationTime(convertHexToString(tmp[5]));
                    resp.setExpiryDate(convertHexToString(tmp[6]));
                    resp.setExpiryTime(convertHexToString(tmp[7]));
                    resp.setGrant(convertHexToString(tmp[8]));
                    resp.setKeypad(convertHexToString(tmp[9]));
                }
                else resp.setInfo("Error");
            }
        }
        else {
            resp.setInfo("Error");
        }

        return resp;
    }

    private static String[] convertHexArrayToStringArray(String[] hex) throws DecoderException {
        for (int i=0; i<hex.length; i++) {
            hex[i] = new String(Hex.decodeHex(hex[i].toCharArray()));
        }

        return hex;
    }

    private static String convertHexToString(String hex) throws DecoderException {
        return new String(Hex.decodeHex(hex.toCharArray()));
    }
}
