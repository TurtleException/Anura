import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TreeSet;

public class TimeZoneIDTest {
    public static void main0(String[] args) {
        TreeSet<String> zoneIDs = new TreeSet<>(ZoneId.getAvailableZoneIds());

        /* ----- FILTER ----- */
        // zoneIDs.removeIf(s -> !s.toLowerCase().contains("europe"));

        /* ----- PRINT ----- */
        for (String zoneID : zoneIDs)
            System.out.println(zoneID);
        System.out.println("##### ##### #####");
        System.out.println(zoneIDs.size());
    }

    public static void main1(String[] args) {
        ZoneId zone = ZoneId.of("Europe/Berlin");

        Instant first  = Instant.parse("2022-03-27T00:00:00.00Z");
        Instant second = Instant.parse("2022-03-27T06:00:00.00Z");

        ZoneId zoneId = ZoneId.of("Europe/Berlin");

        System.out.println("           UTC #  " + first.toString() + "   #   " + second.toString());
        // System.out.println(" Europe/Berlin #  " + first.atZone(zone.toZoneId()).toString() + "   #   " + second.atZone(zone.toZoneId()).toString());
    }

    public static void main(String[] args) {
        ZoneId zone = ZoneId.of("Europe/Berlin");

        Instant instant = Instant.parse("2022-03-27T01:00:00.00Z");
        ZonedDateTime zoned = instant.atZone(zone);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;


        System.out.println("           UTC #  " + instant);
        System.out.println(" Europe/Berlin #  " + formatter.format(zoned));
    }
}
