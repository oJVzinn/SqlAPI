import com.github.sqlapi.SQLManager;
import com.github.sqlapi.interfaces.Model;
import com.github.sqlapi.model.HikariModel;
import com.github.sqlapi.model.InsertModel;
import com.github.sqlapi.model.TableModel;
import com.github.sqlapi.sqlite.SQLite;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class Teste {

    @SneakyThrows
    @Test
    void teste() {
        SQLite sqLite = new SQLite("db", "C:/Users/joaov/Área de Trabalho/ARQUIVOS DO JV/ARQUIVOS DO JV/GitHub/MySQLAPI_SRC/MySQLAPI/src/main/resources/");
        HikariModel hikariModel = new HikariModel();
        hikariModel.setupDefaultConfiguration();
        SQLManager.initSQL(sqLite, hikariModel);
        SQLManager.setLogSQL(true);

        Model model = new TableModel("Teste");
        model.parse(TableModel.class).appendPrimaryKey("id", "INTEGER", false, true, false);
        model.parse(TableModel.class).appendColumn("name", "TEXT", true);
        model.parse(TableModel.class).appendColumn("age", "INTEGER", true);
        SQLManager.createTable((TableModel) model);

        if (SQLManager.selectValue("Teste", "name", "id", "1", "=").isEmpty()) {
            model = new InsertModel("Teste");
            model.parse(InsertModel.class).appendValue("name", "SwatingViado");
            model.parse(InsertModel.class).appendValue("age", "69");
            SQLManager.insertValue((InsertModel) model);
        }

        if (SQLManager.selectValue("Teste", "name", "id", "2", "=").isEmpty()) {
            model = new InsertModel("Teste");
            model.parse(InsertModel.class).appendValue("name", "BalbDaOCu");
            model.parse(InsertModel.class).appendValue("age", "69");
            SQLManager.insertValue((InsertModel) model);
        }
    }
}
