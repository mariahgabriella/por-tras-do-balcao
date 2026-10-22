# Prompt 23 — data/seguranca/SenhaHasher.kt

- **Ferramenta (segundo o relatório do grupo):** Gemini (Chat) + Explain Code
- **Quem fez (pelo histórico do Git):** Heitor (conta kansasgrind)
- **Responsável por explicar (divisão do AGENTS.md):** Heitor (dados)
- **Data de entrada no repositório:** 2026-10-07. É a data em que o arquivo apareceu no Git, não a data em que o prompt foi escrito.
- **Natureza do registro:** reconstruído a partir do relatório de prompts do grupo. O texto não é uma cópia do original da conversa, e o histórico do Git não registra a ferramenta usada.
- **Saída correspondente:** [23-senha-hasher-output.md](../outputs/23-senha-hasher-output.md)

## Arquivos resultantes

- `app/src/main/java/com/example/portrasdobalcao/data/seguranca/SenhaHasher.kt`: 2026-10-07 (commit `4ae8afa`, autor do commit: Heitor (conta kansasgrind))

## P1 (prompt inicial)

Como guardar senha com segurança no app?

## P2 (prompt de melhoria)

Crie um object SenhaHasher com SHA-256 e salt aleatório, formato 'sha256:salt:hash', com gerarHash, verificar (comparação segura com MessageDigest.isEqual) e estaEmHash. Sem biblioteca nova.

## P3

(Explain Code) Explique linha por linha o que o SenhaHasher faz, para eu conseguir explicar na apresentação.
