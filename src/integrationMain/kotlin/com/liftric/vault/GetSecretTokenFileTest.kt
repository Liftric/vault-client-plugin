package com.liftric.vault

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File

@Testcontainers
class GetSecretTokenFileTest : ContainerBase() {
    private val getSecretTestLocation = "build/getSecretTokenFileTest"

    @Test
    fun testGetSecretTokenFileTask() {
        val projectDir = File(getSecretTestLocation)
        projectDir.mkdirs()

        projectDir.resolve("settings.gradle.kts").writeText("")
        projectDir.resolve(".vault-token").writeText(VAULT_TOKEN)
        projectDir.resolve("build.gradle.kts").writeText(
            """
import com.liftric.vault.vault
import com.liftric.vault.GetVaultSecretTask

plugins {
    java
    id("com.liftric.vault-client-plugin")
}
vault {
    vaultAddress.set("$VAULT_ADDR")
    vaultTokenFilePath.set("${projectDir.path}/.vault-token")
    maxRetries.set(2)
    retryIntervalMilliseconds.set(200)
}
tasks {
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
        dependsOn(needsSecrets)
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
