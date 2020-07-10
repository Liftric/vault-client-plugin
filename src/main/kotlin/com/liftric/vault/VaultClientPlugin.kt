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
