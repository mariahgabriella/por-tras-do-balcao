# Prompt 21 — data/repository/EstoqueRepository.kt

- **Ferramenta (segundo o relatório do grupo):** Gemini (Agent Mode)
- **Quem fez (pelo histórico do Git):** Ian
- **Responsável por explicar (divisão do AGENTS.md):** Heitor (dados)
- **Data de entrada no repositório:** 2026-10-08. É a data em que o arquivo apareceu no Git, não a data em que o prompt foi escrito.
- **Natureza do registro:** reconstruído a partir do relatório de prompts do grupo. O texto não é uma cópia do original da conversa, e o histórico do Git não registra a ferramenta usada.
- **Saída correspondente:** [21-estoque-repository-output.md](../outputs/21-estoque-repository-output.md)

## Arquivos resultantes

- `app/src/main/java/com/example/portrasdobalcao/data/repository/EstoqueRepository.kt`: 2026-10-08 (commit `c010670`, autor do commit: Ian)

## P1 (prompt inicial)

Crie o repositório de estoque com entrada e saída.

## P2 (prompt de melhoria)

Crie o EstoqueRepository com entrada e saída. Quantidade deve ser maior que zero, a saída não pode deixar o estoque negativo. Retorne ResultadoOperacao (Sucesso, QuantidadeInvalida, EstoqueInsuficiente, Erro).
