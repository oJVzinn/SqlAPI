package com.github.sqlapi;

import com.github.sqlapi.interfaces.SQLInterface;
import com.github.sqlapi.model.HikariModel;
import com.github.sqlapi.model.InsertModel;
import com.github.sqlapi.model.TableModel;
import lombok.Setter;

import java.util.List;
import java.util.Map;

public class SQLManager {

    @Setter
    private static SQLInterface instance;

    @Setter
    private static boolean logSQL = false;

    public static void initSQL(SQLInterface sql, HikariModel model) throws Exception {
        sql.connect(model);
        instance = sql;
    }

    public static void destroySQL() throws Exception {
        instance.disconnect();
        instance = null;
    }

    public static void createTable(TableModel model) throws Exception {
        instance.createTable(model, logSQL);
    }

    public static void dropTable(String name) throws Exception {
        instance.dropTable(name, logSQL);
    }

    public static void insertValue(InsertModel model) throws Exception {
        instance.insertValue(model, logSQL);
    }

    public static List<Object> selectValue(String tableName, String column, String columnKey, String valueKey, String conditional) throws Exception {
        return instance.selectValue(tableName, column, columnKey, valueKey, conditional, logSQL);
    }

    public static List<Map<String, Object>> selectAll(String tableName, String columnKey, String valueKey, String conditional) throws Exception {
        return instance.selectAll(tableName, columnKey, valueKey, conditional, logSQL);
    }

    public static List<Map<String, Object>> selectAll(String tableName) throws Exception {
        return instance.selectAll(tableName, logSQL);
    }

    public static void deleteRow(String tableName, String columnKey, String valueKey, String conditional) throws Exception {
        instance.deleteRow(tableName, columnKey, valueKey, conditional, logSQL);
    }

    public static void deleteAllRow(String tableName) throws Exception {
        instance.deleteAllRow(tableName, logSQL);
    }

    public static void updateColumn(String tableName, String column, String value, String columnKey, String valueKey, String conditional) throws Exception {
        instance.updateColumn(tableName, column, value, columnKey, valueKey, conditional, logSQL);
    }

    public static <T extends SQLInterface> T getInstance(Class<T> classParse) {
        if (!classParse.isInstance(instance)) {
            return null;
        }

        return classParse.cast(instance);
    }

}
