# Desafio Técnico Klok - Automação Web e Consulta SQL

Este projeto contém a solução para o desafio técnico proposto pela Klok, englobando automação de testes web e uma consulta SQL.

## 📝 Descrição do Desafio

O desafio consiste em duas partes principais:

1.  **Automação Web:**
    *   Criar um teste automatizado que realize um fluxo de ponta a ponta em um grande portal de comércio online (ex: Americanas, Submarino, Magazine Luiza).
    *   O fluxo deve incluir:
        1.  Acessar o portal.
        2.  Buscar por um produto.
        3.  Validar o retorno da busca.
    *   Considerar cenários de sucesso e fluxos alternativos.
    *   Utilizar boas práticas de organização de código e interação com elementos da aplicação.
    *   Publicar o código e cenários no GitHub.

2.  **Consulta SQL:**
    *   Dada uma seguradora "SeguraTudo" com tabelas `clientes` e `apólices`.
    *   Escrever uma consulta SQL para retornar os detalhes das apólices de seguro ativas, incluindo nome do cliente, número da apólice, tipo de seguro e valor do prêmio.
    *   A consulta deve ser colocada em um arquivo `.txt` na raiz do projeto de automação.

## ✨ Funcionalidades Implementadas (Automação Web)

*   **Portais Automatizados:**
    *   Amazon (`amazon.com.br`)
    *   Magazine Luiza (`magazineluiza.com.br`)
*   **Cenários de Teste:**
    *   **Busca por produto existente:**
        *   Navega até a home page.
        *   Busca por um produto conhecido (ex: "Kindle" na Amazon, "iPhone" na Magalu).
        *   Valida se a página de resultados é exibida.
        *   Valida se há resultados para o termo buscado.
        *   Valida se o primeiro resultado contém o termo buscado.
    *   **Busca por produto inexistente:**
        *   Navega até a home page.
        *   Busca por um produto fictício (ex: "ProdutoSuperInexistenteKlok456").
        *   Valida se a mensagem apropriada de "nenhum resultado encontrado" é exibida, contendo o termo buscado.
    *   **Navegação para "Ofertas do Dia":**
        *   Navega até a home page.
        *   Clica no link/botão "Ofertas do Dia".
        *   Valida se a página de "Ofertas do Dia" é carregada corretamente.
        *   Valida se há produtos/ofertas listados na página de "Ofertas do Dia".

## 🛠️ Tecnologias Utilizadas

*   **Linguagem:** Java (JDK 11+)
*   **Automação Web:** Selenium WebDriver
*   **Framework de Teste BDD:** Cucumber
*   **Test Runner:** TestNG
*   **Gerenciador de Dependências e Build:** Apache Maven
*   **Gerenciador de Drivers do Navegador:** WebDriverManager

## 📂 Estrutura do Projeto
```.
├── src/
│ ├── test/
│ │ ├── java/
│ │ │ ├── pages/ # Page Objects (AmazonHomePage, MagaluResultsPage, etc.)
│ │ │ ├── steps/ # Step Definitions (AmazonSteps, MagaluSteps, Hooks)
│ │ │ ├── runners/ # Test Runner (TestRunner.java)
│ │ │ └── utils/ # Utilitários (ConfigReader.java)
│ │ └── resources/
│ │ ├── features/ # Arquivos .feature do Cucumber (Amazon.feature, Magalu.feature)
│ │ └── config.properties # Arquivo de configuração (URLs, browser, timeouts)
├── pom.xml # Arquivo de configuração do Maven
├── sql_query.txt # Consulta SQL para o desafio
└── README.md # Este arquivo
```
## ⚙️ Configuração e Pré-requisitos

1.  **Java Development Kit (JDK):** Versão 11 ou superior instalada.
2.  **Apache Maven:** Instalado e configurado no PATH do sistema.
3.  **Git:** Para clonar o repositório.
4.  **Navegador Web:** Google Chrome ou Mozilla Firefox instalado (o WebDriverManager cuidará do download do driver correspondente).

## 🚀 Executando os Testes

1.  **Clone o repositório:**
    ```bash
    git clone <https://github.com/KesleyWilie/Desafio_Klok>
    cd <nome-do-diretorio-do-projeto>
    ```

