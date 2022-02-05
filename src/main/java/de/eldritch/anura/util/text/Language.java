package de.eldritch.anura.util.text;

import de.eldritch.anura.InstanceKey;
import de.eldritch.anura.InstanceManager;
import de.eldritch.anura.core.AnuraInstance;
import org.jetbrains.annotations.NotNull;

public enum Language implements InstanceKey {
    ENGLISH("EN", true),

    GERMAN("DE", true),

    KERL("KERL", true),

    UNDEFINED("UNDEFINED", false);


    private final String code;
    private final boolean implement;

    Language(@NotNull String code, boolean implement) {
        this.code = code;
        this.implement = implement;
    }

    public @NotNull String code() {
        return code;
    }

    /**
     * Indicates whether the {@link InstanceManager} should implement an {@link AnuraInstance} associated with this
     * language. Some languages like <code>UNDEFINED</code> don't represent an actual language and therefore can not be
     * implemented in any meaningful way.
     * @return Whether an {@link AnuraInstance} with this language should be created.
     */
    public boolean shouldImplement() {
        return implement;
    }
}
