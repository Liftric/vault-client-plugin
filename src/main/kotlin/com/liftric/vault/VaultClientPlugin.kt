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
