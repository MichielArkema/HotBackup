package nl.michielarkema.hotbackupfree;

import java.time.LocalTime;

public final class BackupUtil {

    /**
     * Converts the localtime to tick count.
     * @param time The local time object.
     * @return The tick count.
     */
    public static long getTickCount(LocalTime time) {
        final int hour = time.getHour();
        final int minute = time.getMinute();
        final int second = time.getSecond();

        return (hour * 72000) + (minute * 1200) + (second * 20);
    }

    public static String getFileSize(long bytes) {
        String message = "";

        if(bytes < 1024)
            message = bytes + " bytes";
        else if(bytes > 1024 && bytes < 1048576) {
            message = (bytes / 1024) + " kb";
        }
        else if(bytes >= 1048576) {
            message = (bytes / (1024 * 1024)) + " mb";
        }
        return message;
    }
}
