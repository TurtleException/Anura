package de.eldritch.anura.data;

import de.eldritch.anura.Anura;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

class SQLConnector {
    private final Logger logger;

    // sql data
    private Connection connection;
    private String address, database, password, user;
    private int port;

    // table templates
    private static final String[] TEMPLATES = {
            /* All the memes... ALL OF THEM MUHAHAHAHAHA */
            "`memes` ( `id` VARCHAR(36) NOT NULL , `timestamp` TIMESTAMP NOT NULL , `url` TEXT NOT NULL , `language` TEXT NULL , `author_id` BIGINT NOT NULL , PRIMARY KEY (`id`))",

            /* Member guilds with their associated bot user ID and the chosen language and time zone. */
            "`guilds` ( `id` BIGINT NOT NULL , `bot_id` BIGINT NOT NULL , `language` TEXT NOT NULL , `modules_id` INT NOT NULL , `timezone` TEXT NOT NULL , PRIMARY KEY (`id`, `bot_id`))",

            /* All module configurations (enabled / disabled) for each instance & guild. */
            "`modules` ( `id` SMALLINT NOT NULL AUTO_INCREMENT , `data` BIGINT NOT NULL , PRIMARY KEY (`id`))"
    };

    SQLConnector(Logger logger) throws NullPointerException, SQLException, IOException {
        this.logger = logger;

        // get config
        Properties properties = new Properties();
        properties.load(new FileReader(new File(Anura.singleton.getDirectory(), "mysql.properties")));
        extractConfig(properties);

        buildConnection();
        buildTables();
    }

    /**
     * Checks whether the config is valid and attempts to get all the data from it that is needed later. If either
     * address, database or user are missing or the port is invalid the method will throw an exception.
     * @param config {@link Properties} to retrieve that data from.
     * @throws NullPointerException if the config is null, one of the values is missing or invalid.
     */
    private void extractConfig(Properties config) throws NullPointerException {
        if (config == null)
            throw new NullPointerException("Config may not be null");

        // assign values
        address        = config.getProperty("address");
        database       = config.getProperty("database");
        password       = config.getProperty("password");
        user           = config.getProperty("user");
        String portStr = config.getProperty("port");

        // check values
        if (address == null)
            throw new NullPointerException("Address may not be null");
        if (database == null)
            throw new NullPointerException("Database may not be null");
        if (user == null)
            throw new NullPointerException("User may not be null");

        // parse port to integer
        try {
            port = Integer.parseInt(portStr);
        } catch (ClassCastException e) {
            throw new NullPointerException("Missing valid port (Could not convert '" + portStr + "' to an integer)");
        }

        // check port
        if (port <= 0 || port >= 65535)
            throw new NullPointerException("Missing valid port");
    }

    /**
     * Attempts to get a {@link Connection} from the {@link DriverManager}. Address, port, database, user and password
     * should already have been extracted from config.
     * @throws SQLException if the connection fails to build or is not valid.
     * @see SQLConnector#extractConfig(Properties)
     */
    private void buildConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" + address + ":" + port + "/" + database, user, password);

        if (!connection.isValid(4))
            throw new SQLException("Connection is invalid");
    }

    /**
     * Creates all template tables if they don't already exist.
     */
    private void buildTables() {
        for (String template : TEMPLATES) {
            try {
                connection.createStatement().execute("CREATE TABLE IF NOT EXISTS " + template);
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Unable to create template:  " + template, e);
            }
        }
    }

    /* ------------------------- */

    synchronized boolean execute(@NotNull String statement) throws SQLException {
        return connection.createStatement().execute(statement);
    }

    synchronized @NotNull ResultSet executeQuery(@NotNull String statement) throws SQLException {
        return connection.createStatement().executeQuery(statement);
    }
}