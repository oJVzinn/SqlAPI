package com.github.interfaces;

import com.github.model.HikariModel;
import com.github.model.InsertModel;
import com.github.model.TableModel;

import java.util.List;
import java.util.Map;

public interface SQLInterface {

    void connect(HikariModel model) throws Exception;
    void disconnect() throws Exception;
    void createTable(TableModel model, boolean log) throws Exception;
    void dropTable(String name, boolean log) throws Exception;
    void insertValue(InsertModel model, boolean log) throws Exception;
    List<Object> selectValue(String tableName, String column, String columnKey, String valueKey, String conditional, boolean log) throws Exception;
    List<Map<String, Object>> selectAll(String tableName, boolean log) throws Exception;
    void deleteRow(String tableName, String columnKey, String valueKey, String conditional, boolean log) throws Exception;
    void deleteAllRow(String tableName, boolean log) throws Exception;
    void updateRow(String tableName, String column, String value, String columnKey, String valueKey, String conditional, boolean log) throws Exception;

}
