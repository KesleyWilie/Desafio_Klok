package steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import org.testng.Assert;
import pages.MagaluHomePage;
import pages.MagaluResultsPage;
// import org.openqa.selenium.By; // Removido se não usado diretamente aqui
// import org.openqa.selenium.WebElement; // Removido se não usado diretamente aqui
// import java.util.List; // Removido se não usado diretamente aqui

/**
 * Step Definitions para os cenários de teste da Magazine Luiza.
 */
public class MagaluSteps {

    private MagaluHomePage magaluHomePage;
    private MagaluResultsPage magaluResultsPage;

    /**
     * Navega para a página inicial da Magazine Luiza.
     */
    @Dado("que estou na página inicial da Magazine Luiza")
    public void queEstouNaPaginaInicialDaMagazineLuiza() {
        magaluHomePage = new MagaluHomePage(Hooks.driver);
        magaluHomePage.navigateToHomePage();
        Assert.assertTrue(magaluHomePage.getCurrentUrl().contains("magazineluiza.com.br"),
                "Não navegou para a página correta da Magazine Luiza. URL atual: " + magaluHomePage.getCurrentUrl());
    }

    /**
     * Realiza uma busca por um produto específico na Magazine Luiza.
     * @param produto O nome do produto a ser buscado.
     */
    @Quando("eu busco por {string} na Magazine Luiza")
    public void euBuscoPorNaMagazineLuiza(String produto) {
        Assert.assertNotNull(magaluHomePage, "Página inicial da Magazine Luiza não foi inicializada.");
        magaluResultsPage = magaluHomePage.searchFor(produto);
    }

    /**
     * Verifica se a página de resultados da Magazine Luiza exibe resultados para o produto buscado.
     * @param produto O nome do produto que foi buscado (usado para mensagem de erro).
     */
    @Então("devo ver resultados para {string} na Magazine Luiza")
    public void devoVerResultadosParaNaMagazineLuiza(String produto) {
        Assert.assertNotNull(magaluResultsPage, "Página de resultados da Magazine Luiza não foi inicializada.");
        Assert.assertTrue(magaluResultsPage.isSearchResultsTitleVisible(), "Título 'Resultados para' não visível na página de busca.");
        Assert.assertTrue(magaluResultsPage.hasResults(), "Nenhum resultado encontrado para '" + produto + "' na Magazine Luiza.");
        // A validação do termo buscado (ex: "iPhone") para além do título "Resultados para"
        // dependeria de um elemento específico na página de resultados da Magalu que ecoe o termo buscado.
        // Se tal elemento existir, um método como `getActualSearchedTermText()` poderia ser implementado na MagaluResultsPage.
    }

    /**
     * Verifica se o título do primeiro produto nos resultados da Magazine Luiza contém um termo esperado.
     * @param termoEsperado O termo que deve estar presente no título do primeiro produto.
     */
    @Então("o primeiro resultado na Magazine Luiza deve conter {string}")
    public void oPrimeiroResultadoNaMagazineLuizaDeveConter(String termoEsperado) {
        Assert.assertNotNull(magaluResultsPage, "Página de resultados da Magazine Luiza não foi inicializada.");
        Assert.assertTrue(magaluResultsPage.hasResults(), "Não há resultados para verificar o primeiro produto na Magazine Luiza.");
        String primeiroTitulo = magaluResultsPage.getFirstProductTitle();
        Assert.assertTrue(primeiroTitulo.toLowerCase().contains(termoEsperado.toLowerCase()),
                "O primeiro resultado ('" + primeiroTitulo + "') na Magazine Luiza não contém o termo esperado '" + termoEsperado + "'.");
    }

    /**
     * Clica em uma opção específica na página da Magazine Luiza.
     * @param opcao O nome da opção a ser clicada (ex: "Ofertas do Dia").
     */
    @Quando("eu clico em {string} na Magazine Luiza")
    public void euClicoEmNaMagazineLuiza(String opcao) {
        Assert.assertNotNull(magaluHomePage, "Página inicial da Magazine Luiza não foi inicializada.");
        if (opcao.equalsIgnoreCase("Ofertas do Dia")) {
            magaluHomePage.clickOnOfertasDoDia();
        } else {
            throw new IllegalArgumentException("Opção de clique não reconhecida na Magazine Luiza: " + opcao);
        }
    }

