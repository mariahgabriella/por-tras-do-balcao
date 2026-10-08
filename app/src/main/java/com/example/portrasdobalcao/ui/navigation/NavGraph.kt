package com.example.portrasdobalcao.ui.navigation

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.portrasdobalcao.AppContainer
import com.example.portrasdobalcao.R
import com.example.portrasdobalcao.ui.features.estoque.EstoqueScreen
import com.example.portrasdobalcao.ui.features.estoque.EstoqueViewModel
import com.example.portrasdobalcao.ui.features.login.LoginScreen
import com.example.portrasdobalcao.ui.features.login.LoginViewModel
import com.example.portrasdobalcao.ui.features.perfil.PerfilScreen
import com.example.portrasdobalcao.ui.features.perfil.PerfilViewModel
import com.example.portrasdobalcao.ui.features.principal.PrincipalScreen
import com.example.portrasdobalcao.ui.features.principal.ProdutoViewModel
import com.example.portrasdobalcao.ui.features.produto.ConclusaoForm
import com.example.portrasdobalcao.ui.features.produto.ProdutoFormScreen
import com.example.portrasdobalcao.ui.features.produto.ProdutoFormViewModel
import com.example.portrasdobalcao.ui.features.relatorio.RelatorioScreen
import com.example.portrasdobalcao.ui.features.relatorio.RelatorioViewModel
import com.example.portrasdobalcao.ui.features.about.AboutScreen
import com.example.portrasdobalcao.ui.features.splash.SplashScreen
import com.example.portrasdobalcao.util.fabrica

private data class ItemBarra(val rota: String, val rotulo: Int, val icone: ImageVector)

private val itensBarra = listOf(
    ItemBarra(NavTarget.Principal.route, R.string.nav_produtos, Icons.Outlined.Inventory2),
    ItemBarra(NavTarget.Estoque.route, R.string.nav_estoque, Icons.Outlined.Category),
    ItemBarra(NavTarget.Relatorio.route, R.string.nav_relatorio, Icons.Outlined.BarChart),
    ItemBarra(NavTarget.Perfil.route, R.string.nav_perfil, Icons.Outlined.Person),
)

