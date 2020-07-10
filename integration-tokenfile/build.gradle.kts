import com.liftric.vault.vault
import com.liftric.vault.GetVaultSecretTask

plugins {
    java
    id("com.liftric.vault-client-plugin")
}
vault {
    vaultAddress.set("http://localhost:8200")
    vaultTokenFilePath.set("${projectDir.path}/.vault-token") // don't put the token file on the repo itself like here!
    maxRetries.set(2)
    retryIntervalMilliseconds.set(200)
}
tasks {
    val needsSecrets by creating(GetVaultSecretTask::class) {
        secretPath.set("secret/example")
        doLast {
            val secret = secret.get()
            if (secret["examplestring"] != "helloworld") throw kotlin.IllegalStateException("examplestring couldn't be read")
            if (secret["exampleint"]?.toInt() != 1337) throw kotlin.IllegalStateException("exampleint couldn't be read")
            println("getting secret succeeded!")
        }
    }
    val build by existing {
        dependsOn(needsSecrets)
    }
}
