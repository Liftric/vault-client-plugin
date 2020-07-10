package com.liftric.vault

object Defaults {
    // values
    const val MAX_RETRIES = 5
    const val RETRY_INTERVAL_MILLI = 1000

    // env vars
    const val VAULT_TOKEN_ENV = "VAULT_TOKEN"
    const val VAULT_TOKEN_FILE_PATH_ENV = "VAULT_TOKEN_FILE_PATH"
    const val VAULT_ADDR_ENV = "VAULT_ADDR"
}
