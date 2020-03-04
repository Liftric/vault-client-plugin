rootProject.name = "integration-test"
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "de.friday.elasticmq") {
                useModule("de.friday:elasticmq-gradle-plugin:${requested.version}")
            }
        }
    }
}
