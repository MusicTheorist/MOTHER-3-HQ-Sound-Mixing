package org.musictheorist.mother3hqaudiopatcher.resources;

import java.util.EnumSet;
import java.util.Locale;

public enum Languages {
              ENGLISH("langEnglish",          Locale.of("en", "US")),
               FRENCH("langFrench",           Locale.of("fr", "FR")),
    PORTUGUESE_BRAZIL("langPortugueseBrazil", Locale.of("pt", "BR")),
              ITALIAN("langItalian",          Locale.of("it", "IT"));
    //JAPANESE("langJapanese", Locale.JAPANESE);

    private final String langKey;
    private final Locale locale;

    private static final String baseName = "lang/patcher";
    private static final Languages[] allLangs = new Languages[Languages.values().length];

    private Languages(String langKey, Locale locale) {
        this.langKey = langKey;
        this.locale = locale;
    }

    static {
        int i = 0;
        for(Languages language : EnumSet.allOf(Languages.class)) {
            allLangs[i] = language;
            i++;
        }
    }

    public String langKey() {
        return langKey;
    }
    public Locale locale() {
        return locale;
    }

    public static String baseName() {
        return baseName;
    }
    public static Languages[] allLangs() {
        return allLangs;
    }
}