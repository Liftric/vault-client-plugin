package com.liftric.vault

import com.liftric.vault.Defaults.MAX_RETRIES
import com.liftric.vault.Defaults.RETRY_INTERVAL_MILLI
import com.liftric.vault.Defaults.VAULT_ADDR_ENV
import com.liftric.vault.Defaults.VAULT_TOKEN_ENV
import com.liftric.vault.Defaults.VAULT_TOKEN_FILE_PATH_ENV
import org.gradle.api.DefaultTask
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.mapProperty
import org.gradle.kotlin.dsl.property
import java.io.File

/**
 * See extension for property documentation
 */
open class GetVaultSecretTask : DefaultTask() {
    init {
        group = "com.liftric.vault"
        description = "Fetches a secret from vault."
        outputs.upToDateWhen { false }
    }

    @Input
    val secretPath: Property<String> = project.objects.property()

    @Input
    @Optional
    val vaultAddress: Property<String> = project.objects.property()

    @Input
    @Optional
    val vaultToken: Property<String> = project.objects.property()

    @Input
    @Optional
    val vaultTokenFilePath: Property<String> = project.objects.property()

    @Input
    @Optional
    val maxRetries: Property<Int> = project.objects.property()

    @Input
    @Optional
    val retryIntervalMilliseconds: Property<Int> = project.objects.property()

    @Internal
    // actually used as output...
    val secret: MapProperty<String, String> = project.objects.mapProperty()

    @TaskAction
    fun execute() {
        val token = determineToken(vaultToken = vaultToken.orNull, vaultTokenFilePath = vaultTokenFilePath.orNull)
        val address = determinAddress(vaultAddress = vaultAddress.orNull)
        val maxRetries = maxRetries.getOrElse(MAX_RETRIES)
        val retryIntervalMilliseconds = retryIntervalMilliseconds.getOrElse(RETRY_INTERVAL_MILLI)
        val path = secretPath.get()
        println("[vault] getting `$path` from $address")
        secret.set(
            VaultClient(
                token = token,
                vaultAddress = address,
                maxRetries = maxRetries,
                retryIntervalMilliseconds = retryIntervalMilliseconds
            ).get(path)
        )
    }

    companion object {
        fun determineToken(vaultToken: String?, vaultTokenFilePath: String?): String {
            val finalVaultToken = vaultToken ?: System.getenv()[VAULT_TOKEN_ENV]?.trim()
            val finalVaultTokenFilePath = vaultTokenFilePath ?: System.getenv()[VAULT_TOKEN_FILE_PATH_ENV]?.trim()
            return when {
                finalVaultToken != null -> finalVaultToken.trim()
                finalVaultTokenFilePath != null -> File(finalVaultTokenFilePath).apply {
                    if (exists().not()) error("vault token file doesn't exist!")
                }.let {
                    return@let it.readText().trim()
                }
                else -> error("neither `vaultToken` nor `vaultTokenFilePath` nor `$VAULT_TOKEN_FILE_PATH_ENV` env var nor `$VAULT_TOKEN_ENV` env var provided!")
            }
        }

        fun determinAddress(vaultAddress: String?): String {
            val finalVaultAddress = vaultAddress ?: System.getenv()[VAULT_ADDR_ENV]
            return finalVaultAddress?.trim() ?: error("neither `vaultAddress` nor `$VAULT_ADDR_ENV` env var provided!")
        }
    }
}
