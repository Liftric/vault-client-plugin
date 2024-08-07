package com.liftric.vault

import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

class VaultClientPluginTest {

    private lateinit var project: org.gradle.api.Project

    @BeforeEach
    fun setup(@TempDir tempDir: Path) {
        project = ProjectBuilder.builder().withProjectDir(tempDir.toFile()).build()
    }

    @Test
    fun testApply() {
        project.pluginManager.apply("com.liftric.vault-client-plugin")
        assertNotNull(project.plugins.getPlugin(VaultClientPlugin::class.java))
    }

    @Test
    fun testExtension() {
        project.pluginManager.apply("com.liftric.vault-client-plugin")
        assertNotNull(project.vault())
    }
}