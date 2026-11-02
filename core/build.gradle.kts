plugins {
    id("i18n.publishing-conventions")
    id("me.champeau.jmh")
}

dependencies {
    implementation(libs.caffeine)
    implementation(libs.slf4j.api)

    compileOnlyApi(libs.bundles.adventure)

    testImplementation(libs.bundles.adventure)

    jmhImplementation(libs.bundles.adventure)
}

maven {
    description = "A small and fast translation library based on Adventure MiniMessage"
}
