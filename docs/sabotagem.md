# Teste de sabotagem

| Sabotagem | Como foi provocada | O que apareceu na tela |
|---|---|---|
| Lista vazia | Apagar todos os produtos cadastrados | Apareceu o aviso de "nenhum produto cadastrado" com o botão de cadastrar novo produto |
| Erro de leitura | Forçar temporariamente uma IOException na operação de leitura (`observarItens()` em `ProdutoRepository.kt`) | Apareceu a mensagem "Sem conexão. Verifique a internet e tente novamente." com o botão "Tentar de novo" |
| Carregando visível | Adicionar temporariamente `delay(2000)` no carregamento (`carregar()` em `ProdutoViewModel.kt`) | O indicador de carregamento girando ficou visível por 2 segundos antes da lista aparecer |
| Campo obrigatório vazio | Tentar salvar o cadastro de produto sem preencher um campo obrigatório | Apareceu o aviso "Um campo está vazio. Preencha todos para continuar.", sem travar ou fechar o app |
| Erro genérico | Forçar temporariamente uma Exception genérica no repositório (`observarItens()` em `ProdutoRepository.kt`) | Apareceu a mensagem "Não foi possível carregar os dados." com o botão "Tentar de novo" |
