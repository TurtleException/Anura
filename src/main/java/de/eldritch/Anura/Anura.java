package de.eldritch.Anura;

import de.eldritch.Anura.core.AnuraInstance;
import de.eldritch.Anura.util.config.ConfigSection;
import de.eldritch.Anura.util.config.FileConfig;
import de.eldritch.Anura.util.logging.LogUtil;
import de.eldritch.Anura.util.logging.SimpleFormatter;
import de.eldritch.Anura.util.version.IllegalVersionException;
import de.eldritch.Anura.util.version.Version;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.reader.StreamReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.NotDirectoryException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/**
 * This is the main class of the project.
 * <p>Its main task is to manage all {@link AnuraInstance} instances and basic configuration.
 */
public class Anura {
    public static Anura singleton;
    public static final Version VERSION = Version.retrieveFromResources();

    private final File directory;
    private final Logger logger;

    private FileConfig config;
    private FileConfig instanceConf;

    private final HashSet<AnuraInstance> instances = new HashSet<>();

    public Anura() throws URISyntaxException, NullPointerException, IOException, IllegalVersionException {
        singleton = this;

        /* ----- VERSION ----- */
        if (VERSION == null)
            throw new IllegalVersionException("Version may not be null.");

        System.out.printf(" version %s...%n", VERSION.toString());

        /* ----- FILES ----- */
        directory = new File(Anura.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
        if (directory == null)
            throw new NullPointerException("Directory may not be null");
        if (!directory.exists())
            throw new NullPointerException("Directory does not exist");
        if (!directory.isDirectory())
            throw new NotDirectoryException(directory.getName());

        System.out.println("Working directory verified!");

        /* ----- LOGGER ----- */
        SimpleFormatter formatter = new SimpleFormatter();
        logger = Logger.getLogger("ROOT");
        logger.addHandler(new StreamHandler(System.out, formatter));
        logger.addHandler(LogUtil.getFileHandler(formatter));

        /* ----- CONFIG ----- */
        getLogger().log(Level.INFO, "Constructing configuration...");
        File[] configFiles = {
                new File(directory, "config.yml"),
                new File(directory, "instances.yml")
        };
        for (File configFile : configFiles) {
            if (!configFile.exists()) {
                if (configFile.createNewFile()) {
                    getLogger().log(Level.INFO, "New " + configFile.getName() + " created!");
                } else {
                    throw new IOException("Could not create " + configFile.getName() + ".");
                }
            }
            if (!configFile.isFile())
                throw new IOException(configFile.getName() + " seems to not be a file.");
        }
        config       = new FileConfig(configFiles[0]);
        instanceConf = new FileConfig(configFiles[1]);
        config.loadDefaults("defaults/config.yml");


        this.constructInstances();
        this.enableInstances();
    }

    /* ---------- MAIN ---------- */

    public static void main(String[] args) {
        System.out.print("Starting Anura");
        try {
            Anura anura = new Anura();
        } catch (Exception e) {
            System.out.println("Caught an exception that stopped the program!");
            e.printStackTrace();
        }
    }

    /* ---------- INSTANCES ---------- */

    private void constructInstances() {
        for (String key : instanceConf.getKeys(false)) {
            try {
                getLogger().log(Level.FINE, "Attempting to construct instance '" + key + "'...");

                ConfigSection config = instanceConf.createSection(key);

                AnuraInstance instance = new AnuraInstance(config);
                instances.add(instance);

                getLogger().log(Level.INFO, "Constructed instance:  " + instance.getFullName());
            } catch (Exception e) {
                getLogger().log(Level.WARNING, "Unable to construct instance '" + key + "'.", e);
            }
        }
    }

    private void enableInstances() {
        for (AnuraInstance instance : instances) {
            getLogger().log(Level.FINE, "Enabling instance " + instance.getFullName() + "...");
            instance.start();
        }
    }

    /* ------------------------- */

    /**
     * Provides the working directory of this instance. If the {@link File} has not been changed since startup this
     * should always return an existing {@link File} where {@link File#isDirectory()} is <code>true</code>.
     * <p>The working directory is equal to the directory where the JAR is located.
     * @return Instance working directory.
     */
    public @NotNull File getDirectory() {
        return directory;
    }

    /**
     * Provides the global {@link Logger} of this instance.
     * @return Global Logger of this instance.
     */
    public @NotNull Logger getLogger() {
        return logger;
    }
}
