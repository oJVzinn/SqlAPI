package com.github.sqlapi.interfaces;

public interface Model {

    String makeSQL();
    <T extends Model> T parse(Class<T> clazz);
}
