package de.eldritch.Anura.util.config;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple config that is stored in a file.
 */
public class FileConfig extends ConfigSection {
    private final File file;

    public FileConfig(@NotNull File file) throws IllegalArgumentException, IOException {
        super(null, null);

        if (!file.exists())
            throw new IllegalAccessError("File cannot be null");

        this.file = file;
    }

    private void read(boolean clear) throws IOException, IllegalConfigException {
        if (clear) {
            for (String key : this.getKeys(false)) {
                this.set(key, null);
            }
        }


    }

    public @NotNull File getFile() {
        return file;
    }
}
