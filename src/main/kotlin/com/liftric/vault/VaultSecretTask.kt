package com.liftric.vault

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
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

    @OutputFile
    val secrets: MutableMap<String, String> = mutableMapOf()

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
        secrets.putAll(client.get(secretPath))
    }

    inline operator fun <reified T> getValue(nothing: Nothing?, property: KProperty<*>): T {
        return when (T::class.java) {
            String::class.java -> secrets[property.name]
                ?: throw IllegalStateException("Didn't find ${property.name} in secrets!")
            Int::class.java -> secrets[property.name]?.let { it.toInt() }
                ?: throw IllegalStateException("Didn't find ${property.name} in secrets!")
            Long::class.java -> secrets[property.name]?.let { it.toLong() }
                ?: throw IllegalStateException("Didn't find ${property.name} in secrets!")
            Boolean::class.java -> secrets[property.name]?.let { it.toBoolean() }
                ?: throw IllegalStateException("Didn't find ${property.name} in secrets!")
            Double::class.java -> secrets[property.name]?.let { it.toDouble() }
                ?: throw IllegalStateException("Didn't find ${property.name} in secrets!")
            else -> throw IllegalArgumentException("")
        } as T
    }
}
