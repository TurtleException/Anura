package de.eldritch.Anura.data;

import de.eldritch.Anura.util.config.ConfigSection;
import de.eldritch.Anura.util.config.IllegalConfigException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLConnector {
    private final Logger logger;

    // sql data
    private Connection connection;
    private String address, database, password, user;
    private int port;

    // table templates
    private static final String[] TEMPLATES = {
            "`memes` ( `id` BIGINT NOT NULL , `source` TEXT NOT NULL , `language` TEXT NULL , `user` BIGINT NOT NULL , PRIMARY KEY (`id`))",
            "`guilds` ( `instance_bot_id` BIGINT NOT NULL , `guild` BIGINT NOT NULL , PRIMARY KEY (`instance_bot_id`))"
            // TODO
    };

    public SQLConnector(Logger logger, ConfigSection config) throws SQLException, IllegalConfigException {
        this.logger = logger;

        extractConfig(config);

        buildConnection();
        buildTables();
    }

    /**
     * Checks whether the config is valid and attempts to get all the data from it that is needed later. If either
     * address, database or user are missing or the port is invalid the method will throw an exception.
     * @param config {@link ConfigSection} to retrieve that data from.
     * @throws IllegalConfigException if the config is null, one of the values is missing or invalid.
     */
    private void extractConfig(ConfigSection config) throws IllegalConfigException {
        if (config == null)
            throw new IllegalConfigException("Config may not be null");

        address  = config.getString("address");
        database = config.getString("database");
        password = config.getString("password");
        user     = config.getString("user");
        port     = config.getInt("port");

        if (address == null)
            throw new IllegalConfigException("Address may not be null");
        if (database == null)
            throw new IllegalConfigException("Database may not be null");
        if (user == null)
            throw new IllegalConfigException("User may not be null");
        if (port <= 0 || port >= 65535)
            throw new IllegalConfigException("Missing valid port");
    }

    /**
     * Attempts to get a {@link Connection} from the {@link DriverManager}. Address, port, database, user and password
     * should already have been extracted from config.
     * @throws SQLException if the connection fails to build or is not valid.
     * @see SQLConnector#extractConfig(ConfigSection)
     */
    private void buildConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" + address + ":" + port + "/" + database, user, password);

        if (!connection.isValid(8))
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

    public boolean execute(String statement) throws SQLException {
        return connection.createStatement().execute(statement);
    }

    public ResultSet executeQuery(String statement) throws SQLException {
        return connection.createStatement().executeQuery(statement);
    }
}
