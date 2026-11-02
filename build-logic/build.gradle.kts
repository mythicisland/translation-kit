plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(plugin(libs.plugins.kotlin.jvm))
    implementation(plugin(libs.plugins.jmh))
    implementation(plugin(libs.plugins.kraken))
    implementation(plugin(libs.plugins.spotless))
    implementation(plugin(libs.plugins.detekt))
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

fun plugin(plugin: Provider<PluginDependency>): Provider<String> =
    plugin.map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version.requiredVersion}" }
