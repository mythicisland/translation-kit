package net.mythicisland.i18n.loader.gson

import net.mythicisland.i18n.loader.ConfigurateTranslationLoader
import org.spongepowered.configurate.gson.GsonConfigurationLoader
import java.nio.file.Path

public class GsonTranslationLoader(directory: Path) : ConfigurateTranslationLoader(directory, setOf("json")) {

    override fun readTree(file: Path): Any? = GsonConfigurationLoader.builder().path(file).build().load().raw()
}