/** Vai para uma aba da barra inferior sem empilhar telas repetidas. */
private fun NavController.irParaAba(rota: String) {
    navigate(rota) {
        popUpTo(NavTarget.Principal.route) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

/** Abre uma rota limpando toda a pilha (Splash/Login -> Principal, Sair -> Login). */
private fun NavController.irLimpandoPilha(rota: String) {
    navigate(rota) {
        popUpTo(graph.id) { inclusive = true }
        launchSingleTop = true
    }
}

@Composable
fun NavGraph(container: AppContainer) {
    val navController = rememberNavController()
    val entradaAtual by navController.currentBackStackEntryAsState()
    val rotaAtual = entradaAtual?.destination?.route
    // Barra inferior oculta em splash e login.
    val mostrarBarra = rotaAtual != null && rotaAtual != NavTarget.Splash.route && rotaAtual != NavTarget.Login.route

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (mostrarBarra) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceVariant) {
                    itensBarra.forEach { item ->
                        NavigationBarItem(
                            selected = rotaAtual == item.rota,
                            onClick = { navController.irParaAba(item.rota) },
                            icon = { Icon(item.icone, contentDescription = null) },
                            label = { Text(stringResource(item.rotulo)) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = Color.Transparent,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                        )
                    }
                }
            }
        },
    ) { padding ->
        val modificador = if (mostrarBarra) Modifier.padding(padding).consumeWindowInsets(padding) else Modifier

        NavHost(
            navController = navController,
            startDestination = NavTarget.Splash.route,
            modifier = modificador,
        ) {
            // ---------- Splash ----------
            composable(NavTarget.Splash.route) {
                val logado = remember { container.sessaoManager.estaLogado() }
                SplashScreen(
                    logado = logado,
                    onIrParaPrincipal = { navController.irLimpandoPilha(NavTarget.Principal.route) },
                    onIrParaLogin = { navController.irLimpandoPilha(NavTarget.Login.route) },
                )
            }

            // ---------- Login ----------
            composable(NavTarget.Login.route) {
                val vm: LoginViewModel = viewModel(factory = fabrica { LoginViewModel(container.usuarioRepository, container.sessaoManager) })
                val estado by vm.estado.collectAsState()
                LoginScreen(
                    estado = estado,
                    onNomeLojaChange = vm::onNomeLojaChange,
                    onNomeGestorChange = vm::onNomeGestorChange,
                    onCategoriaChange = vm::onCategoriaChange,
                    onSenhaChange = vm::onSenhaChange,
                    onAlternarSenhaVisivel = vm::onAlternarSenhaVisivel,
                    onEnviar = vm::onEnviar,
                    onTentarDeNovo = vm::verificarUsuario,
                    onLoginConcluido = { navController.irLimpandoPilha(NavTarget.Principal.route) },
                )
            }

            // ---------- Principal ----------
            composable(NavTarget.Principal.route) { entrada ->
                val vm: ProdutoViewModel = viewModel(factory = fabrica { ProdutoViewModel(container.produtoRepository) })
                val estado by vm.estado.collectAsState()

                // Mensagem enviada pela tela de cadastro/edição ao voltar.
                val mensagem by entrada.savedStateHandle
                    .getStateFlow<String?>(NavTarget.CHAVE_MENSAGEM, null)
                    .collectAsState()
                LaunchedEffect(mensagem) {
                    mensagem?.let {
                        vm.mostrarMensagemDoFormulario(it)
                        entrada.savedStateHandle[NavTarget.CHAVE_MENSAGEM] = null
                    }
                }

                PrincipalScreen(
                    estado = estado,
                    onBuscaChange = vm::onBuscaChange,
                    onNovoProduto = { navController.navigate(NavTarget.ProdutoNovo.route) },
                    onEditarProduto = { id -> navController.navigate(NavTarget.ProdutoEditar.criarRota(id)) },
                    onAbrirRelatorio = { navController.irParaAba(NavTarget.Relatorio.route) },
                    onSolicitarExclusao = vm::solicitarExclusao,
                    onConfirmarExclusao = vm::confirmarExclusao,
                    onCancelarExclusao = vm::cancelarExclusao,
                    onDesfazerExclusao = vm::desfazerExclusao,
                    onSnackbarConsumida = vm::snackbarConsumida,
                    onTentarDeNovo = vm::carregar,
                )
            }

            // ---------- Cadastro / edição de produto ----------
            composable(NavTarget.ProdutoNovo.route) {
                RotaProdutoForm(container, navController, produtoId = null)
            }
            composable(
                route = NavTarget.ProdutoEditar.route,
                arguments = listOf(navArgument(NavTarget.ProdutoEditar.ARG_ID) { type = NavType.LongType }),
            ) { entrada ->
                RotaProdutoForm(container, navController, produtoId = entrada.arguments?.getLong(NavTarget.ProdutoEditar.ARG_ID))
            }

            // ---------- Relatório ----------
            composable(NavTarget.Relatorio.route) {
                val vm: RelatorioViewModel = viewModel(factory = fabrica { RelatorioViewModel(container.produtoRepository) })
                val estado by vm.estado.collectAsState()
                RelatorioScreen(
                    estado = estado,
                    onVoltar = { navController.popBackStack() },
                    onTentarDeNovo = vm::carregar,
                )
            }

            // ---------- Estoque ----------
            composable(NavTarget.Estoque.route) {
                val vm: EstoqueViewModel = viewModel(factory = fabrica { EstoqueViewModel(container.produtoRepository) })
                val estado by vm.estado.collectAsState()
                EstoqueScreen(
                    estado = estado,
                    onFiltroBaixoChange = vm::onFiltroBaixoChange,
                    onItemClick = vm::abrirMovimentacao,
                    onFecharMovimentacao = vm::fecharMovimentacao,
                    onTipoChange = vm::onTipoChange,
                    onQuantidadeChange = vm::onQuantidadeChange,
                    onConfirmar = vm::confirmar,
                    onResultadoConsumido = vm::resultadoConsumido,
                    onTentarDeNovo = vm::carregar,
                )
            }

            // ---------- Perfil ----------
            composable(NavTarget.Perfil.route) {
                val vm: PerfilViewModel = viewModel(
                    factory = fabrica {
                        PerfilViewModel(
                            container.usuarioRepository,
                            container.produtoRepository,
                            container.temaPreferences,
                            container.sessaoManager,
                        )
                    }
                )
                val estado by vm.estado.collectAsState()
                PerfilScreen(
                    estado = estado,
                    onFotoEscolhida = vm::onFotoEscolhida,
                    onTemaChange = vm::onTemaChange,
                    onAbrirEditarDados = vm::abrirEditarDados,
                    onAbrirAlterarSenha = vm::abrirAlterarSenha,
                    onAbrirSobre = { navController.navigate(NavTarget.About.route) },
                    onPedirSair = vm::pedirSair,
                    onConfirmarSair = vm::confirmarSair,
                    onFecharDialogo = vm::fecharDialogo,
                    onEditNomeLojaChange = vm::onEditNomeLojaChange,
                    onEditGestorChange = vm::onEditGestorChange,
                    onEditCategoriaChange = vm::onEditCategoriaChange,
                    onSalvarDados = vm::salvarDados,
                    onSenhaAtualChange = vm::onSenhaAtualChange,
                    onSenhaNovaChange = vm::onSenhaNovaChange,
                    onSenhaConfirmarChange = vm::onSenhaConfirmarChange,
                    onSalvarSenha = vm::salvarSenha,
                    onMensagemConsumida = vm::mensagemConsumida,
                    onIrParaLogin = { navController.irLimpandoPilha(NavTarget.Login.route) },
                    onTentarDeNovo = vm::tentarDeNovo,
                )
            }

            // ---------- Sobre ----------
            composable(NavTarget.About.route) {
                val contexto = LocalContext.current
                val versao = remember {
                    runCatching { contexto.packageManager.getPackageInfo(contexto.packageName, 0).versionName }
                        .getOrNull().orEmpty()
                }
                AboutScreen(
                    versao = versao,
                    integrantes = stringArrayResource(R.array.sobre_integrantes).toList(),
                    onVoltar = { navController.popBackStack() },
                )
            }
        }
    }
}

