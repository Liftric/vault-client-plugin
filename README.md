# vault-client-plugin (gradle plugin)

As of 2020-03-04 this is a heavy work in progress!

# Usage
The vault address and token will be read from the env:
`VAULT_ADDR` `VAULT_TOKEN`

You can specify them in the vault configuration as well, but that should only be used for testing and is highly discouraged.

Usage example in you build script:
```
## build scripts
```kotlin
import com.liftric.vault.vault

plugins {
    id("com.liftric.vault-client-plugin") version ("<latest>")
}
vault {
    vaultAddress = "http://localhost:8200"
    vaultToken = "myroottoken" // don't do that in production code!
    maxRetries = 2
    retryIntervalMilliseconds = 200
}
tasks {
    val needsSecrets by creating {
        val secrets = project.objects.mapProperty<String, String>()
    }
}
```

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