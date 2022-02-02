package de.eldritch.anura;

import de.eldritch.anura.data.DataService;
import de.eldritch.anura.util.logging.LogUtil;
import de.eldritch.anura.util.logging.SimpleFormatter;
import de.eldritch.anura.util.version.IllegalVersionException;
import de.eldritch.anura.util.version.Version;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.NotDirectoryException;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

public class Anura {
    public static Anura singleton;
    public static final Version VERSION = Version.retrieveFromResources();

    private final File directory;
    private final Logger logger;

    private final DataService dataService;
    private final InstanceManager instanceManager;

    public Anura() throws Exception {
        singleton = this;


        /* ----- VERSION ----- */
        if (VERSION == null)
            throw new IllegalVersionException("Version can not be null");

        System.out.printf(" version %s...%n", VERSION);


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


        // construct control instance
        this.instanceManager = new InstanceManager();

        // construct services
        this.dataService = new DataService();

        // construct other instances
        this.instanceManager.init();
    }

    /* ---------- MAIN ---------- */

    public static void main(String[] args) {
        System.out.print("Starting Anura");
        try {
            Anura anura = new Anura();
        } catch (Exception e) {
            System.out.println("\nCaught an exception that stopped the application!");
            e.printStackTrace();
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
