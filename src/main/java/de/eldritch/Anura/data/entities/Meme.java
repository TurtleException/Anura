package de.eldritch.Anura.data.entities;

import de.eldritch.Anura.Anura;
import de.eldritch.Anura.util.text.Language;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.logging.Level;

public record Meme(@NotNull UUID id, @NotNull String url, @NotNull Language language) {
    /**
     * Pings the url of the meme and returns the HTTP status code of the response.
     * <p>Note that this method will block the thread for up to 10 seconds.
     * @return HTTP response code.
     */
    public int ping() {
        try {
            HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            return con.getResponseCode();
        } catch (Exception e) {
            Anura.singleton.getLogger().log(Level.FINE, "An unexpected exception occurred when attempting to ping '" + url + "'", e);
        }

        return 0;
    }
}
