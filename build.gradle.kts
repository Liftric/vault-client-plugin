plugins {
    `kotlin-dsl`
    alias(libs.plugins.gradlePluginPublish)
    alias(libs.plugins.nemerosaVersioning)
}

group = "com.liftric.vault"
allprojects {
    version = with(versioning.info) {
        if (branch == "HEAD" && dirty.not()) {
            tag
        } else {
            full
        }
    }.also {
        println("version=$it")
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

sourceSets {
    val main by getting
    val integrationMain by creating {
        compileClasspath += main.output
        runtimeClasspath += main.output
    }
}


dependencies {
    implementation(gradleApi())
    implementation(kotlin("gradle-plugin"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(libs.javaVaultDriver)

    testImplementation(gradleTestKit())
    testImplementation(libs.junitJupiter)

    "integrationMainImplementation"(gradleTestKit())
    "integrationMainImplementation"(libs.junitJupiter)
    "integrationMainImplementation"(libs.javaVaultDriver)
    "integrationMainImplementation"(libs.testContainersJUnit5)
    "integrationMainImplementation"(libs.testContainersMain)
}


tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    val test by existing
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
        systemProperty("org.gradle.testkit.dir", gradle.gradleUserHomeDir)
    }

    register<Test>("integrationTest") {
        val integrationMain by sourceSets
        description = "Runs the integration tests"
        group = "verification"
        testClassesDirs = integrationMain.output.classesDirs
        classpath = integrationMain.runtimeClasspath
        mustRunAfter(test)
        useJUnitPlatform()
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}

gradlePlugin {
    website.set("https://github.com/Liftric/vault-client-plugin")
    vcsUrl.set("https://github.com/Liftric/vault-client-plugin")
    testSourceSets(sourceSets["integrationMain"])
    plugins {
        create("VaultClientPlugin") {
            id = "com.liftric.vault-client-plugin"
            displayName = "vault-client-plugin"
            implementationClass = "com.liftric.vault.VaultClientPlugin"
            description = "Read and use vault secrets in your build script"
            tags.set(listOf("vault", "hashicorp", "secret"))
        }
    }
}
