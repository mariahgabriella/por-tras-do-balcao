# Prompt 44 — res/values/strings*.xml (7 arquivos)

- **Ferramenta (segundo o relatório do grupo):** Gemini (Agent Mode)
- **Quem fez (pelo histórico do Git):** Ian; Heitor (conta kansasgrind); Mariah (conta mariahgabriella); Pedro
- **Responsável por explicar (divisão do AGENTS.md):** Mariah (identidade visual e recursos)
- **Data de entrada no repositório:** 2026-10-07. É a data em que o arquivo apareceu no Git, não a data em que o prompt foi escrito.
- **Natureza do registro:** reconstruído a partir do relatório de prompts do grupo. O texto não é uma cópia do original da conversa, e o histórico do Git não registra a ferramenta usada.
- **Saída correspondente:** [44-strings-output.md](../outputs/44-strings-output.md)

## Arquivos resultantes

- `app/src/main/res/values/strings.xml`: 2026-10-08 (commit `c010670`, autor do commit: Ian)
- `app/src/main/res/values/strings_auth.xml`: 2026-10-07 (commit `4ae8afa`, autor do commit: Heitor (conta kansasgrind))
- `app/src/main/res/values/strings_comum.xml`: 2026-10-07 (commit `1a29293`, autor do commit: Mariah (conta mariahgabriella))
- `app/src/main/res/values/strings_produto.xml`: 2026-10-07 (commit `1a29293`, autor do commit: Mariah (conta mariahgabriella))
- `app/src/main/res/values/strings_estoque.xml`: 2026-10-08 (commit `c010670`, autor do commit: Ian)
- `app/src/main/res/values/strings_perfil.xml`: 2026-10-07 (commit `4ae8afa`, autor do commit: Heitor (conta kansasgrind))
- `app/src/main/res/values/strings_sobre.xml`: 2026-10-07 (commit `0532097`, autor do commit: Pedro)

## P1 (prompt inicial)

Tire os textos do código e coloque em strings.xml.

## P2 (prompt de melhoria)

Separe os textos por assunto em arquivos (auth, comum, produto, estoque, perfil, sobre). Use os parâmetros %1$s onde for preciso, em português, com tom simples e sem jargão. Não mude a lógica das telas.
