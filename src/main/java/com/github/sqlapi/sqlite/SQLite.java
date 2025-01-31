package com.github.sqlapi.sqlite;

import com.github.sqlapi.exeption.ConnectionException;
import com.github.sqlapi.interfaces.SQLInterface;
import com.github.sqlapi.logger.SQLogger;
import com.github.sqlapi.model.HikariModel;
import com.github.sqlapi.model.InsertModel;
import com.github.sqlapi.model.TableModel;
import com.github.sqlapi.model.UpdateModel;
import com.github.sqlapi.utils.TimeUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class SQLite implements SQLInterface {

    @NonNull
    private String database;

    @NonNull
    private String dbPatch;

    private final SQLogger LOGGER = new SQLogger("SQLite");
    private HikariDataSource hikariDS;


    @Override
    public void connect(HikariModel model) throws ConnectionException {
        LOGGER.info("Connecting to SQLite...");
        TimeUtils timeUtils = new TimeUtils();
        createDBFile();

        try {
            HikariConfig config = model.getConfig();
            config.setJdbcUrl("jdbc:sqlite:" + dbPatch + database + ".db");
            this.hikariDS = new HikariDataSource(config);
        } catch (Exception ex) {
            throw new ConnectionException("Error on create connection: " + ex);
        }

        LOGGER.info("SQLite Connected in " + timeUtils.getTimeElapsed() + "ms");
    }

    @Override
    public void disconnect() throws ConnectionException {
        try {
            this.hikariDS.close();
        } catch (Exception ex) {
            throw new ConnectionException("Error on disable connection: " + ex.getMessage());
        }

        LOGGER.info("Disconnected");
    }

    @Override
    public void createTable(TableModel model, boolean log) throws SQLException {
        TimeUtils timeUtils = new TimeUtils();
        try (Connection connection = hikariDS.getConnection()) {
            String sql = model.makeSQL();
            if (log) LOGGER.info(sql);
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
                if (log) LOGGER.info("Query execute in " + timeUtils.getTimeElapsed() + "ms");
            }
        }
    }

    @Override
    public void dropTable(String name, boolean log) throws SQLException {
        TimeUtils timeUtils = new TimeUtils();
        try (Connection connection = hikariDS.getConnection()) {
            String sql = "DROP TABLE IF EXISTS " + name + ";";
            if (log) LOGGER.info(sql);
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
                if (log) LOGGER.info("Query execute in " + timeUtils.getTimeElapsed() + "ms");
            }
        }
    }

    @Override
    public void insertValue(InsertModel model, boolean log) throws SQLException {
        TimeUtils timeUtils = new TimeUtils();
        try (Connection connection = this.hikariDS.getConnection()) {
            String sql = model.makeSQL();
            if (log) LOGGER.info(sql);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (int i = 0; i < model.getTotalIndexes(); i++) {
                    statement.setObject(i + 1, model.getValue(i));
                }

                statement.executeUpdate();
                if (log) LOGGER.info("Query execute in " + timeUtils.getTimeElapsed() + "ms");
            }
        }
    }

    @Override
    public List<Object> selectValue(String tableName, String column, String columnKey, String valueKey, String conditional, boolean log) throws SQLException {
        TimeUtils timeUtils = new TimeUtils();
        List<Object> result = new ArrayList<>();
        try (Connection connection = this.hikariDS.getConnection()) {
            String sql = "SELECT " + column + " FROM `" + tableName + "` WHERE `" + columnKey + "` " + conditional + " ?;";
            if (log) LOGGER.info(sql);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, valueKey);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        result.add(resultSet.getObject(column));
                    }
                    if (log) LOGGER.info("Query execute in " + timeUtils.getTimeElapsed() + "ms");
                }
            }
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> selectAll(String tableName, String columnKey, String valueKey, String conditional, boolean log) throws SQLException {
        List<Map<String, Object>> result;
        try (Connection connection = this.hikariDS.getConnection()) {
            String sql = "SELECT * FROM " + tableName + " WHERE `" + columnKey + "` " + conditional + " ?;";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, valueKey);
                result = getAllResults(sql, statement, log);
            }
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> selectAll(String tableName, boolean log) throws SQLException {
        List<Map<String, Object>> result;
        try (Connection connection = this.hikariDS.getConnection()) {
            String sql = "SELECT * FROM " + tableName;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                result = getAllResults(sql, statement, log);
            }
        }

        return result;
    }

    @Override
    public void deleteRow(String tableName, String columnKey, String valueKey, String conditional, boolean log) throws SQLException {
        TimeUtils timeUtils = new TimeUtils();
        try (Connection connection = this.hikariDS.getConnection()) {
            String sql = "DELETE FROM `" + tableName + "` WHERE `" + columnKey + "` " + conditional + " '" + valueKey + "';";
            if (log) LOGGER.info(sql);
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
                if (log) LOGGER.info("Query execute in " + timeUtils.getTimeElapsed() + "ms");
            }
        }
    }

    @Override
    public void deleteAllRow(String tableName, boolean log) throws SQLException {
        TimeUtils timeUtils = new TimeUtils();
        try (Connection connection = this.hikariDS.getConnection()) {
            String sql = "DELETE FROM `" + tableName + "`;";
            if (log) LOGGER.info(sql);
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
                if (log) LOGGER.info("Query execute in " + timeUtils.getTimeElapsed() + "ms");
            }
        }
    }

    @Override
    public void updateColumn(String tableName, String column, String value, String columnKey, String valueKey, String conditional, boolean log) throws SQLException {
        TimeUtils timeUtils = new TimeUtils();
        try (Connection connection = this.hikariDS.getConnection()) {
            String sql = "UPDATE `" + tableName + "` SET `" + column + "` = ? WHERE `" + columnKey + "` " + conditional + " ?;";
            if (log) LOGGER.info(sql);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, value);
                statement.setString(2, valueKey);
                statement.executeUpdate();
                if (log) LOGGER.info("Query execute in " + timeUtils.getTimeElapsed() + "ms");
            }
        }
    }

    @Override
    public void updateColumns(UpdateModel model, boolean log) throws SQLException {
        TimeUtils timeUtils = new TimeUtils();
        try (Connection connection = this.hikariDS.getConnection()) {
            String sql = model.makeSQL();
            if (log) LOGGER.info(sql);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.executeUpdate();
                if (log) LOGGER.info("Query execute in " + timeUtils.getTimeElapsed() + "ms");
            }
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.hikariDS.getConnection();
    }

    private List<Map<String, Object>> getAllResults(String SQL, PreparedStatement statement, boolean log) throws SQLException {
        TimeUtils timeUtils = new TimeUtils();
        List<Map<String, Object>> result = new ArrayList<>();
        if (log) LOGGER.info(SQL);
        try (ResultSet resultSet = statement.executeQuery()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                Map<String, Object> mapResult = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    mapResult.put(columnName, value);
                }
                result.add(mapResult);
            }

            if (log) LOGGER.info("Query execute in " + timeUtils.getTimeElapsed() + "ms");

        }

        return result;
    }

    private void createDBFile() {
        try {
            File file = new File(this.dbPatch + "/" + this.database + ".db");
            if (!file.exists()) {
                File folder = file.getParentFile();
                if (!folder.exists()) {
                    if (!folder.mkdirs()) {
                        LOGGER.severe("Failed to create DB folder.");
                        throw new IOException("Failed to create DB folder.");
                    }
                }
                if (!file.createNewFile()) {
                    LOGGER.severe("Failed to create DB file.");
                    throw new IOException("Failed to create DB file.");
                }
            }
        } catch (IOException ex) {
            LOGGER.severe("Error while creating DB file: " + ex.getMessage());
            throw new RuntimeException("Error while creating DB file.", ex);
        }
    }

}
