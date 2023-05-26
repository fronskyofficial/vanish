package com.fronsky.vanish.logic.database;

import com.fronsky.vanish.logic.utils.Result;

import java.sql.Connection;
import java.sql.ResultSet;

public interface IDatabase {
    /**
     * Establishes a connection to a database and returns the result as a Result object.
     *
     * @return a Result object containing the Connection object representing the established connection.
     */
    Result<Connection> connect();

    /**
     * Disconnects from the database.
     */
    void disconnect();

    /**
     * Checks if the object is currently connected to the database.
     *
     * @return true if connected, false otherwise.
     */
    boolean isConnected();

    /**
     * Executes a database query and returns the result as a Result object.
     *
     * @param query the SQL query to be executed.
     * @return a Result object containing the result of the query.
     */
    Result<ResultSet> executeQuery(String query);
}
