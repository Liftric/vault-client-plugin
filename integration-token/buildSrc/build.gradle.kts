plugins {
    `kotlin-dsl`
}
repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
}
dependencies {
    val vaultClientPluginVersion = file("../../build/version").readText().trim()
    implementation("com.liftric.vault:vault-client-plugin:$vaultClientPluginVersion")
}
