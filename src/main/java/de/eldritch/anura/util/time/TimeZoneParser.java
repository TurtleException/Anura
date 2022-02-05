package de.eldritch.anura.util.time;

import de.eldritch.anura.util.text.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Set;
import java.util.TreeSet;

public class TimeZoneParser {
    public static synchronized @Nullable ZoneId getRecommendedZoneIdByLanguage(@NotNull Language language) {
        try {
            if (language.equals(Language.GERMAN)) {
                return ZoneId.of("Europe/Berlin");
            }
        } catch (DateTimeException e) {
            return null;
        }
        return null;
    }

    public static synchronized TreeSet<String> offerPossibleZoneIDs(String input) {
        if (input == null || input.equals(""))
            return getPresetIDs();

        // filter and return
        return new TreeSet<>(ZoneId.getAvailableZoneIds().stream().filter(s -> s.toLowerCase().contains(input.toLowerCase())).toList());
    }

    private static synchronized TreeSet<String> getPresetIDs() {
        return new TreeSet<>(Set.of("Europe/Berlin", "UTC"));
    }
}
