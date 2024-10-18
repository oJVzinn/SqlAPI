package com.github.model;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class InsertModel {

    @NonNull
    private String tableName;

    private final Map<String, String> columnValues = new HashMap<>();

    public String makeSQLCommand() {
        return "INSERT INTO `" + tableName + "` (" + getColumns() + ") VALUES (" + getValues() + ");";
    }

    public void appendValue(String column, String value) {
        this.columnValues.put(column, value);
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

    private String getValues() {
        StringBuilder sb = new StringBuilder();
        columnValues.values().forEach(value -> {
            if (sb.length() != 0) {
                sb.append(", ");
            }

            sb.append("'").append(value).append("'");
        });

        return sb.toString();
    }
}
