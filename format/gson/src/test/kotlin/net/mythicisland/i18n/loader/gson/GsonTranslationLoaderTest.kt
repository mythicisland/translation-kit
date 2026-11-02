package net.mythicisland.i18n.loader.gson

import java.nio.file.Files
import java.nio.file.Path
import java.util.Locale
import kotlin.io.path.createTempDirectory
import kotlin.io.path.writeText
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GsonTranslationLoaderTest {

    private val directory: Path = createTempDirectory("i18n-gson")
    private val loader = GsonTranslationLoader(directory)

    @AfterTest
    fun cleanup() {
        Files.walk(directory).use { paths -> paths.sorted(Comparator.reverseOrder()).forEach { Files.delete(it) } }
    }

    @Test
    fun `nested keys and lists are flattened`() {
        directory.resolve("en.json").writeText(
            """
            {
              "welcome": "<green>Welcome <player>!",
              "messages": { "nested": { "key": "Nested" } },
              "lore": ["First line", "Second line"]
            }
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
    fun `empty files do not break other files`() {
        directory.resolve("en.json").writeText("""{ "welcome": "Welcome" }""")
        directory.resolve("de.json").writeText("")
        val loaded = loader.load()
        assertEquals(mapOf("welcome" to "Welcome"), loaded[Locale.ENGLISH])
        assertTrue(loaded[Locale.GERMAN].orEmpty().isEmpty())
    }
}
