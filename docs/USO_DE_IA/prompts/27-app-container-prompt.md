# Prompt 27 — AppContainer.kt

- **Ferramenta (segundo o relatório do grupo):** Gemini (Agent Mode)
- **Quem fez (pelo histórico do Git):** Heitor (conta kansasgrind)
- **Responsável por explicar (divisão do AGENTS.md):** Heitor (dados)
- **Data de entrada no repositório:** 2026-10-07. É a data em que o arquivo apareceu no Git, não a data em que o prompt foi escrito.
- **Natureza do registro:** reconstruído a partir do relatório de prompts do grupo. O texto não é uma cópia do original da conversa, e o histórico do Git não registra a ferramenta usada.
- **Saída correspondente:** [27-app-container-output.md](../outputs/27-app-container-output.md)

## Arquivos resultantes

- `app/src/main/java/com/example/portrasdobalcao/AppContainer.kt`: 2026-10-07 (commit `4ae8afa`, autor do commit: Heitor (conta kansasgrind))

## P1 (prompt inicial)

Crie injeção de dependência.

## P2 (prompt de melhoria)

Crie o AppContainer com getInstance(context), que cria uma vez os repositórios, o SessaoManager e o TemaPreferences. Sem Hilt, no mesmo estilo do AppDatabase.getInstance.
