# vault-client-plugin (gradle plugin)
![GitHub](https://img.shields.io/github/license/Liftric/vault-client-plugin)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/Liftric/vault-client-plugin)
[![CircleCI](https://circleci.com/gh/Liftric/vault-client-plugin/tree/master.svg?style=svg)](https://circleci.com/gh/Liftric/vault-client-plugin/tree/master)

# Usage
The vault address and token will be read from the env:
`VAULT_ADDR` `VAULT_TOKEN`

You can also specify a path to a file containing the token (vault login - CLI- automatically creates this).
This path can also be specified via env var: `VAULT_TOKEN_FILE_PATH`

You can specify the token in the vault configuration as well (see below), but that should only be used for testing 
and is highly discouraged. The vaultAddress and vaultTokenFilePath configs should be fine though.

Usage example in you build script:

```kotlin
import com.liftric.vault.vault
import com.liftric.vault.GetVaultSecretTask

plugins {
    id("com.liftric.vault-client-plugin") version ("<latest>")
}
vault {
    vaultAddress.set("http://localhost:8200")
    vaultToken.set("myroottoken") // don't do that in production code!
    vaultTokenFilePath.set("${System.getProperty("user.home")}/.vault-token") // from file is prefered over vaultToken 
    maxRetries.set(2)
    retryIntervalMilliseconds.set(200)
}
tasks {
    val needsSecrets by creating(GetVaultSecretTask::class) {
        secretPath.set("secret/example")
        doLast {
            val secrets: Map<String, String> = secret.get()
        }
    }
}
```

See the `integration-` sub projects for working examples.

## configuration
After the plugin is applied, the vault extension is registered with the following settings: 

Property | Description | default value 
---|---|---
vaultAddress | vault address to be used | result of `System.getenv()["VAULT_ADDR"]`
vaultToken | vault token to be used (it's recommend you don't set this in your build file) | result of `System.getenv()["VAULT_TOKEN"]`
vaultTokenFilePath | vault token file path (if set has precedence over vaultToken) | result of `System.getenv()["VAULT_TOKEN_FILE_PATH"]`
maxRetries | vault client max retry count | 5
retryIntervalMilliseconds | time between vault request retries | 1000

## buildSrc
You can also use the plugin directly in you buildSrc code:
```kotlin
plugins {
    `kotlin-dsl`
}
repositories {
    jcenter()
}
dependencies {
    implementation("com.liftric.vault:vault-client-plugin:<latest>")
}
```

You'll need access to the project in your buildSrc code:
```kotlin
import com.liftric.vault.vault
import org.gradle.api.Project

object Configs {
    fun Project.secretStuff(): Any {
        val secrets = project.vault("secret/example")
        [...] // use the secrets
    }
    fun Project.secretStuff(): Any {
        val needsSecrets: GetVaultSecretTask = tasks.getByName<GetVaultSecretTask>("needsSecrets").apply {
            execute()
        }
        val secret = needsSecrets.secret.get()
        [...] // use the secrets
    }
}
```
This can be used in your build scripts like:
```kotlin
with(Configs) {
    val secretString = secretStuff() 
    [...] // use it
}
```

See `integration-token` for an example.
