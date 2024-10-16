# SqlAPI
Um gerenciador para banco de dados relacionais SQL, com CRUD completo

# Como começar

### Para se utilizar o SqlAPI é bem simples. Aonde todas as funções disponíveis ficam disponíveis na classe "SQLManager", aonde para se utilizar, é necessário apenas instanciar qual cliente SQL é ideal para sua aplicação e sua configuração de preferência do gerenciador de polls, "Hikari".

## Como instanciar o meu cliente SQL de preferência?

### Para se instanciar o seu cliente SQL de preferência é bem simples. Sendo necessário instanciar a classe do gerenciador do client, que neste exemplo, usaremos o "MySQL", configurar o "Hikari" de acordo com sua preferência e iniciar utilizando a função "initSQL", da classe "SQLManager".

<br/>

```java
public static void main(String[] args) {
    HikariModel model = new HikariModel(); //Classe de configuração do Hikari
    MySQL mySQL = new MySQL("localhost", "3306", "root", "", "test"); //Instancia a classe gerenciadora do MySQL
    model.setupDefaultConfiguration(); //Executando a configuração padrão do hikari

    try {
        SQLManager.setLogSQL(true); //Configurando a logs de SQL executadas para serem exibidas
        SQLManager.initSQL(mySQL, model); //Iniciando o MySQL com as configurações feitas no hikari
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
```

<br/>

### Com isso, já possível realizar quaisquer execução de SQL com o auxilio das funções disponíveis na classe "SQLManager".
