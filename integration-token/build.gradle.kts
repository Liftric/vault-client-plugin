import com.liftric.vault.vault
import com.liftric.vault.GetVaultSecretTask

plugins {
    java
    id("com.liftric.vault-client-plugin") // version known from buildSrc
}
vault {
    vaultAddress.set("http://localhost:8200")
    vaultToken.set("myroottoken") // don't do that in production code!
    maxRetries.set(2)
    retryIntervalMilliseconds.set(200)
}
val configTimeSecrets: Map<String, String> = vault("secret/example")
tasks {
    val needsSecretsConfigTime by creating {
        doLast {
            if (configTimeSecrets["examplestring"] != "helloworld") throw kotlin.IllegalStateException("examplestring couldn't be read")
            if (configTimeSecrets["exampleint"]?.toInt() != 1337) throw kotlin.IllegalStateException("exampleint couldn't be read")
            println("getting secrets succeeded!")
        }
    }
    val needsSecrets by creating(GetVaultSecretTask::class) {
        secretPath.set("secret/example")
        doLast {
            val secret = secret.get()
            if (secret["examplestring"] != "helloworld") throw kotlin.IllegalStateException("examplestring couldn't be read")
            if (secret["exampleint"]?.toInt() != 1337) throw kotlin.IllegalStateException("exampleint couldn't be read")
            println("getting secret succeeded!")
        }
    }
    val fromBuildSrc by creating {
        doLast {
            if (with(Configs) { secretStuff() != "helloworld:1337" }) throw kotlin.IllegalStateException("config with secret couldn't be read")
        }
    }
    val build by existing {
        dependsOn(needsSecretsConfigTime, needsSecrets, fromBuildSrc)
    }
}
