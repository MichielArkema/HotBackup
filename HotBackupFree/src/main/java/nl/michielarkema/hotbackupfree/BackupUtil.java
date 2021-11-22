package nl.michielarkema.hotbackupfree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalTime;
import java.util.Arrays;

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

    /**
     * Gets the file size in message format.
     * @param bytes the file size in bytes.
     * @return The message format of the file size.
     */
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

    /**
     * Sorts the files based on their creation date.
     * @param files The list of files.
     */
    public static void sortFilesByDateCreated (File[] files) {
        Arrays.sort(files, (f1, f2) -> {
            long l1 = getFileCreationEpoch(f1);
            long l2 = getFileCreationEpoch(f2);
            return Long.compare(l1, l2);
        });
    }

    /**
     * Gets the file creation date as epoch.
     * @param file the file.
     * @return File creation date epoch
     */
    public static long getFileCreationEpoch (File file) {
        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(),
                    BasicFileAttributes.class);
            return attr.creationTime()
                    .toInstant().toEpochMilli();
        } catch (IOException e) {
            throw new RuntimeException(file.getAbsolutePath(), e);
        }
    }
}
