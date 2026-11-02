package net.mythicisland.i18n

import java.util.Locale

public fun interface TranslationLoader {

    public fun load(): Map<Locale, Map<String, String>>
}
