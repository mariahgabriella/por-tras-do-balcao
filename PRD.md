# 📄 PRD — Documento de Requisitos do Produto

| | |
| :---- | :---- |
| **App** | Atrás do Balcão |
| **Grupo** | Kaigang |
| **Autores** | Mariah, Pedro, Ian, Heitor, Gabi |
| **Versão do documento** | 1.0 |
| **Última atualização** | 22/09/2026 |
| **Status** | (X) Rascunho ( ) Em revisão ( ) Aprovado |

---

## 1. Visão do produto

**Pitch:** O Atrás do Balcão ajuda lojistas a organizar o estoque e o setor financeiro sem precisar de papel e calculadora.

**Problema:** Muitas pequenas lojas não têm um controle organizado do estoque e das finanças. As anotações são feitas em papel, cadernos ou de memória, o que dificulta saber quanto foi investido, quanto está sendo lucrado e o que precisa ser reposto — causando prejuízos e falhas na gestão do negócio.

**Por que vale a pena fazer isso:** o lojista passa a saber, em poucos toques, o que tem em estoque e qual a margem de lucro de cada produto, sem depender de anotações manuais nem de cálculos feitos na calculadora — reduzindo erro e economizando tempo no dia a dia da loja.

---

## 2. Público e cenário de uso

**Usuário-alvo:** lojistas de pequenos negócios que hoje controlam estoque e finanças manualmente.

**História de uso:**

> "São 19h, a Hyparrenia acabou de fechar a loja e precisa saber quais produtos estão acabando e quanto lucrou no dia. Ela abre o Atrás do Balcão e vê a lista de produtos com a quantidade em estoque e a margem de lucro de cada um. Em menos de 30 segundos, ela identifica o que precisa repor e quanto está ganhando com cada item."

---

## 3. Objetivos e não-objetivos

**Objetivos desta versão (v1.0):**

1. Permitir cadastrar produtos com nome, categoria, quantidade e preços de custo/venda.
2. Permitir listar, editar e excluir produtos cadastrados.
3. Calcular e exibir automaticamente a margem de lucro de cada produto.

**Não-objetivos (fora do escopo):**

- ❌ Pagamento
- ❌ Notificação
- ❌ Sincronização em nuvem

---

## 4. Requisitos funcionais

| ID | História de usuário | Critério de aceite | Prioridade |
| :---- | :---- | :---- | :---- |
| RF01 | Como *lojista*, quero ver a lista de produtos cadastrados para acompanhar meu estoque. | Ao abrir o app, a lista aparece com nome, categoria e quantidade de cada produto; se não houver nenhum, aparece a mensagem "Nenhum produto cadastrado ainda." | Must |
| RF02 | Como *lojista*, quero cadastrar um novo produto para registrá-lo no estoque. | Ao tocar em "+", preencher nome, categoria, quantidade, preço de custo e preço de venda e confirmar, o produto aparece na lista. | Must |
| RF03 | Como *lojista*, quero editar um produto existente para corrigir suas informações. | Ao tocar em um produto, alterar os campos e confirmar, as mudanças são salvas e refletidas na lista. | Must |
| RF04 | Como *lojista*, quero excluir um produto que não vendo mais. | Ao tocar em excluir e confirmar a ação, o item desaparece da lista imediatamente. | Must |
| RF05 | Como *lojista*, quero ver a margem de lucro de cada produto sem precisar calcular. | A partir do preço de custo e do preço de venda informados, o app calcula e exibe a margem de lucro (%) na tela de detalhe do produto. | Should |
| RF06 | Como *lojista*, quero buscar um produto pelo nome para encontrá-lo rápido em uma lista grande. | Ao digitar no campo de busca, a lista é filtrada em tempo real pelos produtos cujo nome contém o texto digitado. | Could |

---

## 5. Requisitos não funcionais

| ID | Requisito | Como será verificado |
| :---- | :---- | :---- |
| RNF01 | O app não pode fechar sozinho durante o uso normal | 5 minutos de uso contínuo sem crash, em 2 celulares diferentes |
| RNF02 | Toda operação que pode falhar está dentro de `try/catch` | Revisão do código: banco e entradas do usuário |
| RNF03 | Nenhuma falha mostra tela branca ou fecha o app — sempre há mensagem ao usuário | Testes de falha da seção 9 |
| RNF04 | O app roda a partir do Android 7.0 (minSdk 24) | Instalação em dispositivo real |
| RNF05 | Textos visíveis ficam em `strings.xml`, não escritos direto no código | Revisão do código |
| RNF06 | Todo arquivo do pacote do app tem comentário de fronteira escrito pelo grupo | Revisão do código |
| RNF07 | Qualquer integrante consegue localizar e alterar qualquer parte do app | Teste de mudança ao vivo (rubrica) |
| RNF08 | O app funciona sem conexão com a internet, já que os dados ficam salvos localmente (Room) | Testar com o celular em modo avião |

