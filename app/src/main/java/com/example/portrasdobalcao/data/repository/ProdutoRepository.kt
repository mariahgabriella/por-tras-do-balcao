package com.example.portrasdobalcao.data.repository

import androidx.room.withTransaction
import com.example.portrasdobalcao.data.AppDatabase
import com.example.portrasdobalcao.model.Estoque
import com.example.portrasdobalcao.model.Produto
import com.example.portrasdobalcao.model.ProdutoItem
import com.example.portrasdobalcao.model.ResultadoOperacao
import com.example.portrasdobalcao.model.TipoMovimentacao
import com.example.portrasdobalcao.util.operacaoSegura
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlin.coroutines.cancellation.CancellationException

/** Junta Produto + Estoque e concentra as operações de banco (todas com try/catch). */
class ProdutoRepository(private val db: AppDatabase) {
    private val produtoDao = db.produtoDao()
    private val estoqueDao = db.estoqueDao()

    fun observarItens(): Flow<List<ProdutoItem>> =
        combine(produtoDao.observarTodos(), estoqueDao.listarTodos()) { produtos, estoques ->
            val porProduto = estoques.associateBy { it.produtoId }
            produtos.map { p ->
                val e = porProduto[p.id]
                ProdutoItem(
                    id = p.id,
                    nome = p.nome,
                    categoria = p.categoria,
                    quantidade = e?.quantidade ?: 0,
                    quantidadeMinima = e?.quantidadeMinima ?: 0,
                    precoCusto = p.precoCusto,
                )
            }
        }

    suspend fun buscarItem(id: Long): Result<ProdutoItem?> = operacaoSegura {
        val p = produtoDao.buscarPorId(id) ?: return@operacaoSegura null
        val e = estoqueDao.buscarPorProduto(id)
        ProdutoItem(
            id = p.id,
            nome = p.nome,
            categoria = p.categoria,
            quantidade = e?.quantidade ?: 0,
            quantidadeMinima = e?.quantidadeMinima ?: 0,
            precoCusto = p.precoCusto,
        )
    }

    /** Cria (id == null) ou atualiza o produto e o estoque na mesma transação. */
    suspend fun salvar(
        id: Long?,
        nome: String,
        categoria: String,
        quantidade: Int,
        minima: Int,
        custo: Double,
    ): Result<Long> = operacaoSegura {
        db.withTransaction {
            val produtoId = if (id == null) {
                produtoDao.inserir(
                    Produto(nome = nome, categoria = categoria, precoCusto = custo, quantidadeEstoque = quantidade)
                )
            } else {
                val atual = produtoDao.buscarPorId(id) ?: error("Produto não encontrado")
                produtoDao.atualizar(
                    atual.copy(nome = nome, categoria = categoria, precoCusto = custo, quantidadeEstoque = quantidade)
                )
                id
            }
            gravarEstoque(produtoId, quantidade, minima)
            produtoId
        }
    }

    suspend fun excluir(id: Long): Result<Unit> = operacaoSegura {
        db.withTransaction {
            val p = produtoDao.buscarPorId(id)
            if (p != null) produtoDao.deletar(p)
        }
    }

    /** Usado pelo "Desfazer": recria o produto com o mesmo id e o estoque que ele tinha. */
    suspend fun restaurar(item: ProdutoItem): Result<Unit> = operacaoSegura {
        db.withTransaction {
            produtoDao.inserir(
                Produto(
                    id = item.id,
                    nome = item.nome,
                    categoria = item.categoria,
                    precoCusto = item.precoCusto,
                    quantidadeEstoque = item.quantidade,
                )
            )
            gravarEstoque(item.id, item.quantidade, item.quantidadeMinima)
        }
    }

    /** Entrada soma, saída subtrai; a saída nunca deixa a quantidade negativa. */
    suspend fun movimentar(produtoId: Long, tipo: TipoMovimentacao, quantidade: Int): ResultadoOperacao {
        if (quantidade <= 0) return ResultadoOperacao.QuantidadeInvalida
        return try {
            db.withTransaction<ResultadoOperacao> {
                val atual = estoqueDao.buscarPorProduto(produtoId)
                    ?: return@withTransaction ResultadoOperacao.Erro
                val novo = when (tipo) {
                    TipoMovimentacao.ENTRADA -> Math.addExact(atual.quantidade, quantidade)
                    TipoMovimentacao.SAIDA -> atual.quantidade - quantidade
                }
                if (novo < 0) {
                    ResultadoOperacao.EstoqueInsuficiente(atual.quantidade)
                } else {
                    estoqueDao.atualizar(atual.copy(quantidade = novo))
                    // mantém Produto.quantidadeEstoque igual ao Estoque
                    produtoDao.buscarPorId(produtoId)?.let { produtoDao.atualizar(it.copy(quantidadeEstoque = novo)) }
                    ResultadoOperacao.Sucesso
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            ResultadoOperacao.Erro
        }
    }

    private suspend fun gravarEstoque(produtoId: Long, quantidade: Int, minima: Int) {
        val atual = estoqueDao.buscarPorProduto(produtoId)
        if (atual == null) {
            estoqueDao.inserir(Estoque(produtoId = produtoId, quantidade = quantidade, quantidadeMinima = minima))
        } else {
            estoqueDao.atualizar(atual.copy(quantidade = quantidade, quantidadeMinima = minima))
        }
    }
}
