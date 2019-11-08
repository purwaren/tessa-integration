package com.dailyinn.connect.listener;

import org.apache.http.ExceptionLogger;
import org.apache.http.impl.nio.bootstrap.HttpServer;
import org.apache.http.impl.nio.bootstrap.ServerBootstrap;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author purwa
 * @version 0.1
 */
public class HttpListener implements Runnable {
    Logger logger = LoggerFactory.getLogger(getClass());
    private int timeout;
    private int port;
    private HttpListenerHandler httpHandler;
    private ListenerCoovaHandler coovaHandler;

    public HttpListener(int port, int timeout, HttpListenerHandler handler, ListenerCoovaHandler handler2) {
        this.port = port;
        this.timeout = timeout;
        this.httpHandler = handler;
        this.coovaHandler = handler2;
    }

    @Override
    public void run() {
        initHttpListener();
    }

    private void initHttpListener()  {
        SSLContext sslContext = null;
        try {
            IOReactorConfig config = IOReactorConfig.custom()
                    .setSoTimeout(timeout)
                    .setTcpNoDelay(true)
                    .build();

            final HttpServer server = ServerBootstrap.bootstrap()
                    .setListenerPort(port)
                    .setServerInfo("HTTP/1.1")
                    .setIOReactorConfig(config)
                    .setSslContext(sslContext)
                    .setExceptionLogger(ExceptionLogger.STD_ERR)
                    .registerHandler("/tesa",httpHandler)
                    .registerHandler("/hotspot/*", coovaHandler)
                    .create();

            server.start();
            logger.debug("HTTP Listener started on port "+this.port+", with timeout "+this.timeout+" seconds");

            server.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    server.shutdown(5, TimeUnit.SECONDS);
                }
            });

        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
