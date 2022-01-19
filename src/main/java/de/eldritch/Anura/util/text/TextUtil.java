package de.eldritch.Anura.util.text;

import de.eldritch.Anura.Anura;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;

public class TextUtil {
    private static final String RESOURCE_PATH = "lang";
    private static final String EXTERNAL_PATH = "lang";

    private static TextUtil singleton;

    private final HashMap<Language, LangData> data = new HashMap<>();

    private TextUtil() { }

    private Text get0(@NotNull String key, @NotNull Language language, String... format) throws IllegalLanguageException, NullPointerException {
        if (!data.containsKey(language)) {
            try {
                this.load0(language);
            } catch (IOException | NullPointerException e) {
                throw new IllegalLanguageException("Language not found: " + language.getCode(), e);
            }
        }

        // return formatted Text object
        String str = data.get(language).get(key);
        if (str == null)
            throw new NullPointerException("Key has no value: " + key);
        return new Text(language, str.formatted((Object[]) format));
    }

    private void load0(@NotNull Language language) throws IOException, NullPointerException {
        // try to load from external file
        File file = new File(new File(Anura.singleton.getDirectory(), EXTERNAL_PATH), "lang-" + language.getCode() + ".properties");
        if (file.exists() && file.isFile()) {
            LangData langData = new LangData(new FileReader(file));
            data.put(language, langData);
            return;
        }

        // try to load from resource
        InputStream stream = ClassLoader.getSystemResourceAsStream(RESOURCE_PATH + File.separator + "lang-" + language.getCode() + ".properties");
        if (stream != null) {
            LangData langData = new LangData(new InputStreamReader(stream));
            data.put(language, langData);
        }

        // not found
        throw new NullPointerException("No language data found.");
    }

    private static void checkSingleton() {
        if (singleton == null) {
            singleton = new TextUtil();
        }
    }

    /* ------------------------- */

    /**
     * Provides a {@link Text} with its {@link String} content taken from a language file.
     * @param key Key to the string stored in a language file.
     * @param language The language that the {@link Text} should be in.
     * @param format Arguments to use as replacement for the format specifies in the string.
     * @return Formatted {@link Text} from {@link LangData} of the specified {@link Language} and its key.
     * @throws IllegalLanguageException if the language could not be loaded. This may happen because the specified
     *                                  language does not exist or because an {@link IOException} prevented it from
     *                                  loading properly.
     * @throws NullPointerException if the key does not point to a valid string.
     * @see TextUtil#load0(Language)
     * @see LangData#get(String)
     */
    public static @NotNull Text get(@NotNull String key, @NotNull Language language, String... format) throws IllegalLanguageException, NullPointerException {
        checkSingleton();
        return singleton.get0(key, language, format);
    }

    /**
     * Loads all strings of a language file into a {@link LangData} object.
     * @param language {@link Language} to load.
     * @throws IOException if a problem occurs while attempting to load the language file.
     * @throws NullPointerException if no matching language file could be found.
     * @see TextUtil#load0(Language)
     */
    public static void load(@NotNull Language language) throws IOException, NullPointerException {
        checkSingleton();
        singleton.load0(language);
    }
}
