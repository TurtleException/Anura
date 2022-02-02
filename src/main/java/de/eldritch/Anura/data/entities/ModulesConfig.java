package de.eldritch.Anura.data.entities;

import java.util.HashMap;

public class ModulesConfig {
    private final int id;
    private HashMap<String, Boolean> modules;

    ModulesConfig(int id, HashMap<String, Boolean> modules) {
        this.id = id;
        this.modules = modules;
    }
}