@Composable
private fun RotaProdutoForm(
    container: AppContainer,
    navController: NavController,
    produtoId: Long?,
) {
    val vm: ProdutoFormViewModel = viewModel(
        key = "produto_form_$produtoId",
        factory = fabrica { ProdutoFormViewModel(container.produtoRepository, produtoId) },
    )
    val estado by vm.estado.collectAsState()

    ProdutoFormScreen(
        estado = estado,
        onCampoChange = vm::onCampoChange,
        onCampoPerdeuFoco = vm::onCampoPerdeuFoco,
        onSalvar = vm::salvar,
        onCancelar = { navController.popBackStack() },
        onPedirExclusao = vm::pedirExclusao,
        onConfirmarExclusao = vm::confirmarExclusao,
        onCancelarExclusao = vm::cancelarExclusao,
        onMensagemConsumida = vm::mensagemConsumida,
        onConcluido = { conclusao ->
            val tipo = if (conclusao == ConclusaoForm.SALVO) ProdutoViewModel.MSG_SALVO else ProdutoViewModel.MSG_EXCLUIDO
            navController.previousBackStackEntry?.savedStateHandle?.set(NavTarget.CHAVE_MENSAGEM, tipo)
            navController.popBackStack()
        },
        onTentarCarregarDeNovo = vm::tentarCarregarDeNovo,
    )
}
