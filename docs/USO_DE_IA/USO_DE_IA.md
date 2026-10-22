# Uso de IA no projeto Atrás do Balcão

## 1. Objetivo

Este documento registra como ferramentas de Inteligência Artificial foram usadas no desenvolvimento do app **Atrás do Balcão**, de forma transparente e rastreável. Ele complementa o `AGENTS.md`, que define as regras que toda IA deve seguir neste repositório, e o Bloco 10 do `docs/CANVAS.md`, que descreve o acordo de trabalho do grupo com IA.

O princípio do grupo (Kaigang) é simples: a IA ajuda a escrever, mas **o grupo decide** e **cada integrante precisa saber explicar tudo o que entrou no projeto**.

## 2. Ferramentas utilizadas

| Ferramenta | Para que é usada | Recursos usados |
| :---- | :---- | :---- |
| Gemini (Android Studio) | Implementação do app em Kotlin: telas, entidades, ViewModels e ajustes de configuração, sempre orientada pelo grupo | Chat, Agent Mode, Explain Code |
| Claude (Anthropic) | Apoio à documentação: organização de issues, orientação de uso de Git/GitHub e redação deste documento | Chat |

Os recursos de IA permitidos no projeto são **Chat**, **Agent Mode** e **Explain Code**. O uso de qualquer outro recurso (Ask Gemini no Logcat, Generate Unit Tests, Transform UI) deve ser combinado antes com o grupo.

## 3. Regras seguidas pelo grupo

Regras gerais definidas no `AGENTS.md`:

1. A IA não pode tomar decisões (arquitetura, bibliotecas, nomes importantes de variáveis, fluxo de telas etc.) sem aprovação do grupo.
2. Todos os integrantes devem compreender o código utilizado. Ninguém deve ter no projeto um trecho que não sabe explicar.
3. A IA não deve adicionar ao código nada que não tenha sido pedido: sem funcionalidades extras, sem melhorias não solicitadas e sem bibliotecas novas por conta própria.

Combinados do grupo:

- Ninguém clica em "Accept" no Agent Mode sem ler a mudança inteira antes.
- Quem aceitou o código escreve o comentário de fronteira daquele arquivo.
- Antes de cada marco do cronograma, o grupo revisa junto se alguém não entende alguma parte do que foi feito.
- Nenhuma chave de API ou senha é colada em um prompt de IA.
- O grupo faz revisões semanais, com um relatório curto de cada integrante sobre o que fez.

## 4. Como os resultados são avaliados e aproveitados

- Toda mudança sugerida pela IA é lida por inteiro antes de ser aceita.
- O código aceito passa a ter um responsável: quem o aceitou comenta o arquivo e consegue explicá-lo.
- Cada integrante é responsável por explicar a sua parte na apresentação final:

| Parte do código | Responsável |
| :---- | :---- |
| Telas (`ui/`) | Ian |
| Dados (`data/`) | Heitor |
| Identidade visual e recursos | Mariah |
| Build e artefatos de loja | Pedro |

- O histórico de commits do repositório permite ver quem integrou cada parte ao projeto.

## 5. O que foi aproveitado e o que foi descartado

**Aproveitado**

- Sequência de comandos Git/Git Bash sugerida pelo Claude (criar a branch `docs/uso-de-ia`, mover o arquivo com `git mv`, commit e push). Pedro executou cada comando e conferiu o resultado com `git status`.
- Estrutura da pasta `docs/USO_DE_IA/` com as subpastas `prompts/` e `outputs/`, incluindo arquivos `.gitkeep` para o Git versionar as pastas vazias.
- Comando sugerido pelo Claude para atualizar as referências ao caminho antigo `docs/USO_DE_IA.md` no `docs/CANVAS.md`. Pedro conferiu o resultado com `git diff` e com uma nova busca.
- Rascunho deste documento, escrito pelo Claude a partir do `AGENTS.md`, do Bloco 10 do `CANVAS.md` e do histórico de commits que Pedro enviou.

**Ajustado**

- O Claude não conseguiu abrir o repositório no GitHub. Por isso Pedro rodou comandos no terminal e enviou as saídas, e o rascunho foi feito só com o que apareceu nelas.
- O Claude sugeriu copiar o arquivo pronto com `cp`, mas isso não era possível porque o arquivo não estava no computador de Pedro. Ele colou o texto direto no arquivo do repositório.
- O rascunho trazia trechos entre colchetes, que foram preenchidos ou removidos pelo grupo com informações reais.

**Descartado ou adiado**

- O registro dos prompts e das respostas nas pastas `prompts/` e `outputs/` foi adiado. Por enquanto, apenas o `USO_DE_IA.md` foi preenchido.
- O Claude apontou que o `AGENTS.md` cita o `applicationId` `br.edu.ifpe.atrasdobalcao` e a estrutura `br/edu/ifpe/atrasdobalcao/`, mas o código usa `com.example.portrasdobalcao`. Essa sugestão de correção depende de decisão do grupo e foi tratada na Issue 2. 
## 6. Organização dos registros

Os registros de uso de IA ficam na pasta `docs/USO_DE_IA/`:

```
docs/USO_DE_IA/
├── USO_DE_IA.md   # este documento
├── prompts/       # prompts enviados às ferramentas de IA
└── outputs/       # respostas e resultados gerados pelas ferramentas
```

Para relacionar cada prompt à sua saída, os arquivos usam o mesmo número no início do nome. Por exemplo, `01-fornecedor-prompt.md` em `prompts/` corresponde a `01-fornecedor-output.md` em `outputs/`.

## 7. Responsabilidade

O uso de IA não retira a responsabilidade do grupo sobre o projeto. Qualquer integrante deve ser capaz de abrir o projeto, explicar o que cada parte faz e fazer uma pequena mudança sozinho.
