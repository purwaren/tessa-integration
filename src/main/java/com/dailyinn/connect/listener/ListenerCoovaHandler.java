package com.dailyinn.connect.listener;

import com.dailyinn.connect.connector.RadiusDeskConnector;
import com.dailyinn.connect.dto.CoovaDeactivateUserRequest;
import com.dailyinn.connect.dto.CoovaUpdatePasswordRequest;
import com.google.gson.Gson;
import org.apache.http.*;
import org.apache.http.nio.protocol.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketException;

public class ListenerCoovaHandler implements HttpAsyncRequestHandler<HttpRequest> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private RadiusDeskConnector rd;

    public ListenerCoovaHandler(RadiusDeskConnector rd) {
        this.rd = rd;
    }

    @Override
    public HttpAsyncRequestConsumer<HttpRequest> processRequest(HttpRequest httpRequest, HttpContext httpContext) {
        return new BasicAsyncRequestConsumer();
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpAsyncExchange httpAsyncExchange, HttpContext httpContext) throws IOException {
        HttpResponse response = httpAsyncExchange.getResponse();
        if (httpRequest.containsHeader("Authorization")) {
            if ("POST".equalsIgnoreCase(httpRequest.getRequestLine().getMethod())) {
                processCoovaRequest(httpRequest, response, httpContext);
            } else {
                response.setStatusCode(405);
                response.setReasonPhrase("Method not allowed");
            }
        }
        else {
            response.setStatusCode(401);
            response.setReasonPhrase("Unauthorized");
        }
        httpAsyncExchange.submitResponse(new BasicAsyncResponseProducer(response));
    }

    private void processCoovaRequest(HttpRequest request, HttpResponse response, HttpContext context) throws IOException {

        HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
        String reqBody = EntityUtils.toString(entity);

        String uri = request.getRequestLine().getUri();
        if (uri.equals("/hotspot/create")) {
            CoovaUpdatePasswordRequest passwordRequest = new Gson().fromJson(reqBody, CoovaUpdatePasswordRequest.class);
            logger.debug("create hotspot user");
            if (updateUserPassword(passwordRequest) > 0) {
                response.setStatusCode(200);
                response.setReasonPhrase("OK");
            }
            else {
                response.setStatusCode(400);
                response.setReasonPhrase("Failed to create user");
            }
        }
        else if (uri.equals("/hotspot/delete")) {
            CoovaDeactivateUserRequest deactivateUserRequest = new Gson().fromJson(reqBody, CoovaDeactivateUserRequest.class);
            logger.debug("delete hotspot user ");
            if (deactivateUser(deactivateUserRequest) > 0) {
                response.setStatusCode(200);
                response.setReasonPhrase("OK");
            }
            else {
                response.setStatusCode(400);
                response.setReasonPhrase("Failed to deactivate user");
            }
        }
        else {
            response.setStatusCode(404);
            response.setReasonPhrase("Not Found");
        }
    }

    private int updateUserPassword(CoovaUpdatePasswordRequest request) {
        return rd.updatePassword(request.getRoom(), request.getPassword());
    }

    private int deactivateUser(CoovaDeactivateUserRequest request) {
        return rd.deactivateUser(request.getRoom());
    }
}
