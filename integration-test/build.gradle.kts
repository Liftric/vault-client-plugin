import com.liftric.vault.vault

plugins {
    java
    id("com.liftric.vault-client-plugin") version "1.0.0-SNAPSHOT"
}
vault {
    vaultAddress = "http://localhost:8200"
    vaultToken = "myroottoken" // don't do that in production code!
    maxRetries = 2
    retryIntervalMilliseconds = 200
}
tasks {
    val needsSecrets by creating {
        val secrets = project.vault("secret/example")
        doLast {
            if (secrets["examplestring"] != "helloworld") throw kotlin.IllegalStateException("examplestring couldn't be read")
            if (secrets["exampleint"]?.toInt() != 1337) throw kotlin.IllegalStateException("exampleint couldn't be read")
        }
    }
    val build by existing {
        dependsOn(needsSecrets)
    }
}
