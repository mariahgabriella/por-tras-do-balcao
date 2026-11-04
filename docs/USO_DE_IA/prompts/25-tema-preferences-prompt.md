# Prompt 25 — data/local/TemaPreferences.kt

- **Ferramenta (segundo o relatório do grupo):** Gemini (Agent Mode)
- **Quem fez (pelo histórico do Git):** Mariah (conta mariahgabriella)
- **Responsável por explicar (divisão do AGENTS.md):** Heitor (dados)
- **Data de entrada no repositório:** 2026-10-07. É a data em que o arquivo apareceu no Git, não a data em que o prompt foi escrito.
- **Natureza do registro:** reconstruído a partir do relatório de prompts do grupo. O texto não é uma cópia do original da conversa, e o histórico do Git não registra a ferramenta usada.
- **Saída correspondente:** [25-tema-preferences-output.md](../outputs/25-tema-preferences-output.md)

## Arquivos resultantes

- `app/src/main/java/com/example/portrasdobalcao/data/local/TemaPreferences.kt`: 2026-10-07 (commit `1a29293`, autor do commit: Mariah (conta mariahgabriella))

## P1 (prompt inicial)

Crie a opção de tema escuro.

## P2 (prompt de melhoria)

Crie TemaPreferences com o enum TemaApp (CLARO, ESCURO, SISTEMA), salvo em SharedPreferences no mesmo estilo do SessaoManager, expondo um StateFlow. Se o valor salvo for inválido, volte para SISTEMA.
