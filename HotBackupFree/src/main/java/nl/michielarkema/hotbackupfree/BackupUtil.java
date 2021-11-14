package nl.michielarkema.hotbackupfree;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class BackupUtil {

    /**
     * Converts the localtime to tick count.
     * @param time
     * @return The tick count.
     */
    public static long getTickCount(LocalTime time) {
        final int hour = time.getHour();
        final int minute = time.getMinute();
        final int second = time.getSecond();

        final long ticks = (hour * 72000) + (minute * 1200) + (second * 20);
        return ticks;
    }

    public static void BackUp() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm");
                LocalDateTime now = LocalDateTime.now();

                String zipFileName = dtf.format(now) + ".backup.zip";
                File sourceFile = new File(zipFileName);
                try {
                    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(sourceFile));
                    List<Path> paths =  Files.walk(Paths.get(""))
                            .filter(Files::isRegularFile)
                            .filter(x -> !x.getFileName().toString().equals("session.lock") && !x.getFileName().toString().equals(zipFileName))
                            .collect(Collectors.toList());

                    for (Path path : paths) {

                        String fileName = path.toFile().getPath();

                        byte[] buffer = Files.readAllBytes(path);
                        ZipEntry entry = new ZipEntry(fileName);
                        out.putNextEntry(entry);
                        out.write(buffer, 0, buffer.length);
                        out.closeEntry();

                    }
                    out.close();
                    Bukkit.getServer().broadcast("Backup finished :)", "");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
