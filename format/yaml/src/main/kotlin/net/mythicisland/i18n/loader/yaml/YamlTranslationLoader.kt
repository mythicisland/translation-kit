package net.mythicisland.i18n.loader.yaml

import net.mythicisland.i18n.loader.ConfigurateTranslationLoader
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.nio.file.Path

public class YamlTranslationLoader(directory: Path) : ConfigurateTranslationLoader(directory, setOf("yml", "yaml")) {

    override fun readTree(file: Path): Any? = YamlConfigurationLoader.builder().path(file).build().load().raw()
}
