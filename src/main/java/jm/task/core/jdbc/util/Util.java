package jm.task.core.jdbc.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class Util {

    private static HikariDataSource dataSource;
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    private Util() {
    }

    public static Connection getConnection() throws SQLException {
        if (Objects.isNull(dataSource)) {
            dataSource = new HikariDataSource(getHikariConfig());
        }
        return dataSource.getConnection();
    }

    public static Session getSession() {
        if (Objects.isNull(sessionFactory)) {
            buildSessionFactory();
        }
        return sessionFactory.openSession();
    }

    public static void shutdown() {
        if (Objects.nonNull(sessionFactory)) {
            sessionFactory.close();
        }

        if (Objects.nonNull(registry)) {
            StandardServiceRegistryBuilder.destroy(registry);
        }

        if (Objects.nonNull(dataSource)) {
            dataSource.close();
        }
    }

    private static void buildSessionFactory() {
        try {
            Configuration hibernateConfig = getHibernateConfig();
            registry = new StandardServiceRegistryBuilder().applySettings(hibernateConfig.getProperties()).build();
            sessionFactory = hibernateConfig.buildSessionFactory(registry);
        } catch (Exception e) {
            if (Objects.nonNull(registry)) {
                StandardServiceRegistryBuilder.destroy(registry);
            }
            throw new RuntimeException(e);
        }
    }

    private static HikariConfig getHikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/kata");
        config.setUsername("root");
        config.setPassword("password");
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(10);
        config.setIdleTimeout(30000);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return config;
    }

    private static Configuration getHibernateConfig() {
        Configuration config = new Configuration();
        config.setProperty("hibernate.hikari.jdbcUrl", "jdbc:mysql://localhost:3306/kata");
        config.setProperty("hibernate.hikari.dataSource.user", "root");
        config.setProperty("hibernate.hikari.dataSource.password", "password");
        config.setProperty("hibernate.hikari.minimumIdle", "5");
        config.setProperty("hibernate.hikari.maximumPoolSize", "10");
        config.setProperty("hibernate.hikari.idleTimeout", "30000");
        config.setProperty("hibernate.current_session_context_class", "thread");
        config.addAnnotatedClass(User.class);
        return config;
    }
}
