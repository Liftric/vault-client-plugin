package com.liftric.vault

import org.gradle.api.Plugin
import org.gradle.api.Project

private const val extensionName = "vault"

class VaultClientPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create(extensionName, VaultClientExtension::class.java, project)
    }
}

fun Project.vault(): VaultClientExtension {
    return extensions.getByName(extensionName) as? VaultClientExtension
        ?: throw IllegalStateException("$extensionName is not of the correct type")
}

fun Project.vault(secretPath: String): Map<String, String> {
    println("[vault] loading $secretPath")
    val vault = project.vault()
    if (vault.vaultAddress == null) {
        throw IllegalStateException("neither the env variable`VAULT_ADDR` nor the `vaultAddress` config set")
    }
    if (vault.vaultToken == null) {
        throw IllegalStateException("neither the env variable`VAULT_TOKEN` nor the `vaultToken` config set")
    }
    val client = VaultClient(vault)
    return client.get(secretPath)
}
