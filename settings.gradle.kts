rootProject.name = "vault-client-plugin"

pluginManagement {
    dependencyResolutionManagement {
        versionCatalogs {
            create("libs") {
                version("junit", "5.9.3")
                version("testContainers", "1.18.3")

                plugin("gradlePluginPublish", "com.gradle.plugin-publish").version("1.2.1")
                plugin("nemerosaVersioning", "net.nemerosa.versioning").version("3.1.0")

                library("junitBom", "org.junit", "junit-bom").versionRef("junit")
                library("junitJupiter", "org.junit.jupiter", "junit-jupiter").versionRef("junit")
                library("testContainersJUnit5", "org.testcontainers", "junit-jupiter").versionRef("testContainers")
                library("testContainersMain", "org.testcontainers", "testcontainers").versionRef("testContainers")
            }
        }
    }
}

