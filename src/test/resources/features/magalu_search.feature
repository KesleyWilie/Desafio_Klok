# language: pt
@magalu @regression
Funcionalidade: Busca de produtos e navegação na Magazine Luiza

  @busca_existente @smoke
  Cenário: Busca por produto existente na Magazine Luiza
    Dado que estou na página inicial da Magazine Luiza
    Quando eu busco por "iPhone" na Magazine Luiza
    Então devo ver resultados para "iPhone" na Magazine Luiza
    E o primeiro resultado na Magazine Luiza deve conter "iPhone"

  @busca_inexistente
  Cenário: Busca por produto inexistente na Magazine Luiza
    Dado que estou na página inicial da Magazine Luiza
    Quando eu busco por "ProdutoSuperInexistenteKlok123" na Magazine Luiza
    Então devo ver a mensagem "não encontrou resultado algum" na Magazine Luiza para o produto "ProdutoSuperInexistenteKlok123"

  @ofertas_do_dia
  Cenário: Verificar navegação e resultados para Ofertas do Dia na Magazine Luiza
    Dado que estou na página inicial da Magazine Luiza
    Quando eu clico em "Ofertas do Dia" na Magazine Luiza
    Então devo ver a página de "Ofertas do Dia" na Magazine Luiza
    E devo ver resultados para Ofertas do Dia na Magazine Luiza