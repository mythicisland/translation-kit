package net.mythicisland.i18n.loader.yaml

import java.nio.file.Files
import java.nio.file.Path
import java.util.Locale
import kotlin.io.path.createDirectories
import kotlin.io.path.createTempDirectory
import kotlin.io.path.writeText
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class YamlTranslationLoaderTest {

    private val directory: Path = createTempDirectory("i18n-yaml")
    private val loader = YamlTranslationLoader(directory)

    @AfterTest
    fun cleanup() {
        Files.walk(directory).use { paths -> paths.sorted(Comparator.reverseOrder()).forEach { Files.delete(it) } }
    }

    @Test
    fun `nested keys and lists are flattened`() {
        directory.resolve("en.yml").writeText(
            """
            welcome: "<green>Welcome <player>!"
            messages:
              nested:
                key: "Nested"
            lore:
              - "First line"
              - "Second line"
            """.trimIndent(),
        )
        val expected = mapOf(
            "welcome" to "<green>Welcome <player>!",
            "messages.nested.key" to "Nested",
            "lore" to "First line\nSecond line",
        )
        assertEquals(expected, loader.load()[Locale.ENGLISH])
    }

    @Test
    fun `both extensions and locale directories are loaded`() {
        directory.resolve("en.yml").writeText("welcome: \"Welcome\"")
        directory.resolve("de.yaml").writeText("welcome: \"Willkommen\"")
        directory.resolve("fr").createDirectories().resolve("messages.yml").writeText("welcome: \"Bienvenue\"")
        val loaded = loader.load()
        assertEquals(setOf(Locale.ENGLISH, Locale.GERMAN, Locale.FRENCH), loaded.keys)
        assertEquals("Bienvenue", loaded[Locale.FRENCH]?.get("welcome"))
    }

    @Test
    fun `empty files do not break other files`() {
        directory.resolve("en.yml").writeText("welcome: \"Welcome\"")
        directory.resolve("de.yml").writeText("")
        val loaded = loader.load()
        assertEquals(mapOf("welcome" to "Welcome"), loaded[Locale.ENGLISH])
        assertTrue(loaded[Locale.GERMAN].orEmpty().isEmpty())
    }
}
