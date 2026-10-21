# AGENTS.md

Este arquivo define o contexto e as regras que qualquer IA (Copilot, Claude, Gemini, Cursor etc.)
deve seguir ao gerar ou alterar código neste repositório.

## Visão geral
App Android de controle de produtos e estoque, desenvolvido em equipe.

## Tecnologias (use somente estas)
- Linguagem: Kotlin. NÃO gerar código em Java.
- Interface: Jetpack Compose com Material 3. NÃO usar layouts XML, Views, Fragments nem ViewBinding.
- Banco de dados: Room (SQLite) com KSP.
- Assincronia: Coroutines e Flow. NÃO usar LiveData.
- Arquitetura: MVVM (Entity -> DAO -> Repository -> ViewModel -> Screen).
- Build: Gradle Kotlin DSL (build.gradle.kts).

## Estrutura de pastas
Dentro de `app/src/main/java/com/example/portrasdobalcao/`:
- `model/`              -> entidades (@Entity), DAOs e modelos de UI
- `data/`               -> AppDatabase, DAOs, `repository/`, `local/` e `seguranca/`
- `ui/features/<tela>/` -> cada tela (@Composable) com o seu ViewModel
- `ui/components/`      -> componentes reutilizáveis
- `ui/navigation/`      -> NavGraph e NavTarget
- `ui/theme/`           -> Color, Theme e Type
- `util/`               -> utilitários

Arquivos de código não ficam soltos na raiz do repositório.

## Regras de código
- Classes em PascalCase; funções e variáveis em camelCase.
- Entidades e tabelas em português, no singular (ex.: Produto, Estoque).
- Toda entidade tem `@PrimaryKey(autoGenerate = true)`.
- Relacionamentos usam `@ForeignKey` com `onDelete` explícito e `@Index` na coluna da chave estrangeira.
- Funções de banco são `suspend` ou retornam `Flow`. Nunca acessar o banco na thread principal.
- Composables recebem estado e callbacks; a lógica de negócio fica no ViewModel.
- Toda entidade nova deve ser registrada em `AppDatabase` (lista `entities`) com a `version` incrementada.
- Toda tela nova deve ser registrada no `NavHost`.
- Não adicionar dependências sem avisar a equipe.

## Regras de negócio do Estoque
- Cada produto tem no máximo um registro de estoque (`produtoId` único).
- A quantidade nunca pode ficar negativa.
- Entradas e saídas devem ter quantidade maior que zero.
- Quantidade igual ou abaixo da quantidade mínima deve ser sinalizada na interface.

## O que a IA NÃO deve fazer
- Alterar arquivos fora do escopo da tarefa pedida.
- Renomear ou apagar entidades, tabelas ou colunas existentes sem pedido explícito.
- Usar APIs depreciadas (ex.: kapt no lugar de KSP).
- Inventar nomes de classes ou campos: sempre conferir o código existente antes.

## Fluxo Git
- Branches no formato `feature/nome-da-funcionalidade`.
- Nunca commitar direto na `main`.
- Todo Pull Request deve estar vinculado a uma Issue (`Closes #N` na descrição).
- Commits curtos e no imperativo (ex.: "Adiciona entidade Estoque").
- Não commitar `build/`, `.idea/` nem `local.properties`.

## Instruções adicionais por pasta
Pastas específicas podem ter seu próprio `AGENTS.md`. Em caso de conflito, vale o arquivo mais próximo do código.
---

## 1. Regras gerais

1. A IA **não pode tomar decisões** (de arquitetura, bibliotecas, nome de variáveis importantes, fluxo de telas, etc.) **sem aprovação do grupo**.
2. Todos os integrantes devem **compreender o código utilizado** — ninguém deve ter no projeto um trecho que não sabe explicar.
3. A IA **não deve adicionar ao código nada que não tenha sido pedido** (sem funcionalidades extras, sem "melhorias" não solicitadas, sem bibliotecas novas por conta própria).

---

## 2. Combinados do grupo

- Ninguém clica em "Accept" no Agent Mode sem ler a mudança inteira antes.
- Quem aceitou o código é quem escreve o comentário de fronteira daquele arquivo.
- Antes de cada marco do cronograma, o grupo revisa junto: alguém não entende alguma parte do que foi feito?
- **Nenhuma chave de API ou senha** deve ser colada em um prompt de IA.
- O grupo faz revisões semanais, com um relatório curto de cada integrante descrevendo o que fez.

---

## 3. Recursos de IA permitidos neste projeto

- Chat
- Agent Mode
- Explain Code

Uso de outros recursos (Ask Gemini no Logcat, Generate Unit Tests, Transform UI) deve ser combinado com o grupo antes.

---

## 4. Divisão do perímetro explicável

Cada integrante é responsável por saber explicar a própria parte na apresentação final:

| Parte do código | Responsável |
| :---- | :---- |
| Telas (`ui/`) | Ian |
| Dados (`data/`) | Heitor |
| Identidade visual e recursos | Gabi |
| Build e artefatos de loja | Pedro |

---

