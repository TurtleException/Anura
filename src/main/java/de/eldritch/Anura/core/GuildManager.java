package de.eldritch.Anura.core;

import de.eldritch.Anura.Anura;
import de.eldritch.Anura.core.module.AnuraModule;
import de.eldritch.Anura.data.entities.GuildData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class GuildManager {
    private final AnuraInstance instance;

    private final HashMap<Long, GuildData> guilds = new HashMap<>();

    public GuildManager(@NotNull AnuraInstance instance) {
        this.instance = instance;
    }

    public boolean checkModule(long guild, @NotNull AnuraModule module) {
        GuildData data = guilds.get(guild);
        if (data != null) {
            Boolean enabled = data.getModules().get(module.getName());
            return enabled != null ? enabled : false;
        }

        // request data
        retrieve(guild);

        data = guilds.get(guild);
        if (data != null) {
            Boolean enabled = data.getModules().get(module.getName());
            return enabled != null ? enabled : false;
        }

        return false;
    }

    private void retrieve(long guild) {
        guilds.put(guild, Anura.singleton.getDataService().getGuildData(instance.getJDA().getSelfUser().getIdLong(), guild));
    }

    private void save(long guild) {
        // TODO: save guild data in SQL db
    }
}
