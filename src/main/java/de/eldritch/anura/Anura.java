package de.eldritch.anura;

import de.eldritch.anura.core.AnuraInstance;
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

/**
 * The main class and core of Anura. This serves as a single point from which every part of Anura can be accessed.
 * <p>It is also responsible for the highest level of logging, managing files and other basics that are needed elsewhere
 * in the program.
 * @see Instance
 * @see InstanceManager
 */
public class Anura {
    public static Anura singleton;
    public static final Version VERSION = Version.retrieveFromResources();

    private final File directory;
    private final Logger logger;

    private final DataService dataService;
    private final InstanceManager instanceManager;

    /**
     * Private constructor that is only ever used by the main method to initialize Anura.
     * <p>
     *     Anura will first check the version provided by the <code>version.properties</code> resource and then proceed
     *     by checking the working directory, which is defined as the directory at which the jar file is located. Next,
     *     the global {@link Logger} instance is initialized. All other loggers will be registered in a tree-structure
     *     with this logger being the root. Exceptions to this rule might be some internal loggers that use other files
     *     or don't produce any actual output at all. The {@link InstanceManager} is responsible for controlling all
     *     {@link AnuraInstance} or other {@link Instance} objects. All traffic to the (MySQL) database will be
     *     controlled by the {@link DataService}.
     * </p>
     * @throws Exception if any exceptions occur while initializing.
     * @see Anura#main(String[])
     */
    private Anura() throws Exception {
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

    /**
     * Main method used to initialize Anura. If this method is called again for some reason an
     * {@link IllegalStateException} will be thrown as re-initializing without restarting the JVM
     * would mess up everything.
     * @param args JVM args (can be safely ignored)
     * @throws IllegalStateException if Anura has already been initialized.
     */
    public static void main(String[] args) throws IllegalStateException {
        if (singleton != null)
            throw new IllegalStateException("Anura is already initialized.");

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

    /**
     * Provides the {@link DataService} of Anura.
     * @return DataService
     * @see DataService
     */
    public DataService getDataService() {
        return dataService;
    }

    /**
     * Provides the {@link InstanceManager} of Anura.
     * @return InstanceManager
     * @see InstanceManager
     */
    public InstanceManager getInstanceManager() {
        return instanceManager;
    }
}
