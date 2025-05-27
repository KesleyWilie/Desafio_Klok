# language: pt
@amazon @regression
Funcionalidade: Busca de produtos e navegação na Amazon

  @ofertas_do_dia
  Cenário: Verificar navegação para Ofertas do Dia na Amazon
    Dado que estou na página inicial da Amazon
    Quando eu clico em "Ofertas do Dia" na Amazon
    Então devo ver a página de "Ofertas do Dia" na Amazon
    E devo ver resultados para Ofertas do Dia na Amazon

  @busca_existente @smoke
  Cenário: Busca por produto existente na Amazon
    Dado que estou na página inicial da Amazon
    Quando eu busco por "Kindle" na Amazon
    Então devo ver resultados para "Kindle" na Amazon
    E o primeiro resultado na Amazon deve conter "Kindle"

  @busca_inexistente
  Cenário: Busca por produto inexistente na Amazon
    Dado que estou na página inicial da Amazon
    Quando eu busco por "ProdutoSuperInexistenteKlok456" na Amazon
    Então devo ver a mensagem "Nenhum resultado para" na Amazon para o produto "ProdutoSuperInexistenteKlok456"