package com.quizapp.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Provides pooled JDBC connections using HikariCP.
 *
 * <p>Reads DB connection info from environment variables (DB_URL, DB_USER, DB_PASS)
 * with sensible defaults for local development. Callers must close returned
 * Connection objects (they are returned to the pool).</p>
 */
public final class DBConnection {
    private static final HikariDataSource ds;

    static {
        HikariConfig config = new HikariConfig();

        // Read environment variables if present; fallback to local defaults.
        String url = System.getenv().getOrDefault("DB_URL",
                "jdbc:mysql://localhost:3306/quiz_platform?useSSL=false&allowPublicKeyRetrieval=true");
        String user = System.getenv().getOrDefault("DB_USER", "root");
        String pass = System.getenv().getOrDefault("DB_PASS", "");

        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(pass);

        // Pool tuning defaults - adjust for production as needed.
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        // Simple validation query for MySQL
        config.setConnectionTestQuery("SELECT 1");

        ds = new HikariDataSource(config);
    }

    private DBConnection() { /* no instances */ }

    /**
     * Obtain a Connection from the pool. Caller must close it.
     *
     * @return a JDBC Connection
     * @throws SQLException when the pool cannot provide a connection
     */
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    /**
     * Access the underlying DataSource (useful for frameworks or tests).
     *
     * @return the Hikari DataSource instance
     */
    public static DataSource getDataSource() {
        return ds;
    }
}
