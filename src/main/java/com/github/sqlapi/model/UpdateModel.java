package com.github.sqlapi.model;

import com.github.sqlapi.interfaces.Model;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class UpdateModel implements Model {

    @NonNull
    private String tableName;

    @NonNull
    private String columnKey;

    @NonNull
    private String valueKey;

    @NonNull
    private String logic;

    private final Map<String, String> columnValues = new HashMap<>();

    public void appendValue(String column, String value) {
        this.columnValues.put(column, value);
    }

    private String getValue() {
        StringBuilder sb = new StringBuilder();
        columnValues.keySet().forEach(column -> {
            if (sb.length() != 0) {
                sb.append(", ");
            }

            sb.append("`").append(column).append("`").append(" = ").append("'").append(columnValues.get(column)).append("'");
        });

        return sb.toString();
    }

    @Override
    public String makeSQL() {
        return "UPDATE `"  + tableName + "` SET " + getValue() + " WHERE `" + columnKey + "` " + logic + " '" + valueKey + "'";
    }

    @Override
    public <T extends Model> T parse(Class<T> clazz) {
        return clazz.isInstance(this) ? clazz.cast(this) : null;
    }
}
