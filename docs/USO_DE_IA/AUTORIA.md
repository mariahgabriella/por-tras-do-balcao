# Autoria do projeto (pelo histórico do Git)

Revisão feita em 22/10/2026 a partir da lista de arquivos com o primeiro commit na `main`, dos merges de Pull Requests, das mensagens de commit e da contagem de commits por conta.

Contas que são a mesma pessoa, confirmadas pelo grupo: Heitor = Heitor Torres e `kansasgrind`; Ian = Ian Bomfim e `malmalbr`; Pedro = Pedro Freitas e `Perplay66`. A conta `mariahgabriella` é a da Mariah.

## Como ler

- **Criado na main por:** quem fez o primeiro commit do arquivo na `main`. Isso não prova que a pessoa escreveu todo o código: o arquivo pode ter nascido em outra branch ou ter sido alterado depois.
- **Explica (AGENTS.md):** quem responde pelo arquivo na apresentação, pela divisão por pasta do `AGENTS.md`: telas em `ui/` (Ian), dados em `data/` (Heitor), identidade visual e recursos (Mariah), build e artefatos de loja (Pedro). As pastas `model/` e `util/` e o `AppContainer.kt` não constam no `AGENTS.md`, e seguiram o relatório de prompts (Heitor). A `MainActivity.kt` foi tratada como tela (Ian). A documentação foi atribuída ao Pedro, responsável por ela.
- "Mesma pessoa?" compara os dois campos. "Não" não é erro: significa que quem explica não foi quem criou o arquivo, e por isso precisa estudá-lo.

## Resumo

Commits na consulta (`git shortlog -sn --all`): Mariah 33; Heitor 29 (27 + 2 da conta `kansasgrind`); Ian 14 (7 + 7 da conta `malmalbr`); Pedro 10 (7 + 3 da conta `Perplay66`). Os números ficam inflados porque há pares de commits com mensagem idêntica em contas diferentes, que parecem o mesmo trabalho registrado duas vezes.

Arquivos criados na `main`, por pessoa (entre os 65 arquivos do relatório de prompts que tiveram data identificada):

- Heitor: 23 arquivos
- Mariah: 21 arquivos
- Pedro: 13 arquivos
- Ian: 8 arquivos

Em 42 desses arquivos, quem criou não é quem explica.

## O que a revisão encontrou

1. **O commit `4ae8afa` é uma consolidação.** Ele tem a mensagem "Base do projeto com todos os arquivos necessários..." e reúne cerca de 30 arquivos na conta `kansasgrind` (Heitor) em 07/10. Para Login, Perfil, navegação, `Usuario`, `UsuarioDao`, `SessaoManager` e `AppDatabase`, existem commits individuais do Heitor com essas mensagens, o que confirma a autoria dele. Para `UsuarioRepository`, `SenhaHasher`, `AppContainer`, `Utilitarios`, `MainActivity` e `SplashScreen` não há commit individual visível, então a autoria dele é provável, mas precisa de confirmação.
2. **Entidades feitas em paralelo.** Produto aparece em commits da Mariah (`392a667`, `1a29293`) e da conta `malmalbr` (`a6d96a2`). Fornecedor aparece em commits do Pedro (`b52ce46`, `0532097`) e da conta `malmalbr` (`c484232`). Estoque é do Ian (`cec7d15`, `1b359dd`, `c010670`). As versões que estão na `main` são: Produto da Mariah, Fornecedor do Pedro e Estoque do Ian.
3. **`AppDatabase.kt`.** Aparece como criado pela Mariah (`392a667`), mas o Heitor tem um commit chamado "AppDataBase" (`fc0f2fb`). Provavelmente os dois trabalharam nele.
4. **Splash e Sobre.** A Mariah tem o commit `f692b05` ("Adicionar splash screen e tela Sobre com navegação inicial"), mas na `main` a `AboutScreen.kt` aparece criada pelo Pedro (`0532097`) e a `SplashScreen.kt` na consolidação do Heitor. Pode ter havido reescrita ou perda de histórico por mudança de pasta. Vale conferir com `git blame`.
5. **Alterações depois da criação.** Pedro alterou o `Produto` ao remover `precoVenda` e `margemLucro` (`4124248`) para alinhar com o PRD, e atualizou Canvas e PRD (`9096bba`). Ian alterou o `ProdutoFormScreen.kt` (`e61d2d3`) e ajustou diretórios e configurações (`c010670`, `d595b9b`). Heitor moveu o código para `src/` (`690eff7`) e organizou `docs/` (`7371560`). Ian moveu PRD, Rubrica e Canvas para `docs/` (`004d452`).
6. **Origem do código base.** O pacote se chamava `petshop` antes de virar `portrasdobalcao` (commits de Mariah e Heitor). Vale registrar no `README.md` ou no `USO_DE_IA.md` de onde veio a base do projeto, se foi de um modelo ou projeto anterior.
7. **Quem explica não é quem criou.** Os maiores casos: telas de Login, Perfil, Splash e navegação foram criadas pelo Heitor, mas a divisão atribui `ui/` ao Ian; Produto, `ModelosUi`, `TemaPreferences`, `ProdutoDao`, `ProdutoRepository` e as telas de produto e relatório foram criados pela Mariah, mas `data/` e `model/` ficam com o Heitor. O `AGENTS.md` exige que cada um saiba explicar sua parte, então o grupo deve combinar uma revisão cruzada antes da apresentação.
8. **Documentação.** Canvas, PRD, `AGENTS.md` e `USO_DE_IA.md` são do Pedro. A versão técnica do `AGENTS.md` foi reescrita pelo Ian (`e894486`). O `README.md` foi criado pela Mariah e atualizado pelo Pedro. `GUIA_GITHUB.md`, `RUBRICA.md`, `loja/descricao.md`, `loja/privacidade.md` e a pasta `data/remote/` aparecem pela primeira vez na `main` no commit `0532097` do Pedro (07/10, "Adiciona documentação, Fornecedor e tela Sobre").
9. **Gabi e Mariah.** O `AGENTS.md` lista "Gabi" como responsável pela identidade visual, e o commit `5de231c` da conta `mariahgabriella` trocou esse nome por "Mariah" no `USO_DE_IA.md`. Confirme se o `AGENTS.md` também deve ser atualizado.

