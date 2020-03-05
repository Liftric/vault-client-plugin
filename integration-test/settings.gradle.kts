rootProject.name = "integration-test"
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.liftric.vault-client-plugin") {
                useModule("com.liftric.vault:vault-client-plugin:${requested.version}")
            }
        }
    }
}
