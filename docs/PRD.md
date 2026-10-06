# 📄 PRD — Documento de Requisitos do Produto

| | |
| :---- | :---- |
| **App** | Atrás do Balcão |
| **Grupo** | Kaigang |
| **Autores** | Mariah, Pedro, Ian, Heitor, Gabi |
| **Versão do documento** | 2.0 |
| **Última atualização** | 06/10/2026 |
| **Status** | ( ) Rascunho (X) Em revisão ( ) Aprovado |

> A visão do produto, o problema, o público-alvo, os objetivos de negócio e os riscos já estão descritos em [`CANVAS.md`](./CANVAS.md) — este documento não repete esse conteúdo, só detalha como ele vira requisito técnico.

---

## 1. Escopo desta versão (v1.0)

Com base no Canvas, esta versão cobre o controle de estoque dos produtos. **Não há cálculo de margem de lucro, preço de venda ou qualquer funcionalidade financeira de lucro** — o app registra o que foi investido (custo) e a quantidade em estoque, nada além disso.

**Fora do escopo** (além do que já está no Canvas): cálculo de margem de lucro, preço de venda, relatório financeiro de lucro.

---

## 2. Requisitos funcionais

| ID | Função | História de usuário | Critério de aceite | Prioridade |
| :---- | :---- | :---- | :---- | :---- |
| RF01 | Listar produtos | Como *lojista*, quero ver a lista de produtos cadastrados para acompanhar meu estoque. | Ao abrir o app, a lista aparece com nome, categoria e quantidade de cada produto; se não houver nenhum, aparece "Nenhum produto cadastrado ainda." | Must |
| RF02 | Cadastrar produto | Como *lojista*, quero cadastrar um novo produto para registrá-lo no estoque. | Ao tocar em "+", preencher nome, categoria, quantidade e preço de custo e confirmar, o produto aparece na lista. | Must |
| RF03 | Editar produto | Como *lojista*, quero editar um produto existente para corrigir suas informações. | Ao tocar em um produto, alterar os campos e confirmar, as mudanças são salvas e refletidas na lista. | Must |
| RF04 | Excluir produto | Como *lojista*, quero excluir um produto que não vendo mais. | Ao tocar em excluir e confirmar, o item desaparece da lista imediatamente. | Must |
| RF05 | Gerar relatório de estoque | Como *lojista*, quero ver o total investido no meu estoque atual. | O app soma (preço de custo × quantidade) de todos os produtos ativos e exibe o total na tela de relatório. | Should |
| RF06 | Buscar produto | Como *lojista*, quero buscar um produto pelo nome para encontrá-lo rápido numa lista grande. | Ao digitar no campo de busca, a lista é filtrada em tempo real pelos produtos cujo nome contém o texto digitado. | Could |

---

## 3. Requisitos não funcionais

| ID | Requisito | Como será verificado |
| :---- | :---- | :---- |
| RNF01 | O app não pode fechar sozinho durante o uso normal | 5 minutos de uso contínuo sem crash, em 2 celulares diferentes |
| RNF02 | Toda operação que pode falhar está dentro de `try/catch` | Revisão do código: banco e entradas do usuário |
| RNF03 | Nenhuma falha mostra tela branca ou fecha o app — sempre há mensagem ao usuário | Testes de falha da seção 6 |
| RNF04 | O app roda a partir do Android 7.0 (minSdk 24) | Instalação em dispositivo real |
| RNF05 | Textos visíveis ficam em `strings.xml`, não escritos direto no código | Revisão do código |
| RNF06 | Todo arquivo do pacote do app tem comentário de fronteira escrito pelo grupo | Revisão do código |
| RNF07 | O app funciona sem conexão com a internet, já que os dados ficam salvos localmente (Room) | Testar com o celular em modo avião |

---

## 4. Telas e navegação

**Mapa de navegação:**

```
[Tela Principal — lista de produtos]
      │
      ├── toca no "+"      → [Tela de Cadastro de Produto]
      ├── toca em um item  → [Tela de Detalhe/Edição]
      ├── toca em "Relatório" → [Tela de Relatório de Estoque]
      └── (estado vazio)   → mensagem "Nenhum produto cadastrado ainda."
```

