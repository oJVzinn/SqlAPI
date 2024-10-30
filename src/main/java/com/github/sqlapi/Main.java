package com.github.sqlapi;

import com.github.sqlapi.model.HikariModel;
import com.github.sqlapi.model.InsertModel;
import com.github.sqlapi.model.TableModel;
import com.github.sqlapi.model.UpdateModel;
import com.github.sqlapi.mysql.MySQL;

public class Main {

    public static void main(String[] args) {
        HikariModel model = new HikariModel();
        MySQL mySQL = new MySQL("localhost", "3306", "root", "", "test");
        model.setupDefaultConfiguration();
        SQLManager.setLogSQL(true);
        try {
            SQLManager.initSQL(mySQL, model);
            UpdateModel updateModel = new UpdateModel("balb", "id", "1", "=");
            updateModel.appendValue("username", "ojvzinn");
            updateModel.appendValue("name", "gostoso");
            SQLManager.updateColumns(updateModel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}