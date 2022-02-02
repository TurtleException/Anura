package de.eldritch.Anura.data;

import de.eldritch.Anura.Anura;
import de.eldritch.Anura.data.entities.GuildData;
import de.eldritch.Anura.data.entities.Meme;
import de.eldritch.Anura.util.config.IllegalConfigException;
import de.eldritch.Anura.util.logging.NestedLogger;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.UUID;

public class DataService {
    private final SQLConnector sqlConnector;

    private final NestedLogger logger;

    public DataService(@NotNull Anura anura) throws SQLException, IllegalConfigException {
        logger = new NestedLogger("DataService", anura.getLogger());
        sqlConnector = new SQLConnector(logger, anura.getConfig().createSection("sql"));
    }

    /* ---------- REQUESTS ---------- */

    public synchronized Meme getMeme(UUID id) {
        return null;
    }

    public synchronized Meme getMeme(String source) {
        return null;
    }

    public synchronized @NotNull GuildData getGuildData(long selfUser, long guild) {
        // TODO
    }
}
