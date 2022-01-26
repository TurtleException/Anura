package de.eldritch.Anura.data.entities;

import de.eldritch.Anura.util.text.Language;

import java.util.HashMap;
import java.util.TimeZone;

public class GuildData {
    private Language language;
    private HashMap<String, Boolean> modules;

    public Language getLanguage() {
        return language;
    }

    public HashMap<String, Boolean> getModules() {
        return modules;
    }
}
