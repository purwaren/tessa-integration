package com.dailyinn.connect;

import com.dailyinn.connect.connector.RadiusDeskConnector;
import com.dailyinn.connect.listener.HttpListener;
import com.dailyinn.connect.listener.HttpListenerHandler;
import com.dailyinn.connect.connector.TesaConnector;
import com.dailyinn.connect.listener.ListenerCoovaHandler;
import com.dailyinn.connect.util.PasswordHash;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author purwa
 * @version 0.1
 */
public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        try {
            //prepare connection to TESA
            PropertiesConfiguration config = new PropertiesConfiguration("config.properties");
            PasswordHash passwordHash = new PasswordHash(config.getString("alg"));
            String strAuth = config.getString("auth");

            if(strAuth != null && strAuth.length() > 0) {
                String[] auth = strAuth.split(":");
                if(!(System.console() == null)) {
                    char[] pass = System.console().readPassword("Enter password: ");
                    String passwd = new String(pass);
                    if(!passwordHash.verifyPassword(auth[1], passwd)) {
                        System.out.println("Authentication failed, exit now");
                        System.exit(0);
                    }
                }
                else {
                    logger.debug("Must be run through shell, otherwise error.");
                }
            }
            else {
                logger.debug("Running with no authentication...");
            }

            RadiusDeskConnector rad = new RadiusDeskConnector(config);
            new Thread(rad).start();

            TesaConnector tesa = new TesaConnector(config.getString("host"), config.getInt("port"), config.getInt("timeout"));
            if (config.getBoolean("use.tesa")) {
                new Thread(tesa).start();
            }

            Thread.sleep(1000);

            //prepare listener
            HttpListenerHandler handler = new HttpListenerHandler(tesa, rad);
            ListenerCoovaHandler handler1 = new ListenerCoovaHandler(rad);
            HttpListener listener = new HttpListener(config.getInt("http.port"), config.getInt("http.timeout"), handler, handler1);
            new Thread(listener).start();

        } catch (ConfigurationException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
