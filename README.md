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

plugins {
    id("com.liftric.vault-client-plugin") version ("<latest>")
}
vault {
    vaultAddress = "http://localhost:8200"
    vaultToken = "myroottoken" // don't do that in production code!
    vaultTokenFilePath = "${System.getProperty("user.home")}/.vault-token" // from file is prefered over vaultToken 
    maxRetries = 2
    retryIntervalMilliseconds = 200
}
tasks {
    val needsSecrets by creating {
        val secrets: Map<String, String> = project.vault("secret/example")
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
    fun Project.secretStuff(): String {
        val secrets = project.vault("secret/example")
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
