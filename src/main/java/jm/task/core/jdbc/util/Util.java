package jm.task.core.jdbc.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Util {

    private static final HikariConfig HIKARI_CONFIG = new HikariConfig();
    private static final HikariDataSource DATA_SOURCE;

    static {
        HIKARI_CONFIG.setJdbcUrl("jdbc:mysql://localhost:3306/kata");
        HIKARI_CONFIG.setUsername("root");
        HIKARI_CONFIG.setPassword("password");
        HIKARI_CONFIG.addDataSourceProperty("cachePrepStmts", "true");
        HIKARI_CONFIG.addDataSourceProperty("prepStmtCacheSize", "250");
        HIKARI_CONFIG.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        DATA_SOURCE = new HikariDataSource(HIKARI_CONFIG);
    }

    private Util() {
    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }
}
