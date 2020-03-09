package com.liftric.vault

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

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
        throw IllegalStateException("neither the env variable `VAULT_ADDR` nor the `vaultAddress` config set")
    }
    val tokenPresent = vault.vaultToken != null
    val filePathPresent = vault.vaultTokenFilePath != null

    val token = when {
        tokenPresent.not() && filePathPresent.not()
        -> throw IllegalStateException("neither vaultToken (prefer ENV VAR) nor vaultTokenFilePath set")
        tokenPresent && filePathPresent.not()
        -> vault.vaultToken!!
        filePathPresent && tokenPresent.not()
        -> getTokenFileContent(vault.vaultTokenFilePath!!)
        tokenPresent && filePathPresent
        -> {
            println("[warn] vault token and token file path set: will choose from file path!")
            getTokenFileContent(vault.vaultTokenFilePath!!)
        }
        else -> throw IllegalStateException("unknown error during token reading")
    }
    val client = VaultClient(vault, token)
    return client.get(secretPath)
}

private fun getTokenFileContent(filePath: String) = File(filePath).let {
    if (it.exists().not())
        throw IllegalStateException("no file found for given vaultTokenFilePath: ${it.absolutePath}")
    it.readText().trim()
}
