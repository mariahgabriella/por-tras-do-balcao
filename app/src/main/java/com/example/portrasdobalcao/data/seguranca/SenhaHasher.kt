package com.example.portrasdobalcao.data.seguranca

import java.security.MessageDigest
import java.security.SecureRandom

/** Hash SHA-256 com salt. Formato guardado: "sha256:<salt hex>:<hash hex>". */
object SenhaHasher {
    private const val PREFIXO = "sha256"
    private const val SEPARADOR = ":"

    fun estaEmHash(armazenado: String): Boolean = armazenado.startsWith("$PREFIXO$SEPARADOR")

    fun gerarHash(senha: String): String {
        val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
        return listOf(PREFIXO, paraHex(salt), paraHex(calcular(salt, senha))).joinToString(SEPARADOR)
    }

    fun verificar(senha: String, armazenado: String): Boolean {
        val partes = armazenado.split(SEPARADOR)
        if (partes.size != 3 || partes[0] != PREFIXO) return false
        val salt = deHex(partes[1]) ?: return false
        val esperado = deHex(partes[2]) ?: return false
        return MessageDigest.isEqual(calcular(salt, senha), esperado)
    }

    private fun calcular(salt: ByteArray, senha: String): ByteArray {
        val digest = MessageDigest.getInstance("SHA-256")
        digest.update(salt)
        return digest.digest(senha.toByteArray(Charsets.UTF_8))
    }

    private fun paraHex(bytes: ByteArray): String = bytes.joinToString("") { "%02x".format(it) }

    private fun deHex(hex: String): ByteArray? {
        if (hex.length % 2 != 0) return null
        return try {
            ByteArray(hex.length / 2) { hex.substring(it * 2, it * 2 + 2).toInt(16).toByte() }
        } catch (e: NumberFormatException) {
            null
        }
    }
}