| Tela | O que mostra | Ações disponíveis |
| :---- | :---- | :---- |
| Principal | Lista de produtos (nome, categoria, quantidade) + busca | Adicionar produto, tocar em um item para editar, excluir, abrir relatório |
| Detalhe/Cadastro | Nome, categoria, quantidade, preço de custo | Salvar, excluir, cancelar |
| Relatório de estoque | Total investido (soma de custo × quantidade) | Voltar |

**Rascunhos das telas:** `docs/telas/01-principal.png`, `docs/telas/02-cadastro.png`, `docs/telas/03-relatorio.png`

---

## 5. Dados

**Entidade principal:** `Produto`

| Campo | Tipo | Obrigatório | Observação |
| :---- | :---- | :---- | :---- |
| `id` | Long | sim | chave primária, autogerada |
| `nome` | String | sim | nome do produto |
| `categoria` | String | sim | categoria do produto |
| `quantidadeEstoque` | Int | sim | quantidade em estoque |
| `precoCusto` | Double | sim | usado no relatório de total investido |
| `ativo` | Boolean | sim | controla se o produto ainda está em uso (padrão: true) |

> ⚠️ **Atenção, grupo:** o `model/Produto.kt` já implementado por enquanto ainda tem os campos `precoVenda` e `margemLucro` — como ficou definido que não vamos ter nada de cálculo de lucro, isso precisa ser removido do código (e das telas que já usam esses campos) numa próxima branch, pra bater com este PRD.

**Operações necessárias:** inserir, listar, atualizar, excluir, somar total investido

---

## 6. Tratamento de erros

| Situação de falha | O que o app faz | Mensagem para o usuário |
| :---- | :---- | :---- |
| Campo obrigatório em branco | Impede o salvamento e destaca o campo vazio | "Um campo está vazio. Preencha todos para continuar." |
| Lista vazia (nenhum produto ainda) | Mostra estado vazio com botão para cadastrar o primeiro produto | "Nenhum produto cadastrado ainda. Toque em '+' para começar." |
| Erro ao salvar no banco | Mantém os dados digitados na tela e mostra aviso | "Não foi possível salvar. Tente novamente." |
| Erro ao excluir | Mantém o item na lista e mostra aviso | "Não foi possível excluir. Tente novamente." |

---

## 7. Arquitetura e tecnologias

| Item | Escolha |
| :---- | :---- |
| Linguagem | Kotlin |
| Interface | Jetpack Compose |
| Persistência | Room |
| `minSdk` / `targetSdk` | 24 / 34 |
| Pacote | `com.example.portrasdobalcao` *(confirmado no código já implementado — diverge do `br.edu.ifpe.atrasdobalcao` do Canvas; grupo precisa alinhar qual é o correto)* |

**Organização de pastas do projeto:**

```
app/src/main/java/com/example/portrasdobalcao/
├── ui/        # telas (Compose)
├── data/      # SessaoManager e afins
├── model/     # entidades e DAOs (Room)
└── MainActivity.kt
```

---

## 8. Plano de testes

| # | O que testar | Passos | Resultado esperado | OK? |
| :---- | :---- | :---- | :---- | :---- |
| T1 | Abrir o app pela primeira vez | Instalar e abrir | Tela principal aparece com estado vazio explicado | |
| T2 | Cadastrar, editar e excluir produto | Tocar em "+", preencher, salvar; depois editar; depois excluir | Produto aparece, é atualizado e depois some da lista | |
| T3 | Relatório de estoque | Cadastrar 2+ produtos com custo e quantidade conhecidos | Total exibido bate com a soma manual (custo × quantidade) | |
| T4 | Falha de banco | Forçar erro ao salvar | Mensagem clara, app não fecha | |
| T5 | Reabrir o app | Fechar e abrir de novo | Produtos cadastrados continuam salvos | |

**Testado em:** *(preencher com modelo e versão do Android — pelo menos 2 aparelhos)*

---

## 9. Histórico de versões deste documento

| Versão | Data | Autor | O que mudou |
| :---- | :---- | :---- | :---- |
| 1.0 | 22/09/2026 | Grupo Kaigang | Versão inicial, preenchida com base no Canvas aprovado |
| 2.0 | 06/10/2026 | Grupo Kaigang | Remove toda funcionalidade de margem de lucro/preço de venda; remove seções duplicadas do Canvas; troca "CRUD" por funções nomeadas (RF01–RF06) |