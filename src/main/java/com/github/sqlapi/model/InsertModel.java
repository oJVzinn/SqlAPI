package com.github.sqlapi.model;

import com.github.sqlapi.interfaces.Model;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class InsertModel implements Model {

    @NonNull
    private String tableName;

    private final Map<String, Object> columnValues = new LinkedHashMap<>();

    public void appendValue(String column, Object value) {
        this.columnValues.put(column, value);
    }

    public int getTotalIndexes() {
        return columnValues.size();
    }

    public Object getValue(int index) {
        List<String> keys = new ArrayList<>(columnValues.keySet());
        String key = keys.get(index);
        return columnValues.get(key);
    }

    private String getColumns() {
        StringBuilder sb = new StringBuilder();
        columnValues.keySet().forEach(column -> {
            if (sb.length() != 0) {
                sb.append(", ");
            }

            sb.append("`").append(column).append("`");
        });

        return sb.toString();
    }

    private String getPlaceholders() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columnValues.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }

            sb.append("?");
        }

        return sb.toString();
    }

    @Override
    public String makeSQL() {
        return "INSERT INTO `" + tableName + "` (" + getColumns() + ") VALUES (" + getPlaceholders() + ");";
    }

    @Override
    public <T extends Model> T parse(Class<T> clazz) {
        return clazz.isInstance(this) ? clazz.cast(this) : null;
    }
}
