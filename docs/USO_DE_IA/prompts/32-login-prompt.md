# Prompt 32 — ui/features/login/LoginScreen.kt e LoginViewModel.kt

- **Ferramenta (segundo o relatório do grupo):** Gemini (Agent Mode)
- **Quem fez (pelo histórico do Git):** Heitor (conta kansasgrind)
- **Responsável por explicar (divisão do AGENTS.md):** Ian (telas)
- **Data de entrada no repositório:** 2026-10-07. É a data em que o arquivo apareceu no Git, não a data em que o prompt foi escrito.
- **Natureza do registro:** reconstruído a partir do relatório de prompts do grupo. O texto não é uma cópia do original da conversa, e o histórico do Git não registra a ferramenta usada.
- **Saída correspondente:** [32-login-output.md](../outputs/32-login-output.md)

## Arquivos resultantes

- `app/src/main/java/com/example/portrasdobalcao/ui/features/login/LoginScreen.kt`: 2026-10-07 (commit `4ae8afa`, autor do commit: Heitor (conta kansasgrind))
- `app/src/main/java/com/example/portrasdobalcao/ui/features/login/LoginViewModel.kt`: 2026-10-07 (commit `4ae8afa`, autor do commit: Heitor (conta kansasgrind))

## P1 (prompt inicial)

Crie a tela de login.

## P2 (prompt de melhoria)

Crie a tela com dois modos (criar conta e entrar) usando CampoComRotulo. O LoginViewModel valida os campos, chama o UsuarioRepository e mostra erro para loja já existente e senha errada. A tela só recebe estado e callbacks.
