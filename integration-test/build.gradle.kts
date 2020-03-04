import com.liftric.vault.VaultSecretTask
import kotlin.reflect.KProperty

plugins {
    id("com.liftric.vault-client-plugin") version "1.0.0-SNAPSHOT"
}
vault {
    vaultAddress = "http://0.0.0.0:8200"
    vaultToken = "myroottoken" // don't do that in production code!
}
tasks {
    val vaultExample = create("vaultExample", VaultSecretTask::class, "secret/example")
    val needsSecrets by creating {
        dependsOn(vaultExample)
        val secrets: Map<String, String> by vaultExample.extra
        doLast {
            if (secrets["examplestring"] != "helloworld") throw kotlin.IllegalStateException("examplestring couldn't be read")
            if (secrets["exampleint"]?.toInt() != 1337) throw kotlin.IllegalStateException("exampleint couldn't be read")
        }
    }
}
