package com.github.sqlapi.model;

import com.github.sqlapi.SQLManager;
import com.github.sqlapi.interfaces.Model;
import com.github.sqlapi.interfaces.SQLInterface;
import com.github.sqlapi.sqlite.SQLite;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TableModel implements Model {

    @NonNull
    private String name;

    private final StringBuilder sqlBuilder = new StringBuilder();

    public void appendPrimaryKey(String column, String type, boolean notNull, boolean autoIncrement, boolean unique) {
        checkIfSetComma();
        this.sqlBuilder.append(column)
                .append(" ")
                .append(type)
                .append(" PRIMARY KEY")
                .append(notNull ? " NOT NULL" : "")
                .append(autoIncrement ? " " + getAutoIncrement() : "")
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

    private String getAutoIncrement() {
        if (SQLManager.getInstance(SQLInterface.class) instanceof SQLite) {
            return "AUTOINCREMENT";
        }

        return "AUTO_INCREMENT";
    }

    @Override
    public String makeSQL() {
        return "CREATE TABLE IF NOT EXISTS " + name + " (" + sqlBuilder + ");";
    }

    @Override
    public <T extends Model> T parse(Class<T> clazz) {
        return clazz.isInstance(this) ? clazz.cast(this) : null;
    }
}
