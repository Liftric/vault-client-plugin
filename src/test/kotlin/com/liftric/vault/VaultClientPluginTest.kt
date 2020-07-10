package com.liftric.vault

import junit.framework.TestCase.*
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

}
