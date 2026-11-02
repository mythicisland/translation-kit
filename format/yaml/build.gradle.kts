plugins {
    id("i18n.publishing-conventions")
}

dependencies {
    api(projects.i18nCore)
    implementation(libs.configurate.yaml)
}

maven {
    description = "YAML translation loader for translation-kit"
}
