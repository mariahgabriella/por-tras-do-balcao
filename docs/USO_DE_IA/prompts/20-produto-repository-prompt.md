# Prompt 20 — data/repository/ProdutoRepository.kt

- **Ferramenta (segundo o relatório do grupo):** Gemini (Agent Mode)
- **Quem fez (pelo histórico do Git):** Mariah (conta mariahgabriella)
- **Responsável por explicar (divisão do AGENTS.md):** Heitor (dados)
- **Data de entrada no repositório:** 2026-10-07. É a data em que o arquivo apareceu no Git, não a data em que o prompt foi escrito.
- **Natureza do registro:** reconstruído a partir do relatório de prompts do grupo. O texto não é uma cópia do original da conversa, e o histórico do Git não registra a ferramenta usada.
- **Saída correspondente:** [20-produto-repository-output.md](../outputs/20-produto-repository-output.md)

## Arquivos resultantes

- `app/src/main/java/com/example/portrasdobalcao/data/repository/ProdutoRepository.kt`: 2026-10-07 (commit `1a29293`, autor do commit: Mariah (conta mariahgabriella))

## P1 (prompt inicial)

Crie o repositório de produtos.

## P2 (prompt de melhoria)

Crie o ProdutoRepository que junta Produto e Estoque num Flow de ProdutoItem, e faz cadastrar, editar e excluir numa transação. Use operacaoSegura para não derrubar o app.
