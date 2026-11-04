# Prompt 12 — model/Estoque.kt

- **Ferramenta (segundo o relatório do grupo):** Gemini (Agent Mode)
- **Quem fez (pelo histórico do Git):** Ian
- **Responsável por explicar (divisão do AGENTS.md):** Heitor (dados)
- **Data de entrada no repositório:** 2026-10-08. É a data em que o arquivo apareceu no Git, não a data em que o prompt foi escrito.
- **Natureza do registro:** reconstruído a partir do relatório de prompts do grupo. O texto não é uma cópia do original da conversa, e o histórico do Git não registra a ferramenta usada.
- **Saída correspondente:** [12-estoque-entidade-output.md](../outputs/12-estoque-entidade-output.md)

## Arquivos resultantes

- `app/src/main/java/com/example/portrasdobalcao/model/Estoque.kt`: 2026-10-08 (commit `c010670`, autor do commit: Ian)

## P1 (prompt inicial)

Crie a entidade Estoque ligada ao Produto.

## P2 (prompt de melhoria)

Crie a entidade Estoque com ForeignKey para Produto (onDelete CASCADE), @Index em produtoId, quantidade, quantidadeMinima e ultimaAtualizacao. Siga o AGENTS.md. Não altere outros arquivos.
