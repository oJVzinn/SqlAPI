# SqlAPI
Um gerenciador para banco de dados relacionais SQL, com CRUD completo

# Como começar

Adicione ao seu pom.xml
```maven
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

	<dependency>
	    <groupId>com.github.oJVzinn</groupId>
	    <artifactId>SqlAPI</artifactId>
	    <version>0.5.4</version>
	</dependency>
```

ou no seu build.gradle
```gradle
	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}

dependencies {
	        implementation 'com.github.oJVzinn:SqlAPI:0.5.4'
	}
```

### Para se utilizar o SqlAPI é bem simples. Todas as funções disponíveis ficam disponíveis na classe "SQLManager", aonde para se utilizar, é necessário apenas instanciar qual cliente SQL é ideal para sua aplicação e sua configuração de preferência do gerenciador de polls, "Hikari".

## Como instanciar o meu cliente SQL de preferência?

### Neste exemplo usaremos o "MySQL", e para iniciar é necessário configurar o "Hikari" de acordo com sua preferência, após isso é necessário chamar a função "initSQL", da classe "SQLManager".

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
