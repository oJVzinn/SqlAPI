package com.github.sqlapi.model;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TableModel {

    @NonNull
    private String name;

    private final StringBuilder sqlBuilder = new StringBuilder();

    public String makeSQLCommand() {
        return "CREATE TABLE IF NOT EXISTS " + name + " (" + sqlBuilder + ");";
    }

    public void appendPrimaryKey(String column, String type, boolean notNull, boolean autoIncrement, boolean unique) {
        checkIfSetComma();
        this.sqlBuilder.append(column)
                .append(" ")
                .append(type)
                .append(" PRIMARY KEY")
                .append(notNull ? " NOT NULL" : "")
                .append(autoIncrement ? " AUTO_INCREMENT" : "")
                .append(unique ? " UNIQUE" : "");
    }

    public void appendColumn(String column, String type, boolean notNull) {
        checkIfSetComma();
        this.sqlBuilder.append(column)
                .append(" ")
                .append(type)
                .append(notNull ? " NOT NULL" : "");
    }

    private void checkIfSetComma() {
        if (this.sqlBuilder.length() != 0) {
            this.sqlBuilder.append(", ");
        }
    }
}
