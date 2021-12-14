plugins {
    `kotlin-dsl`
}
repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
}
dependencies {
    val vaultClientPluginVersion = file("../../build/version").readText().trim()
    implementation("com.liftric.vault:vault-client-plugin:$vaultClientPluginVersion")
}
