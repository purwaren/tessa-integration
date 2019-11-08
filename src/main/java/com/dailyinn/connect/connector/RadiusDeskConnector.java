package com.dailyinn.connect.connector;

import com.dailyinn.connect.dto.CheckInRequest;
import com.dailyinn.connect.dto.RdResponse;
import com.google.gson.Gson;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RadiusDeskConnector implements Runnable{
    Logger logger = LoggerFactory.getLogger(getClass());
    PropertiesConfiguration conf;
    CloseableHttpClient client;
    BasicCookieStore cookieStore;
    RaddbConnectionPool pool;

    public RadiusDeskConnector(PropertiesConfiguration conf) {
        this.conf = conf;
        pool = new RaddbConnectionPool(conf);
    }

    @Override
    public void run() {
        cookieStore = new BasicCookieStore();
        client = HttpClientBuilder.create()
                .setDefaultCookieStore(cookieStore).build();

        try {
            CloseableHttpResponse response = login();
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                logger.debug("init connection to radiusdesk succes");
                HttpEntity entity = response.getEntity();
                RdResponse rdResponse = new Gson().fromJson(EntityUtils.toString(entity), RdResponse.class);
                if(rdResponse.isSuccess()) {
                    logger.debug("radiusdesk is OK");
                }
            }
            logger.debug("test update password");
//            this.updatePassword("purwaren@dailyinn", "abc123");

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private CloseableHttpResponse login() throws IOException {
        HttpPost httpPost = new HttpPost(conf.getString("rd_host")+conf.getString("rd_auth_url"));
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", conf.getString("rd_user")));
        params.add(new BasicNameValuePair("password", conf.getString("rd_passwd")));
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        return client.execute(httpPost);
    }

    public String[] createVoucher(CheckInRequest request) throws IOException {
        String[] voucher = new String[2];
        HttpPost httpPost = new HttpPost(conf.getString("rd_host") + conf.getString("rd_voucher_url"));

        return voucher;
    }

    public int updatePassword(String username, String password) {
        activateUser(username);
        int status = 0;
        String sql = "UPDATE radcheck SET value = ? WHERE username = ? AND attribute = ?";
        try (
            Connection con = pool.getConnection();
            PreparedStatement prep = con.prepareStatement(sql);
        ) {
            prep.setString(1, password);
            prep.setString(2, username);
            prep.setString(3, "Cleartext-Password");
            status = prep.executeUpdate();
            logger.debug("status query == "+status);
            con.commit();
            prep.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    public int deactivateUser(String username) {
        int status = 0;
        try {
            Connection con = pool.getConnection();
            String sql = "UPDATE radcheck SET value = ? WHERE username = ? AND attribute = ?";
            PreparedStatement prep = con.prepareStatement(sql);
            prep.setInt(1, 1);
            prep.setString(2, username);
            prep.setString(3, "Rd-Account-Disabled");
            status = prep.executeUpdate();
            logger.debug("status query == "+status);
            con.commit();
            prep.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    private int activateUser(String username) {
        int status = 0;
        try {
            Connection con = pool.getConnection();
            String sql = "UPDATE radcheck SET value = ? WHERE username = ? AND attribute = ?";
            PreparedStatement prep = con.prepareStatement(sql);
            prep.setInt(1, 0);
            prep.setString(2, username);
            prep.setString(3, "Rd-Account-Disabled");
            status = prep.executeUpdate();
            logger.debug("status query == "+status);
            con.commit();
            prep.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }
}
