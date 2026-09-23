# 🤖 AGENTS.md — Regras para uso de IA no projeto Atrás do Balcão

Este documento define como o grupo **Kaigang** usa IA (Gemini no Android Studio) no desenvolvimento do app **Atrás do Balcão**. Toda ferramenta de IA que trabalhar neste repositório — Gemini, Claude ou qualquer outra — deve seguir estas regras.

---

## 1. Regras gerais

1. A IA **não pode tomar decisões** (de arquitetura, bibliotecas, nome de variáveis importantes, fluxo de telas, etc.) **sem aprovação do grupo**.
2. Todos os integrantes devem **compreender o código utilizado** — ninguém deve ter no projeto um trecho que não sabe explicar.
3. A IA **não deve adicionar ao código nada que não tenha sido pedido** (sem funcionalidades extras, sem "melhorias" não solicitadas, sem bibliotecas novas por conta própria).

---

## 2. Combinados do grupo

- Ninguém clica em "Accept" no Agent Mode sem ler a mudança inteira antes.
- Quem aceitou o código é quem escreve o comentário de fronteira daquele arquivo.
- Antes de cada marco do cronograma, o grupo revisa junto: alguém não entende alguma parte do que foi feito?
- **Nenhuma chave de API ou senha** deve ser colada em um prompt de IA.
- O grupo faz revisões semanais, com um relatório curto de cada integrante descrevendo o que fez.

---

## 3. Recursos de IA permitidos neste projeto

- Chat
- Agent Mode
- Explain Code

Uso de outros recursos (Ask Gemini no Logcat, Generate Unit Tests, Transform UI) deve ser combinado com o grupo antes.

---

## 4. Divisão do perímetro explicável

Cada integrante é responsável por saber explicar a própria parte na apresentação final:

| Parte do código | Responsável |
| :---- | :---- |
| Telas (`ui/`) | Ian |
| Dados (`data/`) | Heitor |
| Identidade visual e recursos | Gabi |
| Build e artefatos de loja | Pedro |

---

## 5. Convenções técnicas do projeto

| Item | Definição |
| :---- | :---- |
| Linguagem | Kotlin |
| Persistência | Room |
| `applicationId` | `br.edu.ifpe.atrasdobalcao` |
| `minSdk` / `targetSdk` | 24 / 34 |

**Estrutura de pastas esperada dentro de `app/`:**

```
app/src/main/java/br/edu/ifpe/atrasdobalcao/
├── ui/        # telas
├── data/      # Room (entidade Produto, DAO, database)
└── MainActivity.kt
```

Arquivos de código **não devem ficar soltos na raiz do repositório** — sempre dentro dessa estrutura.

---

## 6. O que a IA não deve fazer neste projeto

- Tomar decisões de arquitetura ou de fluxo sem validação do grupo.
- Adicionar bibliotecas ou dependências não combinadas previamente.
- Gerar código sem que um integrante entenda e assuma a responsabilidade por ele.
- Sugerir ou incluir chaves de API, senhas ou dados sensíveis no código ou em prompts.
- Fazer commit ou push sozinha — toda alteração passa por revisão humana antes de ir para o repositório.

---

## 7. Decisões que o grupo tomou contra a sugestão da IA

*(preencher ao longo do projeto — isso conta a favor na avaliação, conforme a rubrica)*