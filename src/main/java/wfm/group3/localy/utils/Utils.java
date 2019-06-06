package wfm.group3.localy.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class Utils {

    public static LocalDateTime max(LocalDateTime a, LocalDateTime b) {
        return a.isAfter(b) ? a : b;
    }

    public static LocalDateTime min(LocalDateTime a, LocalDateTime b) {
        return a.isBefore(b) ? a : b;
    }

    public static String formatDuration(Duration duration) {
        long hours = duration.toHours();
        int minutes = (int) ((duration.getSeconds() % (60 * 60)) / 60);
        return String.format("%02d", hours) + ":" + String.format("%02d", minutes);
    }
}
