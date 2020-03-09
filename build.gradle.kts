import net.nemerosa.versioning.tasks.VersionDisplayTask

plugins {
    kotlin("jvm") version "1.3.61"
    `java-gradle-plugin`
    id("org.gradle.kotlin.kotlin-dsl") version "1.3.4"
    `maven-publish`
    id("com.gradle.plugin-publish") version "0.10.1"
    id("net.nemerosa.versioning") version "2.12.0"
}

group = "com.liftric.vault"
allprojects {
    version = with(versioning.info) {
        if (branch == "HEAD" && dirty.not()) {
            tag
        } else {
            full
        }
    }
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(gradleApi())
    implementation(kotlin("gradle-plugin"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.bettercloud:vault-java-driver:5.1.0")
    testImplementation(gradleTestKit())
    testImplementation("junit:junit:4.12")
    testImplementation("com.github.stefanbirkner:system-rules:1.19.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    withType<VersionDisplayTask> {
        doLast {
            println("[VersionDisplayTask] version=$version")
        }
    }
}
versioning {
    releaseMode = "tag"
    displayMode = "tag"
}
publishing {
    repositories {
        mavenLocal()
    }
}
gradlePlugin {
    plugins {
        create("VaultClientPlugin") {
            id = "com.liftric.vault-client-plugin"
            implementationClass = "com.liftric.vault.VaultClientPlugin"
            description = "Read and use vault secrets in your build script"
        }
    }
}