2.  **Execute os testes via Maven:**
    *   Para executar todos os testes (tags `@regression` ou `@magalu` ou `@amazon`):
        ```bash
        mvn clean test
        ```
    *   Para executar testes com uma tag específica (ex: `@smoke`):
        ```bash
        mvn clean test -Dcucumber.filter.tags="@smoke"
        ```
    *   Para executar testes em um navegador específico (ex: `firefox`) e/ou em modo headless:
        ```bash
        mvn clean test -Dbrowser=firefox -Dheadless=true
        ```
        (Opções para `-Dbrowser`: `chrome`, `firefox`. Opções para `-Dheadless`: `true`, `false`)

## 📊 Relatórios de Teste

Após a execução dos testes localmente, os relatórios do Cucumber podem ser encontrados no seguinte diretório dentro do projeto:

*   **Caminho Local:** `target/cucumber-reports/cucumber-html-report.html`

Para visualizar o relatório:
1.  Navegue até o diretório `target/cucumber-reports/` no seu explorador de arquivos.
2.  Abra o arquivo `cucumber-html-report.html` em qualquer navegador web (Google Chrome, Firefox, etc.).

Este relatório HTML fornece uma visão detalhada dos resultados dos testes, incluindo:
*   Status de cada cenário (Passou/Falhou).
*   Passos executados em cada cenário.
*   Mensagens de erro e stack traces para cenários que falharam.
*   Screenshots anexados em caso de falha (conforme configurado nos Hooks).

*Se este projeto estivesse configurado com Integração Contínua (CI) que armazena artefatos (como GitHub Actions), um link para o relatório online poderia ser disponibilizado aqui após cada execução bem-sucedida do pipeline. Por exemplo: `[Ver último relatório de teste](LINK_PARA_O_ARTEFATO_DO_RELATORIO_NA_CI)`.*


## 🔧 Configurações do Projeto (`config.properties`)

O arquivo `src/test/resources/config.properties` contém configurações globais para os testes:

*   `magalu.url`: URL da Magazine Luiza.
*   `amazon.url`: URL da Amazon Brasil.
*   `browser`: Navegador padrão para os testes (`chrome` ou `firefox`). Pode ser sobrescrito via linha de comando.
*   `timeout.seconds`: Timeout padrão em segundos para esperas explícitas.
*   `headless`: Define se os testes rodam em modo headless (`true` ou `false`). Pode ser sobrescrito via linha de comando.

## 📄 Consulta SQL (`sql_query.txt`)

A solução para o desafio de consulta SQL da seguradora "SeguraTudo" encontra-se no arquivo `sql_query.txt` na raiz deste projeto.

**Pergunta:** Quais são os detalhes das apólices de seguro de todos os clientes da "SeguraTudo" que possuem apólices de seguro ativas, incluindo o nome do cliente, número da apólice, tipo de seguro e valor do prêmio?

**Resposta:** Contida em `sql_query.txt`.

## 📈 Melhorias Futuras

*   **Integração Contínua (CI/CD):** Implementar um pipeline de CI/CD (ex: GitHub Actions, Jenkins) para automatizar a execução dos testes em cada push ou pull request. Isso permitiria a execução automática dos testes e o armazenamento/publicação dos relatórios de teste como artefatos do build, facilitando o acesso aos resultados mais recentes.
*   **Paralelização Avançada:** Configurar a execução paralela de testes em nível de feature ou cenário de forma mais granular para otimizar o tempo de execução em suítes maiores.
*   **Framework de Logging:** Integrar um framework de logging mais robusto (ex: Log4j2, SLF4j) para um melhor rastreamento e depuração.
*   **Relatórios Mais Detalhados:** Explorar plugins de relatório adicionais para o Cucumber ou TestNG que ofereçam visualizações e métricas mais ricas, possivelmente com históricos de execução.
*   **Cobertura de Mais Portais:** Expandir a automação para cobrir outros portais de e-commerce sugeridos (Ex: Americanas, Submarino).
*   **Diversificação de Cenários:** Adicionar mais cenários de teste, incluindo fluxos de carrinho de compras, checkout (mockado, se necessário), e validações de diferentes tipos de produtos.
*   **Gerenciamento de Dados de Teste:** Implementar estratégias para gerenciar dados de teste de forma mais eficaz, especialmente para cenários que requerem dados específicos (ex: leitura de arquivos CSV, Excel, ou um pequeno banco de dados de teste).
*   **Containerização:** Utilizar Docker para executar os testes em ambientes isolados e consistentes, facilitando a configuração e a execução em diferentes máquinas e no pipeline de CI/CD.

---
**Autor:** Kesley Santos
**Data:** 26/05.2025