## Tabela por arquivo

Caminhos relativos a `app/src/main/java/com/example/portrasdobalcao/` (os de recursos começam com `res/`).

| Arquivo | Criado na main por | Data | Commit | Explica (AGENTS.md) | Mesma pessoa? |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `.gitignore` | Mariah (conta mariahgabriella) | 2026-09-09 | `dd81cd7` | Pedro (build) | não |
| `AGENTS.md` | Pedro | 2026-09-23 | `5d6fb3f` | Pedro (documentação) | sim |
| `README.md` | Mariah (conta mariahgabriella) | 2026-09-09 | `dd81cd7` | Pedro (documentação) | não |
| `app/.gitignore` | Heitor (conta kansasgrind) | 2026-10-07 | `4ae8afa` | Pedro (build) | não |
| `app/build.gradle.kts` | Heitor | 2026-09-30 | `9dc2132` | Pedro (build) | não |
| `AppContainer.kt` | Heitor (conta kansasgrind) | 2026-10-07 | `4ae8afa` | Heitor (dados) | sim |
| `MainActivity.kt` | Heitor (conta kansasgrind) | 2026-10-07 | `4ae8afa` | Ian (telas) | não |
| `data/AppDatabase.kt` | Mariah (conta mariahgabriella) | 2026-10-06 | `392a667` | Heitor (dados) | não |
| `data/ProdutoDao.kt` | Mariah (conta mariahgabriella) | 2026-10-07 | `1a29293` | Heitor (dados) | não |
| `data/local/SessaoManager.kt` | Heitor | 2026-10-06 | `99d0ea7` | Heitor (dados) | sim |
| `data/local/TemaPreferences.kt` | Mariah (conta mariahgabriella) | 2026-10-07 | `1a29293` | Heitor (dados) | não |
| `data/remote/placeholder` | Pedro | 2026-10-07 | `0532097` | Heitor (dados) | não |
| `data/repository/EstoqueRepository.kt` | Ian | 2026-10-08 | `c010670` | Heitor (dados) | não |
| `data/repository/ProdutoRepository.kt` | Mariah (conta mariahgabriella) | 2026-10-07 | `1a29293` | Heitor (dados) | não |
| `data/repository/UsuarioRepository.kt` | Heitor (conta kansasgrind) | 2026-10-07 | `4ae8afa` | Heitor (dados) | sim |
| `data/seguranca/SenhaHasher.kt` | Heitor (conta kansasgrind) | 2026-10-07 | `4ae8afa` | Heitor (dados) | sim |
| `model/Estoque.kt` | Ian | 2026-10-08 | `c010670` | Heitor (dados) | não |
| `model/EstoqueDao.kt` | Ian | 2026-10-08 | `c010670` | Heitor (dados) | não |
| `model/Fornecedor.kt` | Pedro | 2026-10-07 | `0532097` | Heitor (dados) | não |
| `model/ModelosUi.kt` | Mariah (conta mariahgabriella) | 2026-10-07 | `1a29293` | Heitor (dados) | não |
| `model/Produto.kt` | Mariah (conta mariahgabriella) | 2026-10-07 | `1a29293` | Heitor (dados) | não |
| `model/Usuario.kt` | Heitor | 2026-10-06 | `95ce0bd` | Heitor (dados) | sim |
| `model/UsuarioDao.kt` | Heitor | 2026-10-06 | `d741c25` | Heitor (dados) | sim |
| `ui/components/Componentes.kt` | Mariah (conta mariahgabriella) | 2026-10-07 | `1a29293` | Ian (telas) | não |
| `ui/features/about/AboutScreen.kt` | Pedro | 2026-10-07 | `0532097` | Ian (telas) | não |
| `ui/features/estoque/EstoqueScreen.kt` | Ian | 2026-10-08 | `c010670` | Ian (telas) | sim |
| `ui/features/estoque/EstoqueViewModel.kt` | Ian | 2026-10-08 | `c010670` | Ian (telas) | sim |
| `ui/features/fornecedor/FornecedorScreen.kt` | Pedro | 2026-10-07 | `0532097` | Ian (telas) | não |
| `ui/features/login/LoginScreen.kt` | Heitor (conta kansasgrind) | 2026-10-07 | `4ae8afa` | Ian (telas) | não |
| `ui/features/login/LoginViewModel.kt` | Heitor (conta kansasgrind) | 2026-10-07 | `4ae8afa` | Ian (telas) | não |
| `ui/features/perfil/PerfilScreen.kt` | Heitor (conta kansasgrind) | 2026-10-07 | `4ae8afa` | Ian (telas) | não |
| `ui/features/perfil/PerfilViewModel.kt` | Heitor (conta kansasgrind) | 2026-10-07 | `4ae8afa` | Ian (telas) | não |
| `ui/features/principal/PrincipalScreen.kt` | Mariah (conta mariahgabriella) | 2026-10-07 | `1a29293` | Ian (telas) | não |
| `ui/features/principal/ProdutoViewModel.kt` | Mariah (conta mariahgabriella) | 2026-10-07 | `1a29293` | Ian (telas) | não |
| `ui/features/produto/ProdutoFormScreen.kt` | Mariah (conta mariahgabriella) | 2026-10-07 | `1a29293` | Ian (telas) | não |
| `ui/features/produto/ProdutoFormViewModel.kt` | Mariah (conta mariahgabriella) | 2026-10-07 | `1a29293` | Ian (telas) | não |
| `ui/features/relatorio/RelatorioScreen.kt` | Mariah (conta mariahgabriella) | 2026-10-07 | `1a29293` | Ian (telas) | não |
| `ui/features/relatorio/RelatorioViewModel.kt` | Mariah (conta mariahgabriella) | 2026-10-07 | `1a29293` | Ian (telas) | não |
| `ui/features/splash/SplashScreen.kt` | Heitor (conta kansasgrind) | 2026-10-07 | `4ae8afa` | Ian (telas) | não |
| `ui/navigation/NavGraph.kt` | Heitor (conta kansasgrind) | 2026-10-07 | `4ae8afa` | Ian (telas) | não |
| `ui/navigation/NavTarget.kt` | Heitor (conta kansasgrind) | 2026-10-07 | `4ae8afa` | Ian (telas) | não |
| `ui/theme/Color.kt` | Mariah (conta mariahgabriella) | 2026-10-07 | `1a29293` | Mariah (identidade visual) | sim |
| `ui/theme/Theme.kt` | Mariah (conta mariahgabriella) | 2026-10-07 | `1a29293` | Mariah (identidade visual) | sim |
| `ui/theme/Type.kt` | Mariah (conta mariahgabriella) | 2026-10-07 | `1a29293` | Mariah (identidade visual) | sim |
| `util/Utilitarios.kt` | Heitor (conta kansasgrind) | 2026-10-07 | `4ae8afa` | Heitor (dados) | sim |
| `app/src/main/keepRules/rules.keep` | Heitor (conta kansasgrind) | 2026-10-07 | `4ae8afa` | Pedro (build) | não |
| `res/drawable/ic_launcher_background.xml` | Heitor (conta kansasgrind) | 2026-10-07 | `4ae8afa` | Mariah (identidade visual e recursos) | não |
| `res/drawable/ic_launcher_foreground.xml` | Heitor (conta kansasgrind) | 2026-10-07 | `4ae8afa` | Mariah (identidade visual e recursos) | não |
| `res/drawable/ic_logo_balcao.xml` | Mariah (conta mariahgabriella) | 2026-10-07 | `1a29293` | Mariah (identidade visual e recursos) | sim |
| `res/values/strings.xml` | Ian | 2026-10-08 | `c010670` | Mariah (identidade visual e recursos) | não |
| `res/values/strings_auth.xml` | Heitor (conta kansasgrind) | 2026-10-07 | `4ae8afa` | Mariah (identidade visual e recursos) | não |
| `res/values/strings_comum.xml` | Mariah (conta mariahgabriella) | 2026-10-07 | `1a29293` | Mariah (identidade visual e recursos) | sim |
| `res/values/strings_estoque.xml` | Ian | 2026-10-08 | `c010670` | Mariah (identidade visual e recursos) | não |
| `res/values/strings_perfil.xml` | Heitor (conta kansasgrind) | 2026-10-07 | `4ae8afa` | Mariah (identidade visual e recursos) | não |
| `res/values/strings_produto.xml` | Mariah (conta mariahgabriella) | 2026-10-07 | `1a29293` | Mariah (identidade visual e recursos) | sim |
| `res/values/strings_sobre.xml` | Pedro | 2026-10-07 | `0532097` | Mariah (identidade visual e recursos) | não |
| `res/values/themes.xml` | Ian | 2026-10-08 | `c010670` | Mariah (identidade visual e recursos) | não |
| `docs/CANVAS.md` | Pedro | 2026-09-16 | `33bbac1` | Pedro (documentação) | sim |
| `docs/GUIA_GITHUB.md` | Pedro | 2026-10-07 | `0532097` | Pedro (documentação) | sim |
| `docs/PRD.md` | Pedro | 2026-09-23 | `5d6fb3f` | Pedro (documentação) | sim |
| `docs/RUBRICA.md` | Pedro | 2026-10-07 | `0532097` | Pedro (documentação) | sim |
| `docs/USO_DE_IA/USO_DE_IA.md` | Pedro | 2026-10-21 | `2b95c30` | Pedro (documentação) | sim |
| `docs/loja/descricao.md` | Pedro | 2026-10-07 | `0532097` | Pedro (documentação) | sim |
| `docs/loja/privacidade.md` | Pedro | 2026-10-07 | `0532097` | Pedro (documentação) | sim |
| `gradle/libs.versions.toml` | Heitor (conta kansasgrind) | 2026-10-07 | `4ae8afa` | Pedro (build) | não |

