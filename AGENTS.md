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
- `data/entity/`     -> classes @Entity
- `data/dao/`        -> interfaces @Dao
- `data/database/`   -> AppDatabase
- `data/repository/` -> repositórios
- `ui/screens/`      -> telas (@Composable)
- `ui/viewmodel/`    -> ViewModels

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