# Prompt 15 — model/ModelosUi.kt

- **Ferramenta (segundo o relatório do grupo):** Gemini (Chat)
- **Quem fez (pelo histórico do Git):** Mariah (conta mariahgabriella)
- **Responsável por explicar (divisão do AGENTS.md):** Heitor (dados)
- **Data de entrada no repositório:** 2026-10-07. É a data em que o arquivo apareceu no Git, não a data em que o prompt foi escrito.
- **Natureza do registro:** reconstruído a partir do relatório de prompts do grupo. O texto não é uma cópia do original da conversa, e o histórico do Git não registra a ferramenta usada.
- **Saída correspondente:** [15-modelos-ui-output.md](../outputs/15-modelos-ui-output.md)

## Arquivos resultantes

- `app/src/main/java/com/example/portrasdobalcao/model/ModelosUi.kt`: 2026-10-07 (commit `1a29293`, autor do commit: Mariah (conta mariahgabriella))

## P1 (prompt inicial)

Crie uma classe para mostrar produto com estoque na tela.

## P2 (prompt de melhoria)

Crie ProdutoItem juntando Produto e Estoque, com estoqueBaixo (quantidade menor ou igual à mínima), subtotal (custo vezes quantidade) e progressoEstoque (mínima no meio da barra). Crie também o enum TipoMovimentacao (ENTRADA, SAIDA) e o ResultadoOperacao.