## Linhas por autor (git blame)

Contagem feita em 22/10/2026 com `git blame` sobre os arquivos `.kt`, `.xml` e `.kts` da `main`, num total de 4978 linhas:

| Pessoa | Nomes que aparecem no blame | Linhas | Parte do total |
| :--- | :--- | :--- | :--- |
| Heitor | `kansasgrind` (2229) + Heitor Torres (174) | 2403 | 48,3% |
| Mariah | `mariahgabriella` (1944) | 1944 | 39,1% |
| Ian | Ian Bomfim (456) + `malmalbr` (1) | 457 | 9,2% |
| Pedro | Pedro Freitas (174) | 174 | 3,5% |

Como ler:

- O blame mostra quem fez a última alteração de cada linha que existe hoje na `main`. Não mede esforço nem qualidade, e não conta documentação, `.toml`, `.properties` nem os arquivos de PRD, Canvas, `AGENTS.md` e `USO_DE_IA.md`, que são, em grande parte, trabalho do Pedro.
- A parte do Heitor inclui o commit de consolidação `4ae8afa`, que traz também arquivos que parecem vir do modelo do Android Studio (manifest, testes de exemplo, regras de backup, ícones adaptativos). Sem a contagem por arquivo, não dá para separar o que é código dele do que é arquivo gerado pelo modelo.
- A conta `malmalbr` aparece com uma única linha.
- A distribuição é desigual: 87% das linhas estão com Heitor e Mariah. Como o `AGENTS.md` exige que todos saibam explicar o que entrou no projeto, vale o grupo combinar uma revisão cruzada, principalmente de Ian e Pedro sobre as partes que não escreveram, e de todos sobre Login, Perfil e dados.

## Limites desta revisão

- A tabela por arquivo mostra quem fez o primeiro commit na `main`, e o blame mostra quem tem a última alteração de cada linha. Nenhum dos dois prova quem pensou ou escreveu o código: se uma IA gerou o texto e uma pessoa fez o commit, o Git só registra a pessoa.
- Não há nenhum comentário de fronteira nos arquivos de código, só nos documentos.
- O log de commits que li tinha os 60 mais recentes de 86, e a contagem de commits por conta pode estar inflada por commits repetidos em contas diferentes.

Para ver as linhas arquivo por arquivo, rode no Git Bash e consulte o resultado em `~/blame_autores.txt`:

```bash
for f in $(git ls-files | grep -E '\.(kt|xml|kts)$'); do echo "== $f"; git blame --line-porcelain -w "$f" | sed -n 's/^author //p' | sort | uniq -c | sort -rn; done > ~/blame_autores.txt
```
