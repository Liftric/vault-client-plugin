plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.2.1"
    id("net.nemerosa.versioning") version "3.1.0"
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
        println("version=$version")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation(kotlin("gradle-plugin"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.bettercloud:vault-java-driver:5.1.0")
    testImplementation(gradleTestKit())
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.github.stefanbirkner:system-rules:1.19.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
        }
    }

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
