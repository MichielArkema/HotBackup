package nl.michielarkema.hotbackupfree.tasks;

import net.md_5.bungee.api.ChatColor;
import nl.michielarkema.hotbackupfree.HotBackup;
import nl.michielarkema.hotbackupfree.services.LocalBackupService;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class LocalBackupTask extends BukkitRunnable {

    private final LocalBackupService localBackupService;

    public LocalBackupTask(LocalBackupService localBackupService) {
        this.localBackupService = localBackupService;
    }

    @Override
    public void run() {
        this.sendSenderMessage(ChatColor.YELLOW + "Starting backup...");
        this.collectFiles();
        this.startBackup();
    }

    private void collectFiles() {
        this.sendSenderMessage(ChatColor.YELLOW + "Collecting files to backup...");
        for (String backupPath : this.localBackupService.backupPaths) {
            try {
                this.localBackupService.collectedFiles.addAll(Files.walk(Paths.get(backupPath))
                        .filter(Files::isRegularFile)
                        .map(path -> path.toFile())
                        .filter(file -> !this.localBackupService.filesBlacklist.contains(file.getName())
                                && !file.getParent().contains(this.localBackupService.backupStoragePath.toFile().getName()))
                        .collect(Collectors.toList()));
            }
            catch (IOException e)  {
                e.printStackTrace();
            }
        }
        this.sendSenderMessage(ChatColor.YELLOW + "Successfully collected " + this.localBackupService.collectedFiles.size() + " files.");
    }

    private void startBackup() {
        this.sendSenderMessage(ChatColor.GREEN + "Preparing backup process...");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm");
        LocalDateTime now = LocalDateTime.now();

        String zipFileName = dtf.format(now) + ".backup.zip";
        Path storagePath = this.localBackupService.getBackupStoragePath();

        if(!storagePath.toFile().exists()) {
            try {
                Files.createDirectory(storagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File sourceFile = new File(Paths.get(storagePath.toFile().getPath(), zipFileName).toString());

        try {
            this.sendSenderMessage(ChatColor.GREEN + "Backing up collected files...");
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(sourceFile));
            for (File file : this.localBackupService.collectedFiles) {

                String fileName = file.getPath();
                byte[] buffer = Files.readAllBytes(file.toPath());

                ZipEntry entry = new ZipEntry(fileName);
                out.putNextEntry(entry);
                out.write(buffer, 0, buffer.length);
                out.closeEntry();
            }
            out.close();
            this.sendSenderMessage(ChatColor.GREEN  + "Backup successfully finished.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendSenderMessage(String msg) {
        Bukkit.getScheduler().callSyncMethod(HotBackup.getInstance(), () -> {
            this.localBackupService.commandSender.sendMessage(msg);
            return true;
        });
    }
}
