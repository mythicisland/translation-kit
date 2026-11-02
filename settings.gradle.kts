pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

rootProject.name = "translation-kit"

module("core")
module("yaml", "format/yaml")
module("gson", "format/gson")

fun module(name: String, path: String = name) {
    include("i18n-$name")
    project(":i18n-$name").projectDir = file(path)
}
