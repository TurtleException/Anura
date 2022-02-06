package de.eldritch.anura.data;

import de.eldritch.anura.Anura;
import de.eldritch.anura.Instance;
import de.eldritch.anura.data.entities.Meme;
import de.eldritch.anura.util.logging.NestedLogger;
import de.eldritch.anura.util.text.Language;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The database connection to MySQL. This class is only instantiated once by {@link Anura} and provides a connection for
 * all instances.
 * <p>This class should <b>always</b> be thread-safe as multiple {@link Instance} objects may call it simultaneously.
 */
public class DataService {
    private final NestedLogger logger;
    private final SQLConnector sqlConnector;

    public DataService() throws IOException, SQLException, NullPointerException {
        this.logger = new NestedLogger("DataService", Anura.singleton.getLogger());

        sqlConnector = new SQLConnector(logger);
    }

    /* ---------- DATA ---------- */

    /**
     * Provides a specific {@link Meme} determined by its ID.
     * @param id Unique ID of the meme.
     * @return Meme object.
     * @throws SQLException if an SQL error occurs.
     * @throws NullPointerException if the underlying {@link ResultSet} is empty.
     */
    public synchronized Meme getMemeById(long id) throws SQLException, NullPointerException {
        ResultSet resultSet = sqlConnector.executeQuery("SELECT * FROM memes WHERE id = " + id + " LIMIT 1");

        // check ResultSet
        if (!resultSet.next())
            throw new NullPointerException("Could not match id");

        try {
            // fetch data
            Language language  = Language.valueOf(resultSet.getString("language"));
            String   url       = resultSet.getString("url");
            long     timestamp = resultSet.getLong("timestamp");
            long     authorID  = resultSet.getLong("author_id");
            int      rating    = resultSet.getInt("rating");

            return new Meme(id, timestamp, url, language, authorID, rating);
        } catch (Exception e) {
            throw new SQLException("Could not retrieve meme", e);
        }
    }

    /**
     * Provides a random {@link Meme} while possibly excluding multiple entries specified by ID.
     * @param exclude Array of IDs to exclude.
     * @return Meme object.
     * @throws SQLException if an SQL error occurs.
     * @throws NullPointerException if the underlying {@link ResultSet} is empty.
     */
    public synchronized Meme getMemeRandom(long[] exclude) throws SQLException, NullPointerException {
        return getMemeById(generateID(exclude));
    }

    /**
     * Provides a {@link List} of frequently used time-zones. If the SQL-Query fails to execute properly or returns
     * empty a default list is returned.
     * <p>"Frequently used" is defined as the top 25 results ordered by amount of uses. Results that only occur once are
     * excluded as they would represent an exception.
     * @return List of frequently used TZDB zone IDs.
     */
    public synchronized @NotNull List<String> getRecentTimeZones() {
        String query = "SELECT `timezone` FROM (SELECT `timezone`, COUNT(`timezone`) AS `count` FROM customers GROUP BY `timezone`) AS `frequent` WHERE `count` > 1 ORDER BY `count` DESC LIMIT 25";

        try {
            ResultSet resultSet = sqlConnector.executeQuery(query);
            List<String> frequent = new ArrayList<>();

            // abort if query returns empty
            if (!resultSet.next())
                throw new NullPointerException();

            // pass data to list
            do {
                frequent.add(resultSet.getString(1));
            } while (resultSet.next());

            return frequent;
        } catch (Exception e) {
            // return defaults
            return List.of("UTC", "Europe/Berlin");
        }
    }

    /* ------------------------- */

    /**
     * Queries the SQL database for a random meme entry ID while possibly excluding multiple entries specified by ID.
     * @param exclude Array of IDs to exclude.
     * @return ID of the entry that doesn't match any of the IDs in the given array.
     * @throws SQLException if an SQL error occurs
     * @throws NullPointerException if the {@link ResultSet} is empty.
     */
    private long generateID(long[] exclude) throws SQLException, NullPointerException {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < exclude.length; i++) {
            strBuilder.append(" id != ").append(exclude[i]);
            if (i != exclude.length - 1)
                strBuilder.append(" AND");
        }

        ResultSet resultSet = sqlConnector.executeQuery("SELECT id FROM memes WHERE " + strBuilder.toString() + " ORDER BY RAND() LIMIT 1");

        // check ResultSet
        if (!resultSet.next())
            throw new NullPointerException("Could not find non-excluded id");

        return resultSet.getLong("id");
    }
}
