package com.liftric.vault

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.invoke
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import javax.inject.Inject
import kotlin.reflect.KProperty

open class VaultSecretTask @Inject constructor(
    private val secretPath: String
) : DefaultTask() {

    init {
        group = "vault"
        description = "loads secret from vault"
    }

    @TaskAction
    fun loadSecret() {
        println("[vault] loading $secretPath")
        val vaultClientExtension = project.vault()
        if (vaultClientExtension.vaultAddress == null) {
            throw IllegalStateException("neither the env variable`VAULT_ADDR` nor the `vaultAddress` config set")
        }
        if (vaultClientExtension.vaultToken == null) {
            throw IllegalStateException("neither the env variable`VAULT_TOKEN` nor the `vaultToken` config set")
        }
        val client = VaultClient(vaultClientExtension)
        val secrets: Map<String, String> by project.extra(client.get(secretPath))
    }
}
