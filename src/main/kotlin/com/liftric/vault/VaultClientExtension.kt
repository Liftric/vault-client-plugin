package com.liftric.vault

import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.kotlin.dsl.property

open class VaultClientExtension(project: Project) {
    /**
     * vault address to be used. will check env var `VAULT_ADDR` if unset
     */
    @Input
    @Optional
    val vaultAddress: Property<String> = project.objects.property()

    /**
     * vault token to be used (it's recommend you don't set this in your build file). will check env var `VAULT_TOKEN` if unset
     */
    @Input
    @Optional
    val vaultToken: Property<String> = project.objects.property()

    /**
     * vault token file path (if set has precedence over vaultToken). will check env var `VAULT_TOKEN_FILE_PATH` if unset
     */
    @Input
    @Optional
    val vaultTokenFilePath: Property<String> = project.objects.property()

    /**
     * vault client max retry count
     */
    @Input
    @Optional
    val maxRetries: Property<Int> = project.objects.property()

    /**
     * time between vault request retries
     */
    @Input
    @Optional
    val retryIntervalMilliseconds: Property<Int> = project.objects.property()
}
