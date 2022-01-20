package de.eldritch.Anura.util.logging;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

public class DiscordHandler extends StreamHandler {
    private final MessageChannel channel;

    public DiscordHandler(@NotNull MessageChannel channel) {
        this.channel = channel;
    }

    @Override
    public synchronized void publish(LogRecord record) {
        if (!channel.canTalk()) return;
        if (record == null)     return;
        if (!channel.canTalk()) return;


        String level = ":small_blue_diamond:";
        if (record.getLevel().equals(Level.INFO))
            level = ":small_orange_diamond:";
        else if (record.getLevel().equals(Level.WARNING))
            level = ":warning:";
        else if (record.getLevel().equals(Level.SEVERE))
            level = ":octagonal_sign:";


        MessageBuilder builder = new MessageBuilder();
        builder.append(level);
        builder.append(" `[");
        builder.append(record.getLoggerName());
        builder.append("]`: ");


        // check message length
        if (builder.length() + record.getMessage().length() >= 2000) {
            // put message into separate file
            builder.append("*This message is too long to be displayed.*");

            String stackTrace = buildStackTrace(record.getThrown());
            InputStream messageStream    = new ByteArrayInputStream(record.getMessage() != null ? record.getMessage().getBytes() : new byte[]{});
            InputStream stackTraceStream = new ByteArrayInputStream(stackTrace != null          ? stackTrace.getBytes()          : new byte[]{});

            MessageAction messageAction = channel.sendMessage(builder.build()).addFile(messageStream, "message.txt");

            if (stackTrace != null)
                messageAction = messageAction.addFile(stackTraceStream, "stacktrace.txt");

            messageAction.queue();
        } else {
            builder.append(record.getMessage());

            // check if stacktrace has to be extracted
            if (record.getThrown() != null) {
                List<String> stackTrace = getStackTrace(record.getThrown());

                MessageBuilder newBuilder = new MessageBuilder(builder);

                for (String thrown : stackTrace) {
                    newBuilder.appendCodeBlock(thrown, "java");
                }

                if (newBuilder.length() >= 2000) {
                    // message is too long -> append stacktrace as file
                    String stackTraceString = buildStackTrace(record.getThrown());
                    InputStream stackTraceStream = new ByteArrayInputStream(stackTraceString != null ? stackTraceString.getBytes() : new byte[]{});
                    channel.sendMessage(builder.build()).addFile(stackTraceStream, "stacktrace.txt").queue();
                } else {
                    // send message
                    channel.sendMessage(newBuilder.build()).queue();
                }
            }
        }
    }

    /**
     * Recursively parses the stacktrace of a {@link Throwable} and each of its causes to a {@link String} and provides
     * them in a {@link List} ordered from highest to deepest level (last element is the root cause).
     * @param throwable {@link Throwable} to parse the stacktrace of.
     * @return List of each stacktrace.
     */
    private List<String> getStackTrace(@NotNull Throwable throwable) {
        List<String> list = new ArrayList<>();

        if (throwable.getCause() != null) {
            list.addAll(getStackTrace(throwable.getCause()));
        }

        StringBuilder builder = new StringBuilder(throwable.toString());
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            builder.append("\n\t");
            builder.append(stackTraceElement.toString());
        }
        list.add(builder.toString());

        return list;
    }

    /**
     * Parses the stacktrace of a {@link Throwable} and all of its causes to a {@link String}.
     * <p>If the provided throwable is <code>null</code> this will return <code>null</code>.
     * @param throwable {@link Throwable} to parse the stacktrace of.
     * @return Complete stacktrace.
     * @see DiscordHandler#getStackTrace(Throwable)
     */
    private @Nullable String buildStackTrace(@Nullable Throwable throwable) {
        if (throwable == null)
            return null;

        List<String> thrown = getStackTrace(throwable);
        StringBuilder builder = new StringBuilder();
        for (String str : thrown)
            builder.append(str).append("\n\n");

        return builder.toString();
    }
}
