package com.liftric.vault

import com.bettercloud.vault.VaultException
import junit.framework.TestCase.*
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.Test
import org.junit.contrib.java.lang.system.EnvironmentVariables

class VaultClientPluginTest {

    @get:Rule
    val environmentVariables = EnvironmentVariables()

    @Test
    fun testApply() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.liftric.vault-client-plugin")
        assertNotNull(project.plugins.getPlugin(VaultClientPlugin::class.java))
    }

    @Test
    fun testExtension() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.liftric.vault-client-plugin")
        assertNotNull(project.vault())
    }

    @Test
    fun testExtensionDefaults() {
        environmentVariables.clear("VAULT_ADDR", "VAULT_TOKEN")
        VaultClientExtension(ProjectBuilder.builder().build()).apply {
            assertNull(vaultAddress)
            assertNull(vaultToken)
        }
    }

    @Test
    fun testExtensionFromEnv() {
        environmentVariables.set("VAULT_ADDR", "aabb")
        environmentVariables.set("VAULT_TOKEN", "aacc")
        VaultClientExtension(ProjectBuilder.builder().build()).apply {
            assertEquals("aabb", vaultAddress)
            assertEquals("aacc", vaultToken)
        }
    }
}
