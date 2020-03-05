package com.liftric.vault

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig

/**
 * Actual client connecting to the configured vault server
 */
class VaultClient(
    private val extension: VaultClientExtension
) {
    private val config by lazy {
        VaultConfig()
            .address(extension.vaultAddress)
            .token(extension.vaultToken)
            .build()
    }
    private val vault by lazy { Vault(config) }

    fun get(secretPath: String): Map<String, String> = try {
        vault.withRetries(extension.maxRetries, extension.retryIntervalMilliseconds)
            .logical()
            .read(secretPath)
            .data
    } catch (e: Exception) {
        println("[vault] exception while calling vault at ${extension.vaultAddress}: ${e.message}")
        if (extension.vaultAddress?.startsWith("https") == false) {
            println("[vault] is your vault address correct? It doesn't start with https!")
        }
        throw e
    }
}
