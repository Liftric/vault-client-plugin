plugins {
    `kotlin-dsl`
}
repositories {
    jcenter()
    mavenLocal()
}
dependencies {
    val vaultClientPluginVersion = file("../../build/version").readText().trim()
    implementation("com.liftric.vault:vault-client-plugin:$vaultClientPluginVersion")
}
