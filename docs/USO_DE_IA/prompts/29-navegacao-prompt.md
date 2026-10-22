# Prompt 29 — ui/navigation/NavTarget.kt e NavGraph.kt

- **Ferramenta (segundo o relatório do grupo):** Gemini (Agent Mode)
- **Quem fez (pelo histórico do Git):** Heitor (conta kansasgrind)
- **Responsável por explicar (divisão do AGENTS.md):** Ian (telas)
- **Data de entrada no repositório:** 2026-10-07. É a data em que o arquivo apareceu no Git, não a data em que o prompt foi escrito.
- **Natureza do registro:** reconstruído a partir do relatório de prompts do grupo. O texto não é uma cópia do original da conversa, e o histórico do Git não registra a ferramenta usada.
- **Saída correspondente:** [29-navegacao-output.md](../outputs/29-navegacao-output.md)

## Arquivos resultantes

- `app/src/main/java/com/example/portrasdobalcao/ui/navigation/NavTarget.kt`: 2026-10-07 (commit `4ae8afa`, autor do commit: Heitor (conta kansasgrind))
- `app/src/main/java/com/example/portrasdobalcao/ui/navigation/NavGraph.kt`: 2026-10-07 (commit `4ae8afa`, autor do commit: Heitor (conta kansasgrind))

## P1 (prompt inicial)

Crie a navegação do app.

## P2 (prompt de melhoria)

Crie o NavTarget (sealed class: Splash, Login, Principal, ProdutoNovo, ProdutoEditar com id, Relatorio, Estoque, Perfil, About) e o NavGraph. Splash vai para Login ou Principal conforme a sessão. A tela de produto avisa a lista com 'salvo' ou 'excluido'.
