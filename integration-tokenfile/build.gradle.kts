import com.liftric.vault.vault

plugins {
    java
    id("com.liftric.vault-client-plugin") version "1.0.0-SNAPSHOT"
}
vault {
    vaultAddress = "http://localhost:8200"
    vaultTokenFilePath = "${projectDir.path}/.vault-token" // don't put the token file on the repo itself like here!
    maxRetries = 2
    retryIntervalMilliseconds = 200
}
tasks {
    val needsSecrets by creating {
        val secrets = project.objects.mapProperty<String, String>()
        doLast {
            secrets.set(project.vault("secret/example"))
            if (secrets.get()["examplestring"] != "helloworld") throw kotlin.IllegalStateException("examplestring couldn't be read")
            if (secrets.get()["exampleint"]?.toInt() != 1337) throw kotlin.IllegalStateException("exampleint couldn't be read")
            println("getting secrets succeeded!")
        }
    }
    val build by existing {
        dependsOn(needsSecrets)
    }
}
