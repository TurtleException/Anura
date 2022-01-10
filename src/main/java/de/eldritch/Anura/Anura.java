package de.eldritch.Anura;

import de.eldritch.Anura.core.AnuraInstance;
import de.eldritch.Anura.util.logging.LogUtil;
import de.eldritch.Anura.util.logging.SimpleFormatter;
import de.eldritch.Anura.util.version.IllegalVersionException;
import de.eldritch.Anura.util.version.Version;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.NotDirectoryException;
import java.util.HashSet;
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

    private final HashSet<AnuraInstance> instances = new HashSet<>();

    public Anura() throws URISyntaxException, NullPointerException, IOException, IllegalVersionException {
        singleton = this;

        /* ----- FILES ----- */
        if (VERSION == null)
            throw new IllegalVersionException("Version may not be null.");


        /* ----- FILES ----- */
        directory = new File(Anura.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
        if (directory == null)
            throw new NullPointerException("Directory may not be null");
        if (!directory.exists())
            throw new NullPointerException("Directory does not exist");
        if (!directory.isDirectory())
            throw new NotDirectoryException(directory.getName());

        /* ----- LOGGER ----- */
        SimpleFormatter formatter = new SimpleFormatter();
        logger = Logger.getLogger("ROOT");
        logger.addHandler(new StreamHandler(System.out, formatter));
        logger.addHandler(LogUtil.getFileHandler(formatter));
    }

    /* ---------- MAIN ---------- */

    public static void main(String[] args) throws Exception {
        Anura anura = new Anura();
    }

    /* ------------------------- */

    /**
     * Provides the working directory of this instance.
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
