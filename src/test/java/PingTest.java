import de.eldritch.Anura.Anura;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.util.logging.Level;

public class PingTest {
    private static final String URL = "https://media.discordapp.net/attachments/928829784602071040/929933545198215229/unknown.png";

    public static void main(String[] args) {
        System.out.println("Checking...");
        System.out.println(ping());
        System.out.println("Done!");
    }

    public static int ping() {
        try {
            HttpsURLConnection con = (HttpsURLConnection) new URL(URL).openConnection();
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            return con.getResponseCode();
        } catch (Exception e) {
            Anura.singleton.getLogger().log(Level.FINE, "An unexpected exception occurred when attempting to ping '" + URL + "'", e);
        }

        return 0;
    }
}
