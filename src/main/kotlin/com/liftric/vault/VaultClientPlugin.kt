package com.liftric.vault

import org.gradle.api.Plugin
import org.gradle.api.Project

private const val extensionName = "vault"

class VaultClientPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create(extensionName, VaultClientExtension::class.java, project)
        project.tasks.withType(GetVaultSecretTask::class.java) {
            project.afterEvaluate {
                vaultAddress.set(extension.vaultAddress)
                vaultToken.set(extension.vaultToken)
                vaultTokenFilePath.set(extension.vaultTokenFilePath)
                maxRetries.set(extension.maxRetries)
                retryIntervalMilliseconds.set(extension.retryIntervalMilliseconds)
            }
        }
    }
}

fun Project.vault(): VaultClientExtension {
    return extensions.getByName(extensionName) as? VaultClientExtension
        ?: throw IllegalStateException("$extensionName is not of the correct type")
}

fun Project.vault(secretPath: String): Map<String, String> {
    val extension: VaultClientExtension = vault()
    val token = GetVaultSecretTask.determineToken(
        vaultToken = extension.vaultToken.orNull,
        vaultTokenFilePath = extension.vaultTokenFilePath.orNull
    )
    val address = GetVaultSecretTask.determinAddress(vaultAddress = extension.vaultAddress.orNull)
    val maxRetries = extension.maxRetries.getOrElse(Defaults.MAX_RETRIES)
    val retryIntervalMilliseconds = extension.retryIntervalMilliseconds.getOrElse(Defaults.RETRY_INTERVAL_MILLI)
    val namespace = extension.namespace.orNull
    println("[vault] getting `$secretPath` from $address")

    return VaultClient(
        token = token,
        vaultAddress = address,
        maxRetries = maxRetries,
        retryIntervalMilliseconds = retryIntervalMilliseconds,
        namespace = namespace
    ).get(secretPath)
}
