# AGENTS.md (camada de interface)

Regras específicas para a pasta `ui/`. Complementam o AGENTS.md da raiz.

- Somente Jetpack Compose com Material 3. Nada de XML, Views ou Fragments.
- Telas em `screens/` e ViewModels em `viewmodel/`.
- Composables recebem estado e callbacks; não acessam DAO nem Repository diretamente.
- Estado vindo de `StateFlow` deve ser lido com `collectAsState()`.
- Listas usam `LazyColumn` com `key` estável.
- Toda tela nova deve ser registrada no `NavHost` e ter acesso a partir do menu principal.
- Textos visíveis ao usuário em português.
