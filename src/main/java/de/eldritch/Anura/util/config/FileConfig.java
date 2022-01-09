package de.eldritch.Anura.util.config;

import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.TreeMap;

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

        this.load();
    }

    /**
     * (Re-)loads the config from its file.
     * @throws FileNotFoundException if the {@link FileReader} could not be instantiated properly.
     */
    public void load() throws FileNotFoundException {
        this.load(new FileReader(file), false);
    }

    /**
     * Saves the config to its file.
     * @throws IOException if the {@link FileWriter} could not be instantiated properly.
     */
    public void save() throws IOException {
        TreeMap<String, Object> data = new TreeMap<>();
        this.getKeys(true).forEach(s -> data.put(s, this.get(s)));

        new Yaml().dump(data, new FileWriter(file, false));
    }
}
