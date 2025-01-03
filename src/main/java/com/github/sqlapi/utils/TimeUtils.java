package com.github.sqlapi.utils;

public class TimeUtils {

    private final Long start = System.currentTimeMillis();

    public Long getTimeElapsed() {
        return System.currentTimeMillis() - this.start;
    }

}