    /**
     * Verifica se a página correta foi carregada após uma ação de navegação na Magazine Luiza.
     * @param nomePagina O nome da página esperada (ex: "Ofertas do Dia").
     */
    @Então("devo ver a página de {string} na Magazine Luiza")
    public void devoVerAPaginaDeNaMagazineLuiza(String nomePagina) {
        Assert.assertNotNull(magaluHomePage, "Página inicial da Magalu não foi inicializada.");
        if (nomePagina.equalsIgnoreCase("Ofertas do Dia")) {
            Assert.assertTrue(magaluHomePage.isOfertasDoDiaPageVisible(),
                    "Não está na página de 'Ofertas do Dia' da Magalu ou o identificador da página não foi encontrado.");
            String expectedTitleText = "Aproveite!"; // Título H1 da página de Ofertas do Dia da Magalu
            String actualTitleText = magaluHomePage.getOfertasDoDiaPageTitleText();
            Assert.assertEquals(actualTitleText, expectedTitleText,
                    "O título da página de Ofertas do Dia da Magalu não é o esperado.");
        } else {
            throw new IllegalArgumentException("Nome de página não reconhecido para verificação na Magalu: " + nomePagina);
        }
    }

    /**
     * Verifica se há itens de oferta listados na página "Ofertas do Dia" da Magazine Luiza.
     * Reutiliza MagaluResultsPage, assumindo que a estrutura de listagem de produtos é similar.
     */
    @Então("devo ver resultados para Ofertas do Dia na Magazine Luiza")
    public void devoVerResultadosParaOfertasDoDiaNaMagazineLuiza() {
        // Instancia MagaluResultsPage para a página de Ofertas do Dia,
        // assumindo que ela tem uma estrutura de resultados que pode ser lida por esta Page Object.
        // Se a estrutura for muito diferente, uma Page Object específica para Ofertas do Dia Magalu seria melhor.
        if (magaluResultsPage == null) { // Garante que magaluResultsPage seja instanciada se não foi por uma busca.
            magaluResultsPage = new MagaluResultsPage(Hooks.driver);
        }
        Assert.assertNotNull(magaluResultsPage, "Página de resultados da Magazine Luiza (Ofertas do Dia) não foi inicializada.");
        Assert.assertTrue(magaluResultsPage.resultsCount() > 0, "Nenhum resultado (item de oferta) encontrado na página de Ofertas do Dia da Magalu.");
    }

    /**
     * Verifica se a quantidade de resultados na Magazine Luiza é maior que um valor especificado.
     * @param quantidade A quantidade mínima de resultados esperada (exclusive).
     */
    @Então("devo ver mais de {int} resultados na Magazine Luiza")
    public void devoVerMaisDeResultadosNaMagazineLuiza(int quantidade) {
        Assert.assertNotNull(magaluResultsPage, "Página de resultados da Magazine Luiza não foi inicializada.");
        int actualCount = magaluResultsPage.resultsCount();
        Assert.assertTrue(actualCount > quantidade,
                "Esperado mais de " + quantidade + " resultados, mas foram encontrados " + actualCount + ".");
    }

    /**
     * Verifica se a mensagem correta é exibida na Magazine Luiza quando uma busca não retorna resultados.
     * @param parteDaMensagemEsperada Parte da mensagem esperada (ex: "não encontrou resultado algum").
     * @param produtoInexistente O nome do produto inexistente que foi buscado.
     */
    @Então("devo ver a mensagem {string} na Magazine Luiza para o produto {string}")
    public void devoVerAMensagemNaMagazineLuizaParaOProduto(String parteDaMensagemEsperada, String produtoInexistente) {
        Assert.assertNotNull(magaluResultsPage, "Página de resultados da Magazine Luiza não foi inicializada.");
        String mensagemCompletaAtual = magaluResultsPage.getNoResultsMessageText();

        Assert.assertTrue(mensagemCompletaAtual.toLowerCase().contains(parteDaMensagemEsperada.toLowerCase()),
                "A mensagem de 'nenhum resultado' não contém o trecho esperado. Esperado contendo: '" + parteDaMensagemEsperada + "', mas foi: '" + mensagemCompletaAtual + "'");

        Assert.assertTrue(mensagemCompletaAtual.toLowerCase().contains("\"" + produtoInexistente.toLowerCase() + "\""),
                "A mensagem de 'nenhum resultado' não contém o produto buscado ('" + produtoInexistente + "'). Mensagem completa: '" + mensagemCompletaAtual + "'");
    }
}