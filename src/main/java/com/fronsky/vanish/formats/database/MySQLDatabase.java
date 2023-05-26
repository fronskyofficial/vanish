package com.fronsky.vanish.formats.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.fronsky.vanish.logic.database.IDatabase;
import com.fronsky.vanish.logic.utils.Result;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQLDatabase implements IDatabase {
    private final HikariDataSource dataSource;

    public MySQLDatabase(String jdbcUrl, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        dataSource = new HikariDataSource(config);
    }

    @Override
    public Result<Connection> connect() {
        try {
            return new Result<>(dataSource.getConnection(), null);
        } catch (Exception exception) {
            return new Result<>(null, exception);
        }
    }

    @Override
    public void disconnect() {
        dataSource.close();
    }

    @Override
    public boolean isConnected() {
        return !dataSource.isClosed();
    }

    @Override
    public Result<ResultSet> executeQuery(String query) {
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            return new Result<>(statement.executeQuery(query), null);
        } catch (Exception exception) {
            return new Result<>(null, exception);
        }
    }
}
