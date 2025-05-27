package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.ConfigReader;

/**
 * Page Object para a página inicial da Amazon.
 * Contém elementos e métodos para interagir com a home page.
 */
public class AmazonHomePage extends BasePage {

    @FindBy(id = "twotabsearchtextbox")
    private WebElement searchInput;

    @FindBy(id = "nav-search-submit-button")
    private WebElement searchButton;

    @FindBy(id = "sp-cc-accept")
    private WebElement acceptCookiesButton;

    /**
     * Link para a página "Ofertas do Dia".
     * Localizador XPath: //a[@href='/deals?ref_=nav_cs_gb' and normalize-space(.)='Ofertas do Dia']
     */
    @FindBy(xpath = "//a[@href='/deals?ref_=nav_cs_gb' and normalize-space(.)='Ofertas do Dia']")
    private WebElement ofertasDoDiaLink;

    /**
     * Elemento identificador da página "Ofertas do Dia", usado para verificar se a página correta foi carregada.
     * Localizador XPath: //h1[normalize-space(.)='Ofertas e Promoções']
     */
    @FindBy(xpath = "//h1[normalize-space(.)='Ofertas e Promoções']")
    private WebElement ofertasDoDiaPageIdentifier;

    /**
     * Construtor da AmazonHomePage.
     * @param driver A instância do WebDriver a ser usada.
     */
    public AmazonHomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Clica no link "Ofertas do Dia" no menu de navegação.
     */
    public void clickOnOfertasDoDia() {
        wait.until(ExpectedConditions.elementToBeClickable(ofertasDoDiaLink));
        click(ofertasDoDiaLink);
    }

    /**
     * Navega para a página inicial da Amazon e aceita cookies se o botão estiver presente.
     */
    public void navigateToHomePage() {
        driver.get(ConfigReader.getProperty("amazon.url"));
        acceptCookiesIfPresent();
    }

    /**
     * Verifica se o botão de aceitar cookies está presente e clicável, e o clica.
     * Usa um timeout curto (3 segundos) específico para a aparição do botão de cookies.
     */
    private void acceptCookiesIfPresent() {
        if (isElementReady(acceptCookiesButton, 3)) { // Timeout de 3s para o botão de cookie
            click(acceptCookiesButton);
        }
    }

    /**
     * Realiza uma busca por um produto.
     * @param product O termo a ser buscado.
     * @return Uma instância de AmazonResultsPage representando a página de resultados da busca.
     */
    public AmazonResultsPage searchFor(String product) {
        type(searchInput, product);
        click(searchButton);
        return new AmazonResultsPage(driver);
    }

    /**
     * Verifica se o elemento identificador da página "Ofertas do Dia" está visível.
     * @return true se o identificador estiver visível, false caso contrário.
     */
    public boolean isOfertasDoDiaPageVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOf(ofertasDoDiaPageIdentifier));
            return true;
        } catch (Exception e) {
            System.err.println("Identificador da página de Ofertas do Dia não encontrado: " + ofertasDoDiaPageIdentifier.toString());
            return false;
        }
    }

    /**
     * Obtém o texto do título principal da página "Ofertas do Dia".
     * @return O texto do título se a página estiver visível, ou uma mensagem de erro.
     */
    public String getOfertasDoDiaPageTitleText() {
        if(isOfertasDoDiaPageVisible()){
            return ofertasDoDiaPageIdentifier.getText().trim();
        }
        return "Título da página de Ofertas do Dia não encontrado.";
    }
}