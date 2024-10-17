package com.github;

import com.github.model.HikariModel;
import com.github.mysql.MySQL;

public class Main {

    public static void main(String[] args) {
        HikariModel model = new HikariModel();
        MySQL mySQL = new MySQL("localhost", "3306", "root", "", "test");
        model.setupDefaultConfiguration();
        SQLManager.setLogSQL(true);
        try {
            SQLManager.initSQL(mySQL, model);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}