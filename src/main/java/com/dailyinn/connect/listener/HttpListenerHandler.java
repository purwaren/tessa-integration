package com.dailyinn.connect.listener;

import com.dailyinn.connect.connector.RadiusDeskConnector;
import com.dailyinn.connect.connector.TesaConnector;
import com.dailyinn.connect.constant.TesaCommand;
import com.dailyinn.connect.dto.*;
import com.dailyinn.connect.util.CommandUtil;
import com.google.gson.Gson;
import org.apache.commons.codec.DecoderException;
import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.nio.protocol.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketException;

/**
 * @author purwa
 * @version 0.1
 */
public class HttpListenerHandler implements HttpAsyncRequestHandler<HttpRequest> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private TesaConnector connector;
    private RadiusDeskConnector rd;

    public HttpListenerHandler(TesaConnector connector, RadiusDeskConnector rd) throws IOException, InterruptedException {
        this.connector = connector;
        this.rd = rd;
        int i = 0;
        while (!connector.isReady) {
            i++;
            logger.debug("waiting connection...");
            Thread.sleep(1000);
            if(i == 30)
                break;
        }
        if(connector.isReady)
            this.connector.sendEchoTest();
        else
            logger.debug("TESA connector is not ready");
    }

    @Override
    public HttpAsyncRequestConsumer<HttpRequest> processRequest(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        return new BasicAsyncRequestConsumer();
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpAsyncExchange httpAsyncExchange, HttpContext httpContext) throws HttpException, IOException {
        HttpResponse response = httpAsyncExchange.getResponse();
        try {
            processCommand(httpRequest, response, httpContext);
            httpAsyncExchange.submitResponse(new BasicAsyncResponseProducer(response));

        } catch (SocketException e) {
            e.printStackTrace();
            try {
                this.connector.connect();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            response.setStatusCode(400);
            response.setReasonPhrase(e.getMessage());
            httpAsyncExchange.submitResponse(new BasicAsyncResponseProducer(response));
        }
    }

    private void processCommand(HttpRequest request, HttpResponse response, HttpContext context) throws IOException, InterruptedException, DecoderException {
        //reading & parsing http request body
        HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
        String reqBody = EntityUtils.toString(entity);
        logger.debug("reqBody = "+reqBody);
        GeneralRequest req = new Gson().fromJson(reqBody, GeneralRequest.class);
        GeneralResponse respData = new GeneralResponse();
        NStringEntity stringEntity = null;
        byte[] resp;
        if(req.getCmd().equalsIgnoreCase(TesaCommand.READ_CARD)) {
            ReadCardRequest readCardRequest = new Gson().fromJson(reqBody, ReadCardRequest.class);
            resp = processReadCard(readCardRequest);
            logger.info("Response From TESA (Hex): "+ CommandUtil.bytesToHex(resp));
            ReadCardResponse readCardResponse = CommandUtil.decodeMessageReadCard(readCardRequest, resp);
            stringEntity = new NStringEntity(new Gson().toJson(readCardResponse), ContentType.create("application/json", "utf-8"));
        }
        else if(req.getCmd().equalsIgnoreCase(TesaCommand.PRE_CHECK_IN)) {
            PreCheckInRequest preCheckInRequest = new Gson().fromJson(reqBody, PreCheckInRequest.class);
            resp = processPreCheckIn(preCheckInRequest);
            logger.info("Response From TESA (Hex): "+ CommandUtil.bytesToHex(resp));
            respData = CommandUtil.decodeMessagePreCheckIn(preCheckInRequest, resp);
            stringEntity = new NStringEntity(new Gson().toJson(respData), ContentType.create("application/json", "utf-8"));
        }
        else if(req.getCmd().equalsIgnoreCase(TesaCommand.CHECK_IN)) {
            CheckInRequest checkInRequest = new Gson().fromJson(reqBody, CheckInRequest.class);
            resp = processCheckIn(checkInRequest);
            logger.info("Response From TESA (Hex): "+ CommandUtil.bytesToHex(resp));
            respData = CommandUtil.decodeMessageCheckIn(checkInRequest, resp);

            //create voucher
            if(respData.getInfo().equalsIgnoreCase("Success")) {
                String[] tmp = checkInRequest.getFullName().split(" ");
                rd.updatePassword(checkInRequest.getRoom(), tmp[0]);
                respData.setUserInternet(checkInRequest.getRoom());
                respData.setPasswdInternet(tmp[0]);
            }

            stringEntity = new NStringEntity(new Gson().toJson(respData), ContentType.create("application/json", "utf-8"));
        }
        else {
            respData.setInfo("Unknown command");
            stringEntity = new NStringEntity(new Gson().toJson(respData), ContentType.create("application/json", "utf-8"));
        }


        response.setEntity(stringEntity);
        response.setStatusCode(HttpStatus.SC_OK);

        String respBody = EntityUtils.toString(stringEntity);
        logger.debug("respBody = "+respBody);
    }

    private byte[] processReadCard(ReadCardRequest data) throws IOException, InterruptedException {
        return this.connector.sendCommand(data.buildCommand());
    }

    private byte[] processPreCheckIn(PreCheckInRequest data) throws IOException, InterruptedException {
        return this.connector.sendCommand(data.buildCommand());
    }

    private byte[] processCheckIn(CheckInRequest data) throws IOException, InterruptedException {
        return this.connector.sendCommand(data.buildCommand());
    }
}
