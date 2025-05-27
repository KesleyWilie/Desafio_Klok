package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Page Object para a página de resultados de busca da Amazon.
 * Contém elementos e métodos para interagir com os resultados da busca.
 */
public class AmazonResultsPage extends BasePage {

    /**
     * Lista de WebElements representando os títulos dos produtos nos resultados da busca.
     * Localizador XPath: //div[contains(@data-cel-widget, 'search_result_')]//h2/a/span[normalize-space(.)!='' and not(contains(@class, 'a-offscreen'))]
     */
    @FindBy(xpath = "//div[contains(@data-cel-widget, 'search_result_')]//h2/a/span[normalize-space(.)!='' and not(contains(@class, 'a-offscreen'))]")
    private List<WebElement> productTitles;

    /**
     * Elemento que exibe o termo de busca que foi pesquisado (ex: "Kindle").
     * Localizador CSS: .a-color-state.a-text-bold
     */
    @FindBy(css = ".a-color-state.a-text-bold")
    private WebElement searchTermEcho;

    /**
     * Título "Resultados", presente quando uma busca retorna produtos.
     * Localizador XPath: //h2[normalize-space(.)='Resultados']
     */
    @FindBy(xpath = "//h2[normalize-space(.)='Resultados']")
    private WebElement resultadosTitle;

    /**
     * Mensagem exibida quando a busca não retorna nenhum resultado (ex: "Nenhum resultado para...").
     * O contêiner geral é <div class="s-no-outline">.
     * Localizador XPath: //div[@class='s-no-outline']//span[contains(text(),'Nenhum resultado para')]
     */
    @FindBy(xpath = "//div[@class='s-no-outline']//span[contains(text(),'Nenhum resultado para')]")
    private WebElement noResultsMessage;

    /**
     * Elemento que exibe o termo que foi buscado e não resultou em produtos.
     * Localizador XPath: //div[@class='s-no-outline']//span[contains(text(),'Nenhum resultado para')]/following-sibling::span[1]
     */
    @FindBy(xpath = "//div[@class='s-no-outline']//span[contains(text(),'Nenhum resultado para')]/following-sibling::span[1]")
    private WebElement noResultsSearchedTerm;


    /**
     * Construtor da AmazonResultsPage.
     * Espera até que um dos elementos chave da página de resultados seja visível:
     * - O título "Resultados" (para buscas com resultados).
     * - A mensagem "Nenhum resultado para" (para buscas sem resultados).
     * - A lista de títulos de produtos (como fallback).
     * @param driver A instância do WebDriver a ser usada.
     */
    public AmazonResultsPage(WebDriver driver) {
        super(driver);
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOf(resultadosTitle),      // Para busca com resultados
                    ExpectedConditions.visibilityOf(noResultsMessage),    // Para busca sem resultados
                    (d -> productTitles != null && !productTitles.isEmpty() && isDisplayed(productTitles.get(0))) // Fallback se os títulos acima não forem os primeiros
            ));
        } catch (org.openqa.selenium.TimeoutException e) {
            // Log se nenhum dos elementos esperados principais for encontrado após o timeout
            if (isDisplayed(resultadosTitle) || isDisplayed(noResultsMessage) || (productTitles != null && !productTitles.isEmpty() && isDisplayed(productTitles.get(0)))) {
                System.out.println("Página de resultados Amazon carregada (título 'Resultados', msg 'sem resultados' ou lista de produtos visível).");
            } else {
                System.err.println("Página de resultados Amazon não carregou elementos esperados (títulos, msg 'sem resultados' ou termo de busca): " + e.getMessage());
                // Considerar lançar uma exceção se o estado da página for crítico para o teste
                // throw new IllegalStateException("Página de resultados Amazon não carregou como esperado.", e);
            }
        } catch (Exception e) {
            System.err.println("Exceção inesperada no construtor de AmazonResultsPage: " + e.getMessage());
        }
    }

    /**
     * Verifica se a busca retornou resultados.
     * Considera que há resultados se o título "Resultados" estiver visível e a lista de produtos não estiver vazia.
     * @return true se houver resultados, false caso contrário.
     */
    public boolean hasResults() {
        return isDisplayed(resultadosTitle) &&
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
     * Obtém o título do primeiro produto listado nos resultados.
     * @return O texto do título do primeiro produto, ou uma mensagem de erro/aviso se não houver resultados.
     */
    public String getFirstProductTitle() {
        if (hasResults()) {
            try {
                return productTitles.get(0).getText().trim();
            } catch (Exception e) {
                System.err.println("Erro ao obter título do primeiro produto (Amazon): " + e.getMessage());
                return "Erro ao obter título.";
            }
        }
        return "Nenhum título de produto encontrado.";
    }

    /**
     * Obtém o texto do termo de busca que é ecoado na página (ex: "Kindle" entre aspas).
     * @return O termo de busca ecoado (sem aspas), ou uma mensagem de erro se não encontrado.
     */
    public String getSearchTermEchoText() {
        if (isDisplayed(searchTermEcho)) {
            return searchTermEcho.getText().trim().replace("\"", "");
        }
        return "Termo de busca ecoado não encontrado.";
    }

    /**
     * Verifica se o título "Resultados" está visível na página.
     * @return true se o título "Resultados" estiver visível, false caso contrário.
     */
    public boolean isResultadosTitleVisible() {
        return isDisplayed(resultadosTitle);
    }

    /**
     * Obtém o texto da mensagem exibida quando não há resultados para a busca (ex: "Nenhum resultado para").
     * @return O texto da mensagem, ou uma mensagem de erro se não encontrada.
     */
    public String getNoResultsMessageText() {
        if (isDisplayed(noResultsMessage)) {
            return noResultsMessage.getText().trim(); // Retorna "Nenhum resultado para"
        }
        return "Mensagem 'Nenhum resultado para' não encontrada.";
    }

    /**
     * Obtém o termo que foi buscado e exibido junto à mensagem de "nenhum resultado".
     * @return O termo buscado (ex: "produtoinexistenteklok456"), ou uma mensagem de erro se não encontrado.
     */
    public String getNoResultsSearchedTermText() {
        if (isDisplayed(noResultsSearchedTerm)) {
            return noResultsSearchedTerm.getText().trim();
        }
        return "Termo buscado na mensagem de 'Nenhum resultado' não encontrado.";
    }
}