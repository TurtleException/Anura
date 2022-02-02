package de.eldritch.Anura.data.entities;

import de.eldritch.Anura.util.text.Language;

import java.util.HashMap;

public class GuildData {
    private final long id;

    private Language language;
    private HashMap<String, Boolean> modules;

    GuildData(long id) {
        this.id = id;
    }

    public Language getLanguage() {
        return language;
    }

    public HashMap<String, Boolean> getModules() {
        return modules;
    }
}
