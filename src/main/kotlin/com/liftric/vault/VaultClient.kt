package com.liftric.vault

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import com.bettercloud.vault.VaultException

/**
 * Actual client connecting to the configured vault server
 */
class VaultClient(
    token: String,
    private val vaultAddress: String,
    private val maxRetries: Int,
    private val retryIntervalMilliseconds: Int,
    private val namespace: String?
) {
    private val config by lazy {
        try {
            VaultConfig()
                .address(vaultAddress)
                .token(token)
                .build()
        } catch (e: VaultException) {
            println("[vault] exception while creating vault client for $vaultAddress: ${e.message}")
            throw e
        }
    }
    private val vault by lazy {
        try {
            Vault(config)
        } catch (e: VaultException) {
            println(
                "[vault] exception while preparing vault client config for $vaultAddress: ${e.message} - token valid?"
                    .replace('\n', '#')
            )
            throw e
        }
    }

    fun get(secretPath: String): Map<String, String> {
        verifyTokenValid()
        return try {
            vault.withRetries(maxRetries, retryIntervalMilliseconds)
                 .logical()
                 .withNameSpace(namespace)
                 .read(secretPath)
                 .data.also {
                     if (it.isEmpty()) error("[vault] secret response contains no data - secret exists? token has correct rights to access it?")
                 }
        } catch (e: VaultException) {
            println(
                "[vault] exception while calling vault at $vaultAddress: ${e.message} - secret exists? token has correct rights to access it?"
                    .replace('\n', '#')
            )
            if (vaultAddress.startsWith("https").not()) {
                println("[vault] is your vault address correct? It doesn't start with https!")
            }
            throw e
        }
    }

    private fun verifyTokenValid() {
        try {
            vault.auth().lookupSelf()
        } catch (e: VaultException) {
            println(
                "[vault] exception while verifying vault token validity for $vaultAddress: ${e.message}"
                    .replace('\n', '#')
            )
            throw e
        }
    }
}
