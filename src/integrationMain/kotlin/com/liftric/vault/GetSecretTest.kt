package com.liftric.vault

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File

@Testcontainers
class GetSecretTest : ContainerBase() {
    private val getSecretTestLocation = "build/getSecretTest"

    @Test
    fun testGetSecretTask() {
        val projectDir = File(getSecretTestLocation)
        projectDir.mkdirs()

        projectDir.resolve("settings.gradle.kts").writeText("")
        projectDir.resolve("build.gradle.kts").writeText(
            """
import com.liftric.vault.vault
import com.liftric.vault.GetVaultSecretTask

plugins {
    java
    id("com.liftric.vault-client-plugin")
}

group = "com.liftric.test"
version = "1.0.1"

vault {
    vaultAddress.set("$VAULT_ADDR")
    vaultToken.set("$VAULT_TOKEN")
    maxRetries.set(2)
    retryIntervalMilliseconds.set(200)
}
val configTimeSecrets: Map<String, String> = vault("secret/example")
tasks {
    val needsSecretsConfigTime by creating {
        doLast {
            if (configTimeSecrets["examplestring"] != "helloworld") throw kotlin.IllegalStateException("examplestring couldn't be read")
            if (configTimeSecrets["exampleint"]?.toInt() != 1337) throw kotlin.IllegalStateException("exampleint couldn't be read")
            println("getting secrets succeeded!")
        }
    }
    val needsSecrets by creating(GetVaultSecretTask::class) {
        secretPath.set("secret/example")
        doLast {
            val secret = secret.get()
            if (secret["examplestring"] != "helloworld") throw kotlin.IllegalStateException("examplestring couldn't be read")
            if (secret["exampleint"]?.toInt() != 1337) throw kotlin.IllegalStateException("exampleint couldn't be read")
            println("getting secret succeeded!")
        }
    }
    val build by existing {
        dependsOn(needsSecretsConfigTime, needsSecrets)
    }
}
        """
        )

        val result = GradleRunner.create()
            .withProjectDir(projectDir)
            .withArguments("build")
            .withPluginClasspath()
            .build()

        assertTrue(result.output.contains("BUILD SUCCESSFUL"))
    }
}
