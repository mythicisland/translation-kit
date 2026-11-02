import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("com.diffplug.spotless")
    id("dev.detekt")
}

val libs = the<LibrariesForLibs>()

dependencies {
    testImplementation(libs.kotlin.test)
}

kotlin {
    explicitApi()
    jvmToolchain(21)
    compilerOptions {
        languageVersion = KotlinVersion.KOTLIN_2_4
        apiVersion = KotlinVersion.KOTLIN_2_4
    }
}

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint(libs.versions.ktlint.get())
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint(libs.versions.ktlint.get())
    }
}

detekt {
    buildUponDefaultConfig = true
    parallel = true
    config.setFrom(rootDir.resolve(".detekt/detekt.yml"))
    source.setFrom("src/main/kotlin", "src/test/kotlin", "src/jmh/kotlin")
}
