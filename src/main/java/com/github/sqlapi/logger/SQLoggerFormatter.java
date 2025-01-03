package com.github.sqlapi.logger;

import lombok.AllArgsConstructor;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

@AllArgsConstructor
public class SQLoggerFormatter extends Formatter {

    private String module;

    @Override
    public String format(LogRecord record) {
        return "\n[" + module + "] " + record.getMessage() + "\n";
    }

}
