package com.github.logger;

import lombok.AllArgsConstructor;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

@AllArgsConstructor
public class SQLoggerFormatter extends Formatter {

    private String module;

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        String color;
        switch (record.getLevel().getName().toLowerCase()) {
            case "warning": {
                color = translateColor("&e");
                break;
            }

            case "severe": {
                color = translateColor("&c");
                break;
            }

            default: {
                color = translateColor("&a");
                break;
            }
        }

        builder.append("\n")
                .append(color)
                .append("[").
                append(module)
                .append("] ")
                .append(record.getMessage())
                .append(translateColor("&r"))
                .append("\n");
        return builder.toString();
    }

    private static String translateColor(String message){
        return message.replace("&c", "\u001B[31m")
                .replace("&a", "\u001B[32;1m")
                .replace("&e", "\u001B[33m")
                .replace("&r", "\u001B[0m");
    }
}
