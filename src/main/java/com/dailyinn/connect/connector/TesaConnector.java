package com.dailyinn.connect.connector;

import com.dailyinn.connect.constant.TesaCommand;
import com.dailyinn.connect.util.CommandUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

/**
 * Created by purwa on 5/13/17.
 */
public class TesaConnector implements Runnable{

    Logger logger = LoggerFactory.getLogger(getClass());

    private String host;
    private int port;
    private int timeout;
    private Socket socket;
    public boolean isReady = false;


    public TesaConnector(String host, int port, int timeout) throws IOException {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    /**
     * Connect to TESA InHova server
     * @throws IOException
     * @throws InterruptedException
     */
    public void connect() throws IOException, InterruptedException {
        socket = new Socket(this.host, this.port);
        socket.setSoTimeout(this.timeout);
        socket.setKeepAlive(true);
        socket.setTcpNoDelay(true);

        if(socket.isConnected()) {
            logger.info("Connection established to "+this.host+":"+this.port);
            isReady = true;
        }
        else {
            logger.info("Can not connect to "+this.host+":"+this.port);
            isReady = false;
        }
    }

    @Override
    public void run() {
        try {
            connect();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            isReady = false;
        }
    }

    /**
     * Send command to TESA server
     * @param command Command to be sent, check docs
     * @return byte array response from TESA server
     * @throws IOException
     * @throws InterruptedException
     */
    public byte[] sendCommand(byte[] command) throws IOException, InterruptedException {
        logger.info("Sending Command");
        byte[] calcCmd = CommandUtil.calculateXor(command);
        logger.debug("Command (HEX): "+CommandUtil.bytesToHex(calcCmd));
        logger.debug("Command (String): "+new String(calcCmd));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        OutputStream os = socket.getOutputStream();
        InputStream is = socket.getInputStream();
        os.write(calcCmd);
        os.flush();
        logger.debug("Finished writing to socket, now we read the response");
        int data;
        int lrc;
        while (true) {
            data = is.read();
            logger.debug("fragment read = "+data);
            byte check = (byte) data;
            if(data == -1) {
                logger.info("Channel disconnected");
                buffer.write("Disconnected".getBytes());
                connect();
                break;
            }
            else if(check == TesaCommand.ERR) {
                buffer.write(data);
                logger.info("Received ERROR");
                break;
            }
            else if(check == TesaCommand.NAK) {
                buffer.write(data);
                logger.info("Received NAK");
                break;
            }
            else if(check == TesaCommand.ETX) {
                buffer.write(data);
                logger.info("Received ETX");
                lrc = is.read();
                buffer.write(lrc);
                break;
            }
            else if(check == TesaCommand.ACK) {
                logger.info("Received ACK, continue reading response");
            }
            else buffer.write(data);

            Thread.sleep(5);
        }
        buffer.flush();
        return buffer.toByteArray();
    }

    public void sendEchoTest() throws IOException, InterruptedException {
        logger.debug("Sending ENQ");
        OutputStream os = socket.getOutputStream();
        os.write(TesaCommand.ECHO_TEST);
        os.flush();

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        InputStream is = socket.getInputStream();
        int data;
        while(true) {
            data = is.read();
            logger.debug("fragment read = "+data);
            byte check = (byte) data;
            if(check == TesaCommand.ACK) {
                buffer.write(data);
                logger.info("Received ACK");
                break;
            }
            else if(check == TesaCommand.NAK) {
                buffer.write(data);
                logger.info("Received NAK");
                break;
            }
        }
    }
}