---

## 6. Telas e navegação

**Mapa de navegação:**

```
[Tela Principal — lista de produtos]
      │
      ├── toca no "+"      → [Tela de Cadastro de Produto]
      ├── toca em um item  → [Tela de Detalhe/Edição]
      └── (estado vazio)   → mensagem "Nenhum produto cadastrado ainda."
```

| Tela | O que mostra | Ações disponíveis |
| :---- | :---- | :---- |
| Principal | Lista de produtos com nome, categoria, quantidade e campo de busca | Adicionar produto, tocar em um item para ver/editar, excluir |
| Detalhe/Cadastro | Nome, categoria, quantidade, preço de custo, preço de venda e margem de lucro calculada | Salvar, excluir, cancelar |

**Rascunhos das telas:** coloque as imagens em `docs/telas/` e liste os arquivos aqui.

- `docs/telas/01-principal.png`
- `docs/telas/02-cadastro.png`
- `docs/telas/03-detalhe.png`

---

## 7. Dados

### Opção A (Room)

**Entidade principal:** `Produto`

| Campo | Tipo | Obrigatório | Observação |
| :---- | :---- | :---- | :---- |
| `id` | Long | sim | chave primária, autogerada |
| `nome` | String | sim | nome do produto |
| `categoria` | String | sim | categoria do produto |
| `quantidade` | Int | sim | quantidade em estoque |
| `precoCusto` | Double | sim | usado no cálculo de margem |
| `precoVenda` | Double | sim | usado no cálculo de margem |

**Operações necessárias:** (X) inserir (X) listar (X) atualizar (X) excluir

---

## 8. Arquitetura e tecnologias

| Item | Escolha |
| :---- | :---- |
| Linguagem | Kotlin |
| Interface | (X) Jetpack Compose ( ) XML/Views *(a confirmar com o grupo)* |
| Persistência | (X) Room |
| Rede | — (não se aplica, Opção A) |
| Outras bibliotecas | ViewModel, StateFlow/LiveData |
| `minSdk` / `targetSdk` | 24 / 34 |

**Organização de pastas do projeto:**

```
app/src/main/java/br/edu/ifpe/atrasdobalcao/
├── ui/        # telas
├── data/      # Room (entidade Produto, DAO, database)
└── MainActivity.kt
```

---

## 9. Tratamento de erros

| Situação de falha | O que o app faz | Mensagem para o usuário |
| :---- | :---- | :---- |
| Campo obrigatório em branco | Impede o salvamento e destaca o campo vazio | "Um campo está vazio. Preencha todos para continuar." |
| Lista vazia (nenhum produto ainda) | Mostra estado vazio com botão para cadastrar o primeiro produto | "Nenhum produto cadastrado ainda. Toque em '+' para começar." |
| Erro ao salvar no banco | Mantém os dados digitados na tela e mostra aviso | "Não foi possível salvar. Tente novamente." |
| Erro ao excluir | Mantém o item na lista e mostra aviso | "Não foi possível excluir. Tente novamente." |
| Preço de venda menor que o preço de custo | Permite salvar, mas alerta o lojista | "Preço de venda menor que o custo — confira os valores." |

---

## 10. Identidade visual e publicação

| Item | Definição | Onde fica |
| :---- | :---- | :---- |
| Nome do app | Atrás do Balcão | `strings.xml` |
| Cor principal | `#703D57` | `Color.kt` |
| Cor secundária | `#F2C744` *(amarelo, combinando com o ícone)* | `Color.kt` |
| Ícone 512×512 | Balcão preto em fundo amarelo, com o nome do app em amarelo | `loja/icone-512.png` |
| `applicationId` | `br.edu.ifpe.atrasdobalcao` | `build.gradle.kts` |
| `versionName` / `versionCode` | `1.0` / `1` | `build.gradle.kts` |

**Material da loja** (Etapa 4 do projeto):

