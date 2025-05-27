package pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.ConfigReader;

/**
 * Page Object para a página inicial da Magazine Luiza.
 * Contém elementos e métodos para interagir com a home page.
 */
public class MagaluHomePage extends BasePage {

    @FindBy(id = "input-search")
    private WebElement searchInput;

    /**
     * Botão de submissão da busca.
     * Localizador CSS: button[data-testid='search-submit']
     */
    @FindBy(css = "button[data-testid='search-submit']")
    private WebElement searchButton;

    /**
     * Link para a página "Ofertas do Dia".
     * Localizador CSS: a[data-testid='link'][href='/selecao/ofertasdodia/']
     */
    @FindBy(css = "a[data-testid='link'][href='/selecao/ofertasdodia/']")
    private WebElement ofertasDoDiaLink;

    /**
     * Elemento identificador da página "Ofertas do Dia" da Magalu (ex: título "Aproveite!").
     * Localizador CSS: h1[data-testid='main-title'][title='Aproveite!']
     */
    @FindBy(css = "h1[data-testid='main-title'][title='Aproveite!']")
    private WebElement ofertasDoDiaPageIdentifier;

    /**
     * Construtor da MagaluHomePage.
     * @param driver A instância do WebDriver a ser usada.
     */
    public MagaluHomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navega para a página inicial da Magazine Luiza.
     * Futuramente, pode incluir tratamento de pop-ups ou cookies específicos da Magalu.
     */
    public void navigateToHomePage() {
        driver.get(ConfigReader.getProperty("magalu.url"));
        // Exemplo de futuro tratamento: acceptMagaluPopupsIfPresent();
    }

    /**
     * Realiza uma busca por um produto.
     * Tenta clicar no botão de busca; se não for possível, envia a busca com a tecla ENTER.
     * @param product O termo a ser buscado.
     * @return Uma instância de MagaluResultsPage representando a página de resultados da busca.
     */
    public MagaluResultsPage searchFor(String product) {
        wait.until(ExpectedConditions.elementToBeClickable(searchInput));
        type(searchInput, product);

        try {
            // Tenta clicar no botão primeiro, com uma espera curta
            if (isElementReady(searchButton, 5)) {
                click(searchButton);
            } else {
                System.out.println("Botão de busca da Magalu não encontrado/clicável, tentando enviar com ENTER.");
                searchInput.sendKeys(Keys.ENTER);
            }
        } catch (Exception e) {
            System.err.println("Exceção ao tentar clicar no botão de busca ou enviar ENTER na Magalu: " + e.getMessage());
            System.out.println("Tentando enviar com ENTER como fallback.");
            searchInput.sendKeys(Keys.ENTER); // Fallback final
        }
        return new MagaluResultsPage(driver);
    }

    /**
     * Clica no link "Ofertas do Dia" no menu de navegação.
     */
    public void clickOnOfertasDoDia() {
        wait.until(ExpectedConditions.elementToBeClickable(ofertasDoDiaLink));
        click(ofertasDoDiaLink);
    }

    /**
     * Verifica se o elemento identificador da página "Ofertas do Dia" da Magalu está visível.
     * @return true se o identificador estiver visível, false caso contrário.
     */
    public boolean isOfertasDoDiaPageVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOf(ofertasDoDiaPageIdentifier));
            return true;
        } catch (Exception e) {
            System.err.println("Identificador da página de Ofertas do Dia da Magalu não encontrado: " + ofertasDoDiaPageIdentifier.toString());
            return false;
        }
    }

    /**
     * Obtém o texto do título principal da página "Ofertas do Dia" da Magalu.
     * @return O texto do título (esperado "Aproveite!") se a página estiver visível, ou uma mensagem de erro.
     */
    public String getOfertasDoDiaPageTitleText() {
        if(isOfertasDoDiaPageVisible()){
            return ofertasDoDiaPageIdentifier.getText().trim();
        }
        return "Título da página de Ofertas do Dia da Magalu não encontrado.";
    }
}