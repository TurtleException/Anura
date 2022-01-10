import de.eldritch.Anura.util.config.FileConfig;
import de.eldritch.Anura.util.config.MemoryConfig;

import java.io.File;

public class ConfigTest {
    public static void main(String[] args) throws Exception {
        File file = new File("H:" + File.separator + "Temp" + File.separator + "220110", "config.yml");

        FileConfig fc = new FileConfig(file);

        System.out.println(fc.toString());

        System.out.println("\n#####\n");

        for (String key : fc.getKeys(true)) {
            Object val = fc.get(key);
            System.out.println(key + ": " + val + "  (" + (val != null ? val.getClass().getSimpleName() : "null") + ")");
        }
    }
}
