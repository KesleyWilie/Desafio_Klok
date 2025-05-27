package steps;

import io.cucumber.java.pt.*;
import org.testng.Assert;
import pages.AmazonHomePage;
import pages.AmazonResultsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;

/**
 * Step Definitions para os cenários de teste da Amazon.
 */
public class AmazonSteps {

    private AmazonHomePage amazonHomePage;
    private AmazonResultsPage amazonResultsPage;

    /**
     * Navega para a página inicial da Amazon.
     */
    @Dado("que estou na página inicial da Amazon")
    public void queEstouNaPaginaInicialDaAmazon() {
        amazonHomePage = new AmazonHomePage(Hooks.driver);
        amazonHomePage.navigateToHomePage();
        Assert.assertTrue(amazonHomePage.getCurrentUrl().contains("amazon.com.br"),
                "Não navegou para a página correta da Amazon. URL atual: " + amazonHomePage.getCurrentUrl());
    }

    /**
     * Realiza uma busca por um produto específico na Amazon.
     * @param produto O nome do produto a ser buscado.
     */
    @Quando("eu busco por {string} na Amazon")
    public void euBuscoPorNaAmazon(String produto) {
        Assert.assertNotNull(amazonHomePage, "Página inicial da Amazon não foi inicializada.");
        amazonResultsPage = amazonHomePage.searchFor(produto);
    }

    /**
     * Verifica se a página de resultados da Amazon exibe resultados para o produto buscado.
     * @param produto O nome do produto que foi buscado.
     */
    @Então("devo ver resultados para {string} na Amazon")
    public void devoVerResultadosParaNaAmazon(String produto) {
        Assert.assertNotNull(amazonResultsPage, "Página de resultados da Amazon não foi inicializada.");
        Assert.assertTrue(amazonResultsPage.isResultadosTitleVisible(), "Título 'Resultados' não visível na página de busca.");
        Assert.assertTrue(amazonResultsPage.hasResults(), "Nenhum resultado encontrado para '" + produto + "' na Amazon.");

        String searchTermDisplayed = amazonResultsPage.getSearchTermEchoText();
        Assert.assertTrue(searchTermDisplayed.toLowerCase().contains(produto.toLowerCase()),
                "O termo de busca ecoado ('" + searchTermDisplayed + "') não corresponde a '" + produto + "'.");
    }

    /**
     * Clica em uma opção específica na página da Amazon.
     * @param opcao O nome da opção a ser clicada (ex: "Ofertas do Dia").
     */
    @Quando("eu clico em {string} na Amazon")
    public void euClicoEmNaAmazon(String opcao) {
        Assert.assertNotNull(amazonHomePage, "Página inicial da Amazon não foi inicializada.");
        if (opcao.equalsIgnoreCase("Ofertas do Dia")) {
            amazonHomePage.clickOnOfertasDoDia();
        } else {
            throw new IllegalArgumentException("Opção de clique não reconhecida na Amazon: " + opcao);
        }
    }

    /**
     * Verifica se a página correta foi carregada após uma ação de navegação na Amazon.
     * @param nomePagina O nome da página esperada (ex: "Ofertas do Dia").
     */
    @Então("devo ver a página de {string} na Amazon")
    public void devoVerAPaginaDeNaAmazon(String nomePagina) {
        Assert.assertNotNull(amazonHomePage, "Página inicial da Amazon não foi inicializada.");
        if (nomePagina.equalsIgnoreCase("Ofertas do Dia")) {
            Assert.assertTrue(amazonHomePage.isOfertasDoDiaPageVisible(),
                    "Não está na página de 'Ofertas do Dia' da Amazon ou o identificador da página não foi encontrado.");
            String expectedTitleText = "Ofertas e Promoções";
            String actualTitleText = amazonHomePage.getOfertasDoDiaPageTitleText();
            Assert.assertEquals(actualTitleText, expectedTitleText,
                    "O título da página de Ofertas do Dia não é o esperado.");
        } else {
            throw new IllegalArgumentException("Nome de página não reconhecido para verificação na Amazon: " + nomePagina);
        }
    }

    /**
     * Verifica se há itens de oferta listados na página "Ofertas do Dia" da Amazon.
     * Este método utiliza um seletor XPath específico para os itens de oferta.
     * Seria ideal mover este seletor para a Page Object correspondente em uma refatoração futura.
     */
    @Então("devo ver resultados para Ofertas do Dia na Amazon")
    public void devoVerResultadosParaOfertasDoDiaNaAmazon() {
        // O seletor abaixo é específico para identificar os contêineres de ofertas na página de "Ofertas do Dia".
        // Idealmente, a lógica de encontrar esses itens e verificar sua presença/quantidade
        // estaria encapsulada em um método na AmazonHomePage ou uma Page Object específica para Ofertas do Dia.
        String seletorItensDeOferta = "//*[@id=\"DealsGridScrollAnchor\"]/div[3]/div/div/div[2]/div[1]/div/div";

        List<WebElement> itensDeOferta = Hooks.driver.findElements(By.xpath(seletorItensDeOferta));

        Assert.assertFalse(itensDeOferta.isEmpty(), "Nenhum item de oferta encontrado na página de Ofertas do Dia da Amazon usando o seletor: " + seletorItensDeOferta);
        System.out.println("Encontrados " + itensDeOferta.size() + " itens de oferta na página de Ofertas do Dia da Amazon.");
    }

    /**
     * Verifica se a mensagem correta é exibida na Amazon quando uma busca não retorna resultados.
     * @param mensagemEsperada Parte da mensagem esperada (ex: "Nenhum resultado para").
     * @param produtoInexistente O nome do produto inexistente que foi buscado.
     */
    @Então("devo ver a mensagem {string} na Amazon para o produto {string}")
    public void devoVerAMensagemNaAmazonParaOProduto(String mensagemEsperada, String produtoInexistente) {
        Assert.assertNotNull(amazonResultsPage, "Página de resultados da Amazon não foi inicializada.");
        String mensagemPrincipal = amazonResultsPage.getNoResultsMessageText();
        String termoBuscado = amazonResultsPage.getNoResultsSearchedTermText();

        Assert.assertTrue(mensagemPrincipal.toLowerCase().contains(mensagemEsperada.toLowerCase()),
                "Parte principal da mensagem de 'nenhum resultado' incorreta. Esperado contendo: '" + mensagemEsperada + "', mas foi: '" + mensagemPrincipal + "'");
        Assert.assertEquals(termoBuscado.toLowerCase(), produtoInexistente.toLowerCase(),
                "O termo buscado na mensagem de 'nenhum resultado' está incorreto.");
    }

    /**
     * Verifica se o título do primeiro produto nos resultados da Amazon contém um termo esperado.
     * @param termoEsperado O termo que deve estar presente no título do primeiro produto.
     */
    @Então("o primeiro resultado na Amazon deve conter {string}")
    public void oPrimeiroResultadoNaAmazonDeveConter(String termoEsperado) {
        Assert.assertNotNull(amazonResultsPage, "Página de resultados da Amazon não foi inicializada.");
        Assert.assertTrue(amazonResultsPage.hasResults(), "Não há resultados para verificar o primeiro produto.");
        String primeiroTitulo = amazonResultsPage.getFirstProductTitle();
        Assert.assertTrue(primeiroTitulo.toLowerCase().contains(termoEsperado.toLowerCase()),
                "O primeiro resultado ('" + primeiroTitulo + "') não contém o termo esperado '" + termoEsperado + "'.");
    }
}