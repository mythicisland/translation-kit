plugins {
    id("i18n.publishing-conventions")
}

dependencies {
    api(projects.i18nCore)
    implementation(libs.configurate.gson)
}

maven {
    description = "JSON translation loader for translation-kit"
}
