package com.dailyinn.connect.connector;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.configuration.PropertiesConfiguration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class RaddbConnectionPool {

    private PropertiesConfiguration conf;
    private DataSource dataSource;

    public RaddbConnectionPool(PropertiesConfiguration conf) {
        this.conf = conf;
        init();
    }

    public void init() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl(conf.getString("raddb_url"));
        config.setUsername(conf.getString("raddb_user"));
        config.setPassword(conf.getString("raddb_passwd"));
        config.setMaximumPoolSize(10);
        config.setAutoCommit(false);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
