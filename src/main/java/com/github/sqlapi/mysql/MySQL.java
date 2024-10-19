package com.github.sqlapi.mysql;

import com.github.sqlapi.interfaces.SQLInterface;
import com.github.sqlapi.logger.SQLogger;
import com.github.sqlapi.model.HikariModel;
import com.github.sqlapi.model.InsertModel;
import com.github.sqlapi.model.TableModel;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.*;

@RequiredArgsConstructor
public class MySQL implements SQLInterface {

    @NonNull
    private String host;

    @NonNull
    private String port;

    @NonNull
    private String user;

    @NonNull
    private String password;

    @NonNull
    private String database;

    private final SQLogger LOGGER = new SQLogger("MySQL");
    private HikariDataSource hikariDS;

    @Override
    public void connect(HikariModel model) throws Exception {
        LOGGER.info("Connecting to MySQL...");
        long startTime = System.currentTimeMillis();

        HikariConfig config = model.getConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(this.user);
        config.setPassword(this.password);
        this.hikariDS = new HikariDataSource(config);

        LOGGER.info("MySQL Connected in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    @Override
    public void disconnect() throws Exception {
        this.hikariDS.close();
        LOGGER.info("Disconnected");
    }

    @Override
    public void createTable(TableModel model, boolean log) throws Exception {
        try (Connection connection = hikariDS.getConnection()) {
            String sql = model.makeSQLCommand();
            if (log) LOGGER.info(sql);
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
            }
        }
    }

    @Override
    public void dropTable(String name, boolean log) throws Exception {
        try (Connection connection = hikariDS.getConnection()) {
            String sql = "DROP TABLE IF EXISTS " + name + ";";
            if (log) LOGGER.info(sql);
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
            }
        }
    }

    @Override
    public void insertValue(InsertModel model, boolean log) throws Exception {
        try (Connection connection = this.hikariDS.getConnection()) {
            String sql = model.makeSQLCommand();
            if (log) LOGGER.info(sql);
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.executeUpdate();
            }
        }
    }

    @Override
    public List<Object> selectValue(String tableName, String column, String columnKey, String valueKey, String conditional, boolean log) throws Exception {
        List<Object> result = new ArrayList<>();
        try (Connection connection = this.hikariDS.getConnection()) {
            String sql = "SELECT " + column + " FROM `" + tableName + "` WHERE `" + columnKey + "` " + conditional + " '" + valueKey + "';";
            if (log) LOGGER.info(sql);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        result.add(resultSet.getObject(column));
                    }
                }
            }
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> selectAll(String tableName, String columnKey, String valueKey, String conditional, boolean log) throws Exception {
        List<Map<String, Object>> result;
        try (Connection connection = this.hikariDS.getConnection()) {
            String sql = "SELECT * FROM " + tableName + " WHERE `" + columnKey + "` " + conditional + " '" + valueKey + "';";
            result = getAllResults(sql, connection, log);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> selectAll(String tableName, boolean log) throws Exception {
        List<Map<String, Object>> result;
        try (Connection connection = this.hikariDS.getConnection()) {
            String sql = "SELECT * FROM " + tableName;
            result = getAllResults(sql, connection, log);
        }

        return result;
    }

    @Override
    public void deleteRow(String tableName, String columnKey, String valueKey, String conditional, boolean log) throws Exception {
        try (Connection connection = this.hikariDS.getConnection()) {
            String sql = "DELETE FROM `" + tableName + "` WHERE `" + columnKey + "` " + conditional + " '" + valueKey + "';";
            if (log) LOGGER.info(sql);
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
            }
        }
    }

    @Override
    public void deleteAllRow(String tableName, boolean log) throws Exception {
        try (Connection connection = this.hikariDS.getConnection()) {
            String sql = "DELETE FROM `" + tableName + "`;";
            if (log) LOGGER.info(sql);
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
            }
        }
    }

    @Override
    public void updateColumn(String tableName, String column, String value, String columnKey, String valueKey, String conditional, boolean log) throws Exception {
        try (Connection connection = this.hikariDS.getConnection()) {
            String sql = "UPDATE `" + tableName + "` SET `" + column + "` = '" + value + "' WHERE `" + columnKey + "` " + conditional + " '" + valueKey + "';";
            if (log) LOGGER.info(sql);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.executeUpdate();
            }
        }
    }

    private List<Map<String, Object>> getAllResults(String SQL, Connection connection, boolean log) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        if (log) LOGGER.info(SQL);
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
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
            }
        }

        return result;
    }
}
