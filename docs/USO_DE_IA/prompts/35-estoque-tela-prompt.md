# Prompt 35 — ui/features/estoque/EstoqueScreen.kt e EstoqueViewModel.kt

- **Ferramenta (segundo o relatório do grupo):** Gemini (Agent Mode)
- **Quem fez (pelo histórico do Git):** Ian
- **Responsável por explicar (divisão do AGENTS.md):** Ian (telas)
- **Data de entrada no repositório:** 2026-10-08. É a data em que o arquivo apareceu no Git, não a data em que o prompt foi escrito.
- **Natureza do registro:** reconstruído a partir do relatório de prompts do grupo. O texto não é uma cópia do original da conversa, e o histórico do Git não registra a ferramenta usada.
- **Saída correspondente:** [35-estoque-tela-output.md](../outputs/35-estoque-tela-output.md)

## Arquivos resultantes

- `app/src/main/java/com/example/portrasdobalcao/ui/features/estoque/EstoqueScreen.kt`: 2026-10-08 (commit `c010670`, autor do commit: Ian)
- `app/src/main/java/com/example/portrasdobalcao/ui/features/estoque/EstoqueViewModel.kt`: 2026-10-08 (commit `c010670`, autor do commit: Ian)

## P1 (prompt inicial)

Crie a tela de entrada e saída de estoque.

## P2 (prompt de melhoria)

Crie a tela para registrar ENTRADA ou SAIDA de um produto com SeletorSegmentado. O ViewModel usa o EstoqueRepository e mostra mensagens para cada ResultadoOperacao, principalmente EstoqueInsuficiente com a quantidade atual.
