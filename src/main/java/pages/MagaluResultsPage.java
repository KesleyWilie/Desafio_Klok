package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Page Object para a página de resultados de busca da Magazine Luiza.
 * Contém elementos e métodos para interagir com os resultados da busca.
 */
public class MagaluResultsPage extends BasePage {

    /**
     * Lista de WebElements representando os títulos dos produtos nos resultados da busca.
     * Localizador CSS: [data-testid='product-card-content'] [data-testid='product-title']
     */
    @FindBy(css = "[data-testid='product-card-content'] [data-testid='product-title']")
    private List<WebElement> productTitles;

    /**
     * Mensagem exibida quando a busca não retorna nenhum resultado (ex: "Sua busca por "..." não encontrou resultado algum :(").
     * Localizador CSS: h1[data-testid='text-list-title']
     */
    @FindBy(css = "h1[data-testid='text-list-title']")
    private WebElement emptyResultsMessage;

    /**
     * Título "Resultados para ", presente quando uma busca retorna produtos.
     * Localizador CSS: span[data-testid='main-title'][title='Resultados para ']
     */
    @FindBy(css = "span[data-testid='main-title'][title='Resultados para ']")
    private WebElement searchResultsTitle;


    /**
     * Construtor da MagaluResultsPage.
     * Espera até que um dos elementos chave da página de resultados seja visível:
     * - O título "Resultados para " (para buscas com resultados).
     * - A mensagem de "nenhum resultado" (para buscas sem resultados).
     * - A lista de títulos de produtos (como fallback).
     * @param driver A instância do WebDriver a ser usada.
     */
    public MagaluResultsPage(WebDriver driver) {
        super(driver);
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOf(searchResultsTitle), // Para busca com resultados
                    ExpectedConditions.visibilityOf(emptyResultsMessage),  // Para busca sem resultados
                    (d -> productTitles != null && !productTitles.isEmpty() && isDisplayed(productTitles.get(0))) // Fallback
            ));
        } catch (org.openqa.selenium.TimeoutException e) {
            if (isDisplayed(searchResultsTitle) || isDisplayed(emptyResultsMessage) || (productTitles != null && !productTitles.isEmpty() && isDisplayed(productTitles.get(0)))) {
                System.out.println("Página de resultados Magalu carregada.");
            } else {
                System.err.println("Página de resultados Magalu não carregou elementos esperados (título 'Resultados para', msg 'sem resultados' ou lista de produtos): " + e.getMessage());
                // Considerar lançar uma exceção se o estado da página for crítico para o teste
                // throw new IllegalStateException("Página de resultados Magalu não carregou como esperado.", e);
            }
        } catch (Exception e) {
            System.err.println("Exceção inesperada no construtor de MagaluResultsPage: " + e.getMessage());
        }
    }

    /**
     * Verifica se a busca retornou resultados.
     * Considera que há resultados se o título "Resultados para " estiver visível e a lista de produtos não estiver vazia.
     * @return true se houver resultados, false caso contrário.
     */
    public boolean hasResults() {
        return isDisplayed(searchResultsTitle) &&
                productTitles != null &&
                !productTitles.isEmpty() &&
                isDisplayed(productTitles.get(0));
    }

    /**
     * Retorna a quantidade de produtos listados na página de resultados.
     * @return O número de produtos, ou 0 se não houver resultados.
     */
    public int resultsCount() {
        if (hasResults()) {
            return productTitles.size();
        }
        return 0;
    }

    /**
     * Obtém o texto da mensagem exibida quando não há resultados para a busca.
     * @return O texto completo da mensagem (ex: "Sua busca por "..." não encontrou resultado algum :("),
     *         ou uma mensagem de erro se não encontrada.
     */
    public String getNoResultsMessageText() {
        if (isDisplayed(emptyResultsMessage)) {
            return emptyResultsMessage.getText().trim();
        }
        return "Mensagem de 'Nenhum resultado encontrado' não achada.";
    }

    /**
     * Obtém o texto do título "Resultados para ".
     * @return O texto do título, ou uma mensagem de erro se não encontrado.
     */
    public String getSearchResultsTitleText() {
        if (isDisplayed(searchResultsTitle)) {
            return searchResultsTitle.getText().trim(); // Retorna "Resultados para "
        }
        return "Título 'Resultados para' não encontrado.";
    }

    /**
     * Verifica se o título "Resultados para " está visível na página.
     * @return true se o título estiver visível, false caso contrário.
     */
    public boolean isSearchResultsTitleVisible(){
        return isDisplayed(searchResultsTitle);
    }

    /**
     * Obtém o título do primeiro produto listado nos resultados.
     * @return O texto do título do primeiro produto, ou uma mensagem de erro/aviso se não houver resultados.
     */
    public String getFirstProductTitle() {
        if (hasResults()) {
            try {
                return productTitles.get(0).getText().trim();
            } catch (Exception e) {
                System.err.println("Erro ao obter título do primeiro produto (Magalu): " + e.getMessage());
                return "Erro ao obter título.";
            }
        }
        return "Nenhum título de produto encontrado.";
    }
}