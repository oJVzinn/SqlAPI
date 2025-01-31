package com.github.sqlapi.interfaces;

import com.github.sqlapi.exeption.ConnectionException;
import com.github.sqlapi.model.HikariModel;
import com.github.sqlapi.model.InsertModel;
import com.github.sqlapi.model.TableModel;
import com.github.sqlapi.model.UpdateModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface SQLInterface {

    void connect(HikariModel model) throws ConnectionException;
    void disconnect() throws ConnectionException;
    void createTable(TableModel model, boolean log) throws SQLException;
    void dropTable(String name, boolean log) throws SQLException;
    void insertValue(InsertModel model, boolean log) throws SQLException;
    List<Object> selectValue(String tableName, String column, String columnKey, String valueKey, String conditional, boolean log) throws SQLException;
    List<Map<String, Object>> selectAll(String tableName, String columnKey, String valueKey, String conditional, boolean log) throws SQLException;
    List<Map<String, Object>> selectAll(String tableName, boolean log) throws SQLException;
    void deleteRow(String tableName, String columnKey, String valueKey, String conditional, boolean log) throws SQLException;
    void deleteAllRow(String tableName, boolean log) throws SQLException;
    void updateColumn(String tableName, String column, String value, String columnKey, String valueKey, String conditional, boolean log) throws SQLException;
    void updateColumns(UpdateModel model, boolean log) throws SQLException;
    Connection getConnection() throws SQLException;

}
