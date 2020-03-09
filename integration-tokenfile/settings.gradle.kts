rootProject.name = "integration-tokenfile"
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.liftric.vault-client-plugin") {
                val vaultClientPluginVersion = file("../build/version").readText().trim()
                useModule("com.liftric.vault:vault-client-plugin:${vaultClientPluginVersion}")
            }
        }
    }
}
