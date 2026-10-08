# 🎯 Canvas do Projeto Final — App Android

| | |
| :---- | :---- |
| **Grupo nome** | Kaigang |
| **Integrantes (3 a 4)** | Mariah, Pedro, Ian, Heitor, Gabi |
| **Turma** | 3º ano B |
| **Repositório** | https://github.com/mariahgabriella/por-tras-do-balcao |
| **Data de preenchimento** | 09/09/2026 |
| **Entrega final** | 10/12/2026 |

---

## 🧩 Bloco 1 — Nome e pitch do app

**Nome do app:** Atrás do Balcão

**Pitch em uma frase:**

> "O Atrás do Balcão ajuda Hyparrenia Torres (proprietária de loja) a organizar o estoque sem precisar de papel e calculadora."

---

## 😖 Bloco 2 — Problema

Muitas pequenas lojas não possuem um controle organizado do estoque. Isso dificulta o acompanhamento do capital investido, podendo causar prejuízos e problemas na gestão do negócio.

**Como esse problema é resolvido hoje (sem o app)?**
Com papel e calculadora, ou às vezes nem é resolvido.

---

## 👥 Bloco 3 — Público-alvo

**Perfil principal:** Lojistas
**Quando/onde usam:** Diariamente em suas lojas
**Uma pessoa real que testaria o app:** Hyparrenia (mãe do Heitor), lojista

---

## 💡 Bloco 4 — Solução em uma tela

**A tela principal:** Exibe os produtos cadastrados e informações básicas de estoque.
**A ação principal do usuário é:** Cadastrar produtos da loja e consultar o relatório de estoque.
**Depois de agir, o usuário vê:** Uma confirmação de sucesso ou uma mensagem de erro explicando o que precisa ser corrigido.

---

## ✅ Bloco 5 — Funcionalidades do MVP

| # | Funcionalidade | Essencial? | Quem faz |
| :---- | :---- | :---- | :---- |
| F1 | Cadastrar produto | Sim | Heitor |
| F2 | Listar produtos | Sim | Gabi |
| F3 | Editar e excluir produto | Sim | Ian |
| F4 | Relatório de estoque (total investido) | Sim | Pedro |

---

## 🚫 Bloco 6 — Fora do escopo

- Pagamento
- Notificação
- Sincronização em nuvem
- Cálculo de margem de lucro / preço de venda

---

## ⚙️ Bloco 7 — Caminho técnico

**Opção escolhida:** (X) Opção A — Room: dados salvos no próprio celular

**Pode falhar:** Campo em branco ao tentar registrar um produto
**O usuário vê a mensagem:** "Um campo está vazio. Preencha todos para continuar."

---

## 🎨 Bloco 8 — Identidade visual

| Item | Definição do grupo |
| :---- | :---- |
| Nome exibido (strings.xml) | Atrás do Balcão |
| Cor principal (hex, em Color.kt) | #FFFC99 |
| Ideia do ícone (512×512) | Um balcão preto em um fundo amarelo com o nome do app em amarelo |
| `applicationId` | `br.edu.ifpe.atrasdobalcao` *(confirmar com o grupo — o código já implementado está usando `com.example.portrasdobalcao`)* |
| Versão inicial | 1.0 (versionCode 1) |

---

## 👤 Bloco 9 — Equipe, papéis e riscos

| Integrante | Papel principal | Responsável por |
| :---- | :---- | :---- |
| Ian | Dev / telas | Fazer as telas e as informações serem mostradas de forma organizada |
| Heitor | Dev / dados (Room) | Fazer o banco de dados e garantir que os dados sejam registrados e salvos de maneira apropriada |
| Gabi | Design e identidade visual | Criar uma identidade visual reconhecível e navegação intuitiva |
| Pedro | Documentação, build e entrega | Registrar as mudanças e funcionalidades do app e garantir que tudo vá de acordo com os planos |

Todos programam. O "papel" define quem responde por aquela parte, não quem trabalha sozinho.

**Riscos:**

| Risco | Plano B |
| :---- | :---- |
| Alguém falta num momento importante | Dividir as tarefas em partes pequenas, para outro do grupo conseguir assumir |
| O banco de dados (Room) dá erro perto do prazo | Deixar a parte de cadastrar produto funcionando cedo, antes de mexer no relatório |
| A testadora não consegue testar o app a tempo | Ter uma segunda pessoa reserva já combinada |
| Falta tempo para terminar tudo | Cortar o que está fora do escopo, focar no essencial |

---

## 🤖 Bloco 10 — Acordo de trabalho com IA

A implementação pode ser feita com o Gemini no Android Studio. Vocês orientam, ele digita — e cada integrante precisa saber explicar o que entrou no projeto. Regras completas em `docs/USO_DE_IA.md` e `AGENTS.md`.

**Três regras do `AGENTS.md`:**
- A IA não pode tomar decisões sem aprovação do grupo.
- Todos os integrantes devem compreender o código utilizado.
- Não adicionar ao código nada que não tenha sido pedido.

**Combinados do grupo:**
- Ninguém clica Accept no Agent Mode sem ler a mudança inteira.
- Quem aceitou o código escreve o comentário de fronteira do arquivo.
- Antes de cada marco, revisamos juntos: alguém aqui não entende alguma parte?
- Nenhuma chave de API ou senha vai para o prompt.

**Como garantimos que todos entendem tudo:** revisões semanais, com um relatório que cada integrante que adiciona uma funcionalidade escreve descrevendo o que fez.

---

## 🗓️ Bloco 11 — Marcos até 10/12

| Marco | Prazo | Como se comprova no GitHub |
| :---- | :---- | :---- |
| M1 — Canvas preenchido + repositório criado | 16/09 | CANVAS.md no main |
| M2 — PRD aprovado + telas rascunhadas | 30/09 | PRD.md + imagens em docs/ |
| M3 — Funcionalidade base rodando | 21/10 | tela principal lista dados + 1 ação + try/catch |
| M4 — Dados completos (Room) e erros tratados | 11/11 | commits da camada de dados |
| M5 — Identidade visual + .apk de release testado | 25/11 | ícone, cores, .apk testado por 2 pessoas de fora |
| M6 — .aab + material de loja + README.md | 02/12 | pasta loja/ + README.md completo |
| **Entrega e apresentação** | **10/12** | tag v1.0 no repositório |

---

## 🏁 Bloco 12 — Definição de pronto

- O app abre e não fecha sozinho depois de 5 minutos de uso.
- A tela principal mostra dados reais (não texto de exemplo fixo no código).
- A ação principal funciona e o resultado aparece na tela.
- Quando algo falha, aparece uma mensagem clara — o app não quebra.
- O app tem nome, ícone e cor próprios (nada de ícone padrão do Android).
- Duas pessoas de fora do grupo instalaram o .apk e conseguiram usar sem explicação.
- O README.md explica o que o app faz, com o que foi feito e como gerar o build.
- O docs/USO_DE_IA.md e o AGENTS.md estão preenchidos.
- Cada integrante consegue abrir o projeto e fazer uma mudança pequena sozinho.
- Todo arquivo nosso tem o comentário de fronteira escrito por nós.

---

## ✍️ Validação do professor

| | |
| :---- | :---- |
| **Data** | |
| **Situação** | ( ) Aprovado ( ) Aprovado com ajustes ( ) Refazer |
| **Observações** | |
