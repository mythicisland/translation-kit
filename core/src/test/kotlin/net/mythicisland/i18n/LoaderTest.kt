package net.mythicisland.i18n

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.mythicisland.i18n.loader.PropertiesTranslationLoader
import java.nio.file.Files
import java.nio.file.Path
import java.util.Locale
import kotlin.io.path.createDirectories
import kotlin.io.path.createTempDirectory
import kotlin.io.path.writeText
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LoaderTest {

    private val directory: Path = createTempDirectory("i18n-loader")
    private val plain = PlainTextComponentSerializer.plainText()

    @AfterTest
    fun cleanup() {
        Files.walk(directory).use { paths -> paths.sorted(Comparator.reverseOrder()).forEach { Files.delete(it) } }
    }

    private fun translations(defaultLocale: Locale, vararg loaders: TranslationLoader): Translations {
        val builder = loaders.fold(Translations.builder()) { builder, loader -> builder.loader(loader) }
        val translations = builder.defaultLocale(defaultLocale).disableGlobalTranslator().build()
        translations.initialize()
        return translations
    }

    private fun text(translations: Translations, key: String, locale: Locale): String =
        plain.serialize(translations.translate(key, locale))

    @Test
    fun `properties files are loaded`() {
        directory.resolve("en.properties").writeText("welcome=Welcome!\numlaut=Grüße")
        translations(Locale.ENGLISH, PropertiesTranslationLoader(directory)).use { translations ->
            assertEquals("Welcome!", text(translations, "welcome", Locale.ENGLISH))
            assertEquals("Grüße", text(translations, "umlaut", Locale.ENGLISH))
        }
    }

    @Test
    fun `later files override earlier files of the same locale`() {
        val locale = directory.resolve("en").createDirectories()
        locale.resolve("a.properties").writeText("shared=A\nonly-a=Only A")
        locale.resolve("b.properties").writeText("shared=B")
        translations(Locale.ENGLISH, PropertiesTranslationLoader(directory)).use { translations ->
            assertEquals("B", text(translations, "shared", Locale.ENGLISH))
            assertEquals("Only A", text(translations, "only-a", Locale.ENGLISH))
        }
    }

    @Test
    fun `later loaders override earlier loaders`() {
        val first = TranslationLoader { mapOf(Locale.ENGLISH to mapOf("shared" to "First")) }
        val second = TranslationLoader { mapOf(Locale.ENGLISH to mapOf("shared" to "Second")) }
        translations(Locale.ENGLISH, first, second).use { translations ->
            assertEquals("Second", text(translations, "shared", Locale.ENGLISH))
        }
    }

    @Test
    fun `regions fall back to other regions of the same language`() {
        directory.resolve("en_US.properties").writeText("welcome=Welcome")
        directory.resolve("de_DE.properties").writeText("welcome=Willkommen")
        translations(Locale.US, PropertiesTranslationLoader(directory)).use { translations ->
            assertEquals("Willkommen", text(translations, "welcome", Locale.of("de", "AT")))
            assertEquals("Welcome", text(translations, "welcome", Locale.UK))
        }
    }

    @Test
    fun `file names that are no locales are skipped`() {
        directory.resolve("en.properties").writeText("welcome=Welcome")
        directory.resolve("messages.properties").writeText("welcome=Broken")
        translations(Locale.ENGLISH, PropertiesTranslationLoader(directory)).use { translations ->
            assertEquals(setOf(Locale.ENGLISH), translations.locales())
        }
    }
}
