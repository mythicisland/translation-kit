package net.mythicisland.i18n.loader

import org.slf4j.LoggerFactory
import java.nio.file.Path

public abstract class ConfigurateTranslationLoader(
    directory: Path,
    extensions: Set<String>,
) : AbstractTranslationLoader(directory, extensions) {

    private val logger = LoggerFactory.getLogger(ConfigurateTranslationLoader::class.java)

    protected abstract fun readTree(file: Path): Any?

    override fun readMessages(file: Path): Map<String, String> {
        val messages = flatten(readTree(file), "")
        val duplicates = messages.groupingBy { it.first }.eachCount().filterValues { it > 1 }.keys
        if (duplicates.isNotEmpty()) {
            logger.warn("Translation file {} contains the keys {} multiple times", file, duplicates)
        }
        return messages.toMap()
    }

    private fun flatten(value: Any?, path: String): List<Pair<String, String>> = when {
        value is Map<*, *> -> value.flatMap { (key, child) -> flatten(child, join(path, key.toString())) }
        value is List<*> -> listOf(path to value.joinToString("\n") { it?.toString().orEmpty() })
        path.isEmpty() || value == null -> emptyList()
        else -> listOf(path to value.toString())
    }

    private fun join(path: String, key: String) = if (path.isEmpty()) key else "$path.$key"
}
