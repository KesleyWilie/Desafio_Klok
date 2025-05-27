package steps;

import io.cucumber.java.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import utils.ConfigReader;

/**
 * Hooks do Cucumber para configurar e limpar o ambiente de teste.
 * Inclui configuração do WebDriver antes de cada cenário e limpeza após cada cenário.
 */
public class Hooks {
    public static WebDriver driver;

    /**
     * Executado uma vez antes de todos os cenários.
     * Loga o início da configuração global.
     */
    @BeforeAll
    public static void beforeAll() {
        System.out.println("====================================================");
        System.out.println("INICIANDO CONFIGURAÇÃO GLOBAL DOS TESTES...");
        // ConfigReader é carregado estaticamente na sua primeira utilização.
        // WebDriverManager será chamado no @Before de cada cenário.
        System.out.println("====================================================");
    }

    /**
     * Executado antes de cada cenário.
     * Configura e inicializa o WebDriver com base nas propriedades definidas.
     * @param scenario Informações sobre o cenário atual.
     */
    @Before
    public void setupTest(Scenario scenario) {
        System.out.println("----------------------------------------------------");
        System.out.println("INICIANDO CENÁRIO: " + scenario.getName() + " | TAGS: " + scenario.getSourceTagNames());

        // Prioriza propriedades de sistema (ex: -Dbrowser=firefox), depois config.properties, depois default ("chrome")
        String browserType = System.getProperty("browser", ConfigReader.getProperty("browser", "chrome")).toLowerCase();
        String headlessProp = System.getProperty("headless", ConfigReader.getProperty("headless", "false"));
        boolean isHeadless = "true".equalsIgnoreCase(headlessProp);

        System.out.println("NAVEGADOR: " + browserType + " | HEADLESS: " + isHeadless);

        switch (browserType) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (isHeadless) {
                    firefoxOptions.addArguments("--headless");
                }
                // firefoxOptions.addArguments("--start-maximized"); // Maximizar pode ser instável em headless no Firefox
                driver = new FirefoxDriver(firefoxOptions);
                break;
            case "chrome":
            default: // Chrome como padrão
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-extensions"); // Desabilita extensões
                chromeOptions.addArguments("--disable-popup-blocking"); // Desabilita bloqueio de pop-ups
                // chromeOptions.addArguments("--incognito"); // Para rodar em modo anônimo

                if (isHeadless) {
                    chromeOptions.addArguments("--headless=new"); // Novo modo headless do Chrome
                    chromeOptions.addArguments("--disable-gpu"); // Necessário para algumas versões/ambientes em headless
                    chromeOptions.addArguments("--window-size=1920,1080"); // Garante tamanho consistente em headless
                } else {
                    chromeOptions.addArguments("--start-maximized"); // Inicia o navegador maximizado
                }
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        // É recomendado evitar misturar waits implícitos e explícitos.
        // A BasePage já utiliza waits explícitos (WebDriverWait).
        // Se fosse usar implicit wait:
        // int implicitWait = ConfigReader.getIntProperty("implicit.wait.seconds", 0);
        // if (implicitWait > 0) {
        //     driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        // }

        // A opção --start-maximized no Chrome já trata da maximização.
        // Para Firefox (se não usar --start-maximized) ou outros navegadores em modo não-headless:
        // if (!isHeadless && driver != null && !"chrome".equals(browserType)) { // Chrome já usa --start-maximized
        //    driver.manage().window().maximize();
        // }
    }

    /**
     * Executado após cada cenário.
     * Tira um screenshot se o cenário falhar e fecha o WebDriver.
     * @param scenario Informações sobre o cenário atual, incluindo seu status.
     */
    @After
    public void teardownTest(Scenario scenario) {
        System.out.println("FINALIZANDO CENÁRIO: " + scenario.getName() + " | STATUS: " + scenario.getStatus());
        if (driver != null) {
            if (scenario.isFailed()) {
                System.out.println("CENÁRIO FALHOU. Capturando screenshot...");
                try {
                    final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    // Nome do arquivo de screenshot mais descritivo para facilitar a identificação
                    String screenshotName = scenario.getName().replaceAll("[^a-zA-Z0-9.-]", "_") + "_failure.png";
                    scenario.attach(screenshot, "image/png", screenshotName);
                    System.out.println("Screenshot anexado ao relatório como: " + screenshotName);
                } catch (Exception e) {
                    System.err.println("ERRO AO CAPTURAR SCREENSHOT: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            driver.quit(); // Fecha todas as janelas do navegador e encerra a sessão do WebDriver
            driver = null; // Limpa a referência para a próxima execução de cenário
            System.out.println("NAVEGADOR FECHADO.");
        } else {
            System.out.println("Driver não estava inicializado, nada para fechar.");
        }
        System.out.println("----------------------------------------------------");
    }

    /**
     * Executado uma vez após todos os cenários.
     * Loga a finalização da execução dos testes.
     */
    @AfterAll
    public static void afterAll() {
        System.out.println("====================================================");
        System.out.println("FINALIZANDO EXECUÇÃO DE TODOS OS TESTES.");
        System.out.println("====================================================");
    }
}