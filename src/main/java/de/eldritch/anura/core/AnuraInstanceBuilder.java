package de.eldritch.anura.core;

import de.eldritch.anura.InstanceKey;
import de.eldritch.anura.InstanceManager;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.security.auth.login.LoginException;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class AnuraInstanceBuilder {
    private final JDABuilder jdaBuilder;

    private InstanceManager instanceManager;
    private InstanceKey instanceKey;

    public AnuraInstanceBuilder(boolean defaults) {
        jdaBuilder = JDABuilder.createDefault(null);

        if (defaults) {
            this.applyDefaults();
        }
    }

    private void applyDefaults() {
        this.enableIntent(GatewayIntent.GUILD_MEMBERS);
        this.enableIntent(GatewayIntent.GUILD_BANS);
        this.enableIntent(GatewayIntent.GUILD_VOICE_STATES);
        this.enableIntent(GatewayIntent.GUILD_MESSAGES);
        this.enableIntent(GatewayIntent.GUILD_MESSAGE_REACTIONS);
        this.enableIntent(GatewayIntent.GUILD_MESSAGE_TYPING);
        this.enableIntent(GatewayIntent.DIRECT_MESSAGES);
        this.enableIntent(GatewayIntent.DIRECT_MESSAGE_REACTIONS);
        this.enableIntent(GatewayIntent.DIRECT_MESSAGE_TYPING);

        this.disableIntent(GatewayIntent.GUILD_PRESENCES);

        this.setStatus(OnlineStatus.ONLINE);

        this.setActivity(null);

        this.setMemberCachePolicy(MemberCachePolicy.NONE);

        this.setChunkingFilter(ChunkingFilter.NONE);
    }

    /* ---------- BUILDER METHODS ----------*/

    public AnuraInstanceBuilder setInstanceManager(InstanceManager instanceManager) {
        this.instanceManager = instanceManager;
        return this;
    }

    public AnuraInstanceBuilder setInstanceKey(InstanceKey instanceKey) {
        this.instanceKey = instanceKey;
        return this;
    }

    public AnuraInstanceBuilder setToken(@Nullable String token) {
        jdaBuilder.setToken(token);
        return this;
    }

    public AnuraInstanceBuilder enableIntent(@NotNull GatewayIntent intent) {
        jdaBuilder.enableIntents(intent);
        return this;
    }

    public AnuraInstanceBuilder disableIntent(@NotNull GatewayIntent intent) {
        jdaBuilder.disableIntents(intent);
        return this;
    }

    public AnuraInstanceBuilder setStatus(@NotNull OnlineStatus status) {
        jdaBuilder.setStatus(status);
        return this;
    }

    public AnuraInstanceBuilder setActivity(@Nullable Activity activity) {
        jdaBuilder.setActivity(activity);
        return this;
    }

    public AnuraInstanceBuilder setMemberCachePolicy(@Nullable MemberCachePolicy memberCachePolicy) {
        jdaBuilder.setMemberCachePolicy(memberCachePolicy);
        return this;
    }

    public AnuraInstanceBuilder setChunkingFilter(@Nullable ChunkingFilter chunkingFilter) {
        jdaBuilder.setChunkingFilter(chunkingFilter);
        return this;
    }

    /* ------------------------- */

    public @NotNull AnuraInstance build() throws IllegalStateException, LoginException, IllegalArgumentException {
        if (instanceManager == null)
            throw new IllegalStateException("InstanceManager is not yet specified");

        if (instanceKey == null)
            throw new IllegalStateException("InstanceKey is not yet specified");

        return new AnuraInstance(instanceManager, instanceKey, jdaBuilder);
    }
}
