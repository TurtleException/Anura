package de.eldritch.Anura.util.id;

import de.eldritch.Anura.util.time.Offset;
import de.eldritch.Anura.util.time.Time;
import org.jetbrains.annotations.NotNull;

public class IDUtil {
    public static long instant = System.currentTimeMillis();
    public static long increment = 0;

    public synchronized @NotNull ID newId() {
        Time inst = Time.now();

        if (instant < System.currentTimeMillis()) {
            instant = inst.toMillis(Offset.UNIX);
            increment = 0;
        }

        return new ID(inst, increment++);
    }
}
