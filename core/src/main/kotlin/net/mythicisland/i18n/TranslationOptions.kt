package net.mythicisland.i18n

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import java.util.Locale

@ConsistentCopyVisibility
public data class TranslationOptions internal constructor(
    public val loaders: List<TranslationLoader> = emptyList(),
    public val defaultLocale: Locale = Locale.ENGLISH,
    public val isCachingEnabled: Boolean = true,
    public val cacheMaximumSize: Long = 10_000,
    public val miniMessage: MiniMessage = MiniMessage.miniMessage(),
    public val tagResolver: TagResolver = TagResolver.empty(),
    public val name: Key = Key.key("i18n", "translations"),
    public val isGlobalTranslatorEnabled: Boolean = true,
)
