package com.github.sqlapi.logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class SQLogger extends Logger {

    public SQLogger(String module) {
        super(module, null);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SQLoggerFormatter(module));
        addHandler(handler);
        setUseParentHandlers(false);
    }

}
