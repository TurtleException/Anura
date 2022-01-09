package de.eldritch.Anura.util.logging;

import de.eldritch.Anura.Anura;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;

public class LogUtil {
    /**
     * Provides a {@link FileHandler} that is already set up for the running {@link Anura} instance.
     * @param formatter Log {@link Formatter} to assign to the handler.
     * @return A {@link FileHandler} with the new log file.
     * @throws IOException if the handler fails to initialize.
     */
    public static @NotNull FileHandler getFileHandler(@NotNull Formatter formatter) throws IOException {
        String datePrefix = new SimpleDateFormat("yyMMdd").format(Date.from(Instant.now()));

        // retrieve all existing log files from today
        File[] files = getLogDir().listFiles((dir, name) -> name.matches("^\\d\\d\\d\\d\\d\\d-\\d+\\.log$") && name.startsWith(datePrefix));
        if (files == null)
            throw new NotDirectoryException(getLogDir().getName());

        String fileName = datePrefix + "-" + files.length + ".log";

        // create FileHandler
        FileHandler fileHandler = new FileHandler(new File(getLogDir(), fileName).getPath(), true);
        fileHandler.setFormatter(formatter);
        return fileHandler;
    }

    /**
     * Provides the directory containing all log files.
     * @return log file directory.
     */
    public static File getLogDir() {
        File file = new File(Anura.singleton.getDirectory(), "logs");
        //noinspection ResultOfMethodCallIgnored
        file.mkdir();
        return file;
    }
}
