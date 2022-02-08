package de.eldritch.anura.data;

import java.time.ZoneId;
import java.util.Set;

public class StaticData {
    public static final Set<String> AVAILABLE_ZONE_IDS = ZoneId.getAvailableZoneIds();
}
