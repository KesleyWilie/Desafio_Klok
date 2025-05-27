package pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;

/**
 * Classe base para todas as Page Objects.
 * Fornece funcionalidades comuns como inicialização de elementos, cliques, digitação e verificações de visibilidade.
 */
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected int defaultTimeout;

    /**
     * Construtor da BasePage.
     * Inicializa o WebDriver, WebDriverWait e os WebElements da página usando PageFactory.
     * @param driver A instância do WebDriver a ser usada.
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.defaultTimeout = ConfigReader.getIntProperty("timeout.seconds", 10);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(this.defaultTimeout));
        PageFactory.initElements(driver, this);
    }

    /**
     * Clica em um WebElement após garantir que ele esteja visível e clicável.
     * Tenta recuperar o elemento em caso de StaleElementReferenceException.
     * @param element O WebElement a ser clicado.
     */
    protected void click(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (StaleElementReferenceException e) {
            System.err.println("StaleElementReferenceException ao clicar. Tentando novamente: " + element.toString());
            PageFactory.initElements(driver, this); // Re-inicializa os elementos na página atual
            wait.until(ExpectedConditions.visibilityOf(element));
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        }
    }

    /**
     * Digita um texto em um WebElement após garantir que ele esteja visível.
     * Limpa o campo antes de digitar.
     * @param element O WebElement onde o texto será digitado.
     * @param text O texto a ser digitado.
     */
    protected void type(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Verifica se um WebElement está visível na página.
     * Utiliza um timeout curto para esta verificação.
     * @param element O WebElement a ser verificado.
     * @return true se o elemento estiver visível, false caso contrário.
     */
    protected boolean isDisplayed(WebElement element) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(Math.min(3, defaultTimeout / 2)));
            shortWait.until(ExpectedConditions.visibilityOf(element));
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException | org.openqa.selenium.TimeoutException e) {
            return false;
        } catch (Exception e) {
            System.err.println("Exceção inesperada em isDisplayed para o elemento: " + element.toString() + " - " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica se um WebElement está pronto para interação (visível e clicável).
     * @param element O WebElement a ser verificado.
     * @param timeoutInSeconds Timeout opcional em segundos. Se não fornecido, usa metade do timeout padrão.
     * @return true se o elemento estiver pronto, false caso contrário.
     */
    protected boolean isElementReady(WebElement element, int... timeoutInSeconds) {
        int currentTimeout = (timeoutInSeconds.length > 0) ? timeoutInSeconds[0] : this.defaultTimeout / 2;
        if (currentTimeout <= 0) currentTimeout = 1; // Garante um timeout mínimo de 1 segundo

        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(currentTimeout));
            customWait.until(ExpectedConditions.visibilityOf(element));
            customWait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtém a URL atual da página.
     * @return A URL atual.
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Obtém o título da página atual.
     * @return O título da página.
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
}