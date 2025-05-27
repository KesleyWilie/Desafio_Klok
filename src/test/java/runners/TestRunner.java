package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
// import org.testng.annotations.DataProvider; // Descomente para habilitar execução paralela de cenários via TestNG

/**
 * Classe Test Runner para executar os testes Cucumber com TestNG.
 * Configura as opções do Cucumber, como o caminho das features, glue (step definitions),
 * plugins de relatório e tags a serem executadas.
 */
@CucumberOptions(
        features = "src/test/resources/features", // Caminho para os arquivos .feature
        glue = {"steps"}, // Pacote onde estão os Step Definitions e Hooks
        plugin = { // Formatos de relatório
                "pretty", // Saída legível no console
                "html:target/cucumber-reports/cucumber-html-report.html", // Relatório HTML
                "json:target/cucumber-reports/cucumber.json", // Relatório JSON
                "testng:target/cucumber-reports/cucumber-testng.xml", // Relatório XML para TestNG
                "timeline:target/cucumber-reports/timeline" // Relatório de timeline
        },
        monochrome = true, // Saída do console mais legível (remove caracteres de cor)
        tags = "@regression or @magalu or @amazon" // Executa cenários com qualquer uma destas tags.
        // Para rodar uma tag específica via CLI: mvn test -Dcucumber.filter.tags="@sua_tag"
)
public class TestRunner extends AbstractTestNGCucumberTests {

        /**
         * Sobrescreve o método scenarios para habilitar a execução paralela de cenários com TestNG.
         * Descomente o método e a anotação @DataProvider(parallel = true) para ativar.
         */
        // @Override
        // @DataProvider(parallel = true)
        // public Object[][] scenarios() {
        //     return super.scenarios();
        // }
}