package com.github.sqlapi.exeption;

public class ConnectionException extends Exception {

    public ConnectionException(String errorMessage) {
        super(errorMessage);
    }

}
