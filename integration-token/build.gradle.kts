import com.liftric.vault.vault

plugins {
    java
    id("com.liftric.vault-client-plugin") // version known from buildSrc
}
vault {
    vaultAddress = "http://localhost:8200"
    vaultToken = "myroottoken" // don't do that in production code!
    maxRetries = 2
    retryIntervalMilliseconds = 200
}
tasks {
    val needsSecrets by creating {
        doLast {
            val secrets: Map<String, String> = project.vault("secret/example")
            if (secrets["examplestring"] != "helloworld") throw kotlin.IllegalStateException("examplestring couldn't be read")
            if (secrets["exampleint"]?.toInt() != 1337) throw kotlin.IllegalStateException("exampleint couldn't be read")
            println("getting secrets succeeded!")
        }
    }
    val fromBuildSrc by creating {
        doLast {
            if (with(Configs) { secretStuff() != "helloworld:1337" }) throw kotlin.IllegalStateException("config with secret couldn't be read")
        }
    }
    val build by existing {
        dependsOn(needsSecrets, fromBuildSrc)
    }
}
