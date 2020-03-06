package com.liftric.vault

import org.gradle.api.Project

open class VaultClientExtension(val project: Project) {
    var vaultAddress: String? = System.getenv()["VAULT_ADDR"]
    var vaultToken: String? = System.getenv()["VAULT_TOKEN"]
    var vaultTokenFilePath: String? = System.getenv()["VAULT_TOKEN_FILE_PATH"]
    var maxRetries: Int = 5
    var retryIntervalMilliseconds = 1000
}
