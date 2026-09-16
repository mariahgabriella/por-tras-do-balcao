# 🎯 Canvas do Projeto Final — App Android

> **Como usar:** este é o primeiro documento do projeto. Preencha em grupo, em uma única aula, **antes de escrever qualquer linha de código**. Cada bloco tem no máximo 5 linhas — se não couber, o projeto está grande demais. Depois de preenchido e validado pelo professor, ele vira a base do [`PRD.md`](http://PRD.md).

|  |  |
| :---- | :---- |
| **Grupo nome** | Kaigang |
| **Integrantes (3 a 4\)** | Mariah, Pedro, Ian, Heitor senior |
| **Turma** | 3º ano B |
| **Repositório** | `https://github.com/mariahgabriella/por-tras-do-balcao` |
| **Data de preenchimento** | *09/09*/2026 |
| **Entrega final** | **10/12/2026** |

---

## 🧩 Bloco 1 — Nome e pitch do app

**Nome do app:** *(máx. 30 caracteres — é o mesmo que vai na loja)*

**Pitch em uma frase:**

> "O **Atrás do balcão** ajuda **Hyparrenia Torres(proprietária de loja)**  a organizar o estoque e o setor financeiro sem precisar de **papel e calculadora**."

---

## 😖 Bloco 2 — Problema

Qual dor real vocês estão resolvendo? Descrevam uma situação concreta que alguém vive hoje.

Muitas pequenas lojas não possuem um controle organizado do estoque e das finanças. Isso dificulta o acompanhamento do capital investido e do lucro obtido, podendo causar prejuízos e problemas na gestão do negócio.

**Como esse problema é resolvido hoje (sem o app)?**  
com papel e calculadora ou às vezes nem é resolvido  
---

## 👥 Bloco 3 — Público-alvo

Para quem é o app? Sejam específicos (idade, contexto, com que frequência usariam).

- **Perfil principal: Lojistas**   
- **Quando/onde usam: Diariamente em suas lojas**  
- **Uma pessoa real que testaria o app:** *(nome e relação com o grupo — ela vai testar o `.apk` na Etapa 3\) Hyparrenia(Mãe do Heitor) e Lojista*

---

## 💡 Bloco 4 — Solução em uma tela

Descreva o que a **tela principal** mostra e o que o usuário consegue fazer nela.

- **A tela principal: Exibe os produtos cadastrados, informações básicas de estoque e acesso rápido ao relatório financeiro.**  
- **A ação principal do usuário é: Cadastrar produtos da loja e visualizar o Relatório financeiro.**  
- **Depois de agir, o usuário vê: Uma confirmação de sucesso ou uma mensagem de erro explicando o que precisa ser corrigido.**

---

## ✅ Bloco 5 — Funcionalidades do MVP

Máximo de **4 funcionalidades**. Se tiver mais, corte. Lembre: *qualidade acima de complexidade*.

| \# | Funcionalidade | Essencial? | Quem faz |
| :---- | :---- | :---- | :---- |
| F1  | CRUD | Sim | Heitor |
| F2 | Lista de produtos | Sim | Gabi |
| F3 | Cálculo de Lucros | Sim | Ian |
| F4 | Relatório | Sim | Pedro |

---

## 🚫 Bloco 6 — Fora do escopo

O que o app **não** vai fazer nesta entrega. Escrever isso aqui protege vocês de perder o prazo.

- Pagamento  
- Notificação  
- Sincronização em Nuvem

---

## ⚙️ Bloco 7 — Caminho técnico

Marque **uma** opção (as três valem a mesma nota):

- [x] **Opção A — Room:** dados salvos no próprio celular (lista de compras, agenda, diário de treino, controle financeiro)  
- [ ] **Opção B — Retrofit:** dados vindos de uma API pública (notícias, filmes, feed, clima)  
- [ ] **Opção C — Desafio:** API \+ salvar favoritos localmente

**Se escolheu B ou C — qual API?** *(link da documentação \+ precisa de chave? é gratuita?)*

**Bibliotecas que o grupo vai usar:**

**Onde entra o `try/catch`?** *(qual operação pode falhar: banco vazio, internet caindo, API fora do ar, campo em branco)*

- Pode falhar: Campo em branco ao tentar registrar um produto  
- O usuário vê a mensagem: “Um campo está vazio. Preencha todos para continuar”

---

## 🎨 Bloco 8 — Identidade visual

| Item | Definição do grupo |
| :---- | :---- |
| Nome exibido (`strings.xml`) | Atrás do Balcão |
| Cor principal (hex, em `Color.kt`) | `#FFFC99` |
| Ideia do ícone (512×512) | Um balcão preto em um fundo amarelo com o nome do app em amarelo |
| `applicationId` | `br.edu.ifpe.atrasdobalcao` |
| Versão inicial | `1.0` (versionCode `1`) |

---

## 👤 Bloco 9 — Equipe, papéis e riscos

| Integrante | Papel principal | Responsável por |
| :---- | :---- | :---- |
| Ian | Dev / telas | Fazer as telas e fazer as informações serem mostrada de forma organizada |
| Heitor | Dev / dados (Room ou Retrofit) | Fazer o banco de dados e garantir que os dados sejam registrados e salvos de maneira apropriada |
| Gabi | Design e identidade visual | Criar uma identidade visual e reconhecível, assim como uma navegação intuitiva |
| Pedro | Documentação, build e entrega | Registrar as mudanças e funcionalidades do app e garantir que tudo vá de acordo com os planos. |

> Todos programam. O "papel" define quem **responde** por aquela parte, não quem trabalha sozinho.

**Riscos — o que pode dar errado e o plano B:**

| Risco | Plano B |
| :---- | :---- |
| Alguém falta num momento importante | Dividir as tarefas em partes pequenas, pra outro do grupo conseguir assumir |
| O banco de dados (Room) dá erro perto do prazo | Deixar a parte de cadastrar produto funcionando cedo, antes de mexer em Lucro/Relatório |
| A testadora não consegue testar o app a tempo | Ter uma segunda pessoa reserva já combinada |
| Falta tempo pra terminar tudo | Cortar o que está fora do escopo, focar no essencial |

---

## 🤖 Bloco 10 — Acordo de trabalho com IA

A implementação pode ser feita com o **Gemini no Android Studio**. Vocês orientam, ele digita — e cada integrante precisa saber explicar o que entrou no projeto. Regras completas em [`docs/USO_DE_IA.md`](http://docs/USO_DE_IA.md).

**Três regras que vamos escrever no nosso `AGENTS.md`** *(o arquivo que diz à IA como trabalhar no nosso projeto)*:

1. A IA não pode tomar decisões sem aprovação do grupo.  
2. Todos os integrantes devem compreender o código utilizado  
3. Não adicionar ao código nada que não tenha sido pedido.

**Combinados do grupo:**

- [x] Ninguém clica *Accept* no Agent Mode sem ler a mudança inteira.  
- [x] Quem aceitou o código escreve o comentário de fronteira do arquivo.  
- [x] Antes de cada marco, revisamos juntos: alguém aqui não entende alguma parte?  
- [x] Nenhuma chave de API ou senha vai para o prompt.  
- Outro combinado nosso:

**Como vamos garantir que todos entendem tudo** *(ex.: quem implementa apresenta o arquivo aos outros; revezar as partes; revisar o pull request do colega)*:  
Nós vamos fazer revisões de cada parte do projeto semanalmente, com um relatório que cada integrante que adiciona uma funcionalidade  escreve e descreve o que fez. 

---

## 🗓️ Bloco 11 — Marcos até 10/12

| Marco | Prazo | Como se comprova no GitHub |
| :---- | :---- | :---- |
| M1 — Canvas preenchido \+ repositório criado | 16/09 | `CANVAS.md` no `main` |
| M2 — PRD aprovado \+ telas rascunhadas | 30/09 | `PRD.md` \+ imagens em `docs/` |
| M3 — Funcionalidade base rodando | 21/10 | tela principal lista dados \+ 1 ação \+ `try/catch` |
| M4 — Dados completos (Room/Retrofit) e erros tratados | 11/11 | commits da camada de dados |
| M5 — Identidade visual \+ `.apk` de release testado | 25/11 | ícone, cores, `.apk` testado por 2 pessoas de fora |
| M6 — `.aab` \+ material de loja \+ `README.md` | 02/12 | pasta `loja/` \+ `README.md` completo |
| **Entrega e apresentação** | **10/12** | tag `v1.0` no repositório |

---

## 🏁 Bloco 12 — Definição de pronto

O grupo só considera o app pronto quando **todas** estas frases forem verdadeiras:

- [ ] O app abre e não fecha sozinho depois de 5 minutos de uso.  
- [ ] A tela principal mostra dados reais (não texto de exemplo fixo no código).  
- [ ] A ação principal funciona e o resultado aparece na tela.  
- [ ] Quando algo falha, aparece uma mensagem clara — o app não quebra.  
- [ ] O app tem nome, ícone e cor próprios (nada de ícone padrão do Android).  
- [ ] Duas pessoas de fora do grupo instalaram o `.apk` e conseguiram usar sem explicação.  
- [ ] O `README.md` explica o que o app faz, com o que foi feito e como gerar o build.  
- [ ] O `docs/USO_DE_IA.md` e o `AGENTS.md` estão preenchidos.  
- [ ] **Cada integrante consegue abrir o projeto e fazer uma mudança pequena sozinho** — trocar um texto, acrescentar um campo, mudar a ordem da lista.  
- [ ] Todo arquivo nosso tem o comentário de fronteira escrito por nós.

---

## ✍️ Validação do professor

|  |  |
| :---- | :---- |
| Data |  |
| Situação | ( ) Aprovado ( ) Aprovado com ajustes ( ) Refazer |
| Observações |  |

