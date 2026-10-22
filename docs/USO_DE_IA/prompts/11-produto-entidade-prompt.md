# Prompt 11 — model/Produto.kt

- **Ferramenta (segundo o relatório do grupo):** Gemini (Agent Mode)
- **Quem fez (pelo histórico do Git):** Mariah (conta mariahgabriella)
- **Responsável por explicar (divisão do AGENTS.md):** Heitor (dados)
- **Data de entrada no repositório:** 2026-10-07. É a data em que o arquivo apareceu no Git, não a data em que o prompt foi escrito.
- **Natureza do registro:** reconstruído a partir do relatório de prompts do grupo. O texto não é uma cópia do original da conversa, e o histórico do Git não registra a ferramenta usada.
- **Saída correspondente:** [11-produto-entidade-output.md](../outputs/11-produto-entidade-output.md)

## Arquivos resultantes

- `app/src/main/java/com/example/portrasdobalcao/model/Produto.kt`: 2026-10-07 (commit `1a29293`, autor do commit: Mariah (conta mariahgabriella))

## P1 (prompt inicial)

Crie uma entidade Produto.

## P2 (prompt de melhoria)

Crie a entidade Produto do Room (tabela produtos) com id autoGenerate, nome, categoria, precoCusto, quantidadeEstoque e ativo. Valide no init: nome e categoria não vazios, preço maior que zero, quantidade não negativa. Mensagens em português.