| Artefato | Limite | Conteúdo |
| :---- | :---- | :---- |
| Título | 30 caracteres | Atrás do Balcão |
| Descrição curta | 80 caracteres | Controle de estoque e cálculo de lucro para pequenos lojistas. |
| Descrição completa | — | *(escrever em `loja/descricao.md`)* |
| Imagem de destaque | 1024×500 | `loja/destaque-1024x500.png` |
| Screenshots | mín. 2 | `loja/screenshots/` |
| Esboço de privacidade | — | Os dados ficam salvos apenas no celular (Room); o app não envia nada para fora e não coleta dados além dos que o lojista digita. |
| Arquivo `.aab` | — | `loja/app-release.aab` |

---

## 11. Plano de testes

| # | O que testar | Passos | Resultado esperado | OK? |
| :---- | :---- | :---- | :---- | :---- |
| T1 | Abrir o app pela primeira vez | Instalar e abrir | Tela principal aparece com estado vazio explicado | |
| T2 | Cadastrar, editar e excluir produto | Tocar em "+", preencher, salvar; depois editar; depois excluir | Produto aparece, é atualizado e depois some da lista | |
| T3 | Falha de banco | Preencher dados e tentar salvar em condição forçada de erro | Mensagem clara, app não fecha | |
| T4 | Reabrir o app | Fechar e abrir de novo | Produtos cadastrados continuam salvos | |
| T5 | Teste com usuário externo | Hyparrenia (ou outra pessoa de fora) usa sem explicação | Consegue cadastrar e consultar um produto sozinha | |

**Testado em:** *(preencher com modelo e versão do Android — pelo menos 2 aparelhos)*

---

## 12. Cronograma

| Marco | Prazo | Responsável | Status |
| :---- | :---- | :---- | :---- |
| M1 — Canvas + repositório | 16/09 | Grupo | Concluído |
| M2 — PRD aprovado + telas | 30/09 | Pedro (documentação) | Em andamento |
| M3 — Funcionalidade base | 21/10 | Ian e Heitor | Não iniciado |
| M4 — Dados e erros tratados | 11/11 | Heitor | Não iniciado |
| M5 — Identidade + `.apk` testado | 25/11 | Gabi | Não iniciado |
| M6 — `.aab` + loja + README | 02/12 | Pedro | Não iniciado |
| **Entrega e apresentação** | **10/12** | grupo | Não iniciado |

---

## 13. Riscos

| Risco | Impacto | Plano B |
| :---- | :---- | :---- |
| Alguém falta num momento importante | Médio | Dividir as tarefas em partes pequenas, para outro do grupo conseguir assumir |
| O banco de dados (Room) dá erro perto do prazo | Alto | Deixar a parte de cadastrar produto funcionando cedo, antes de mexer em relatórios/margem |
| A testadora (Hyparrenia) não consegue testar a tempo | Médio | Ter uma segunda pessoa reserva já combinada |
| Falta tempo para terminar tudo | Alto | Cortar o que está fora do escopo, focar no essencial |

---

## 14. Como vamos orientar a implementação com IA

A implementação usa o **Gemini no Android Studio**. Este PRD é o documento que diz à IA o que construir — quanto mais preciso ele estiver, menos a IA inventa. Regras completas em `docs/USO_DE_IA.md`.

**Recursos que vamos usar:** (X) Chat (X) Agent Mode (X) Explain Code ( ) Ask Gemini no Logcat ( ) Generate Unit Tests ( ) Transform UI

**Regras que colocamos no `AGENTS.md`** (resumo):

- A IA não pode tomar decisões sem aprovação do grupo.
- Todos os integrantes devem compreender o código utilizado.
- Não adicionar ao código nada que não tenha sido pedido.

**Divisão do perímetro explicável** — quem responde por explicar o quê na apresentação:

| Parte do código | Responsável |
| :---- | :---- |
| Telas (`ui/`) | Ian |
| Dados (`data/`) | Heitor |
| Identidade visual e recursos | Gabi |
| Build e artefatos de loja | Pedro |

**Decisões que o grupo tomou contra a sugestão da IA** *(preencher ao longo do projeto)*:

---

## 15. Histórico de versões deste documento

| Versão | Data | Autor | O que mudou |
| :---- | :---- | :---- | :---- |
| 1.0 | 22/09/2026 | Grupo Kaigang | Versão inicial, preenchida com base no Canvas aprovado |