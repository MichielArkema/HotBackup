package nl.michielarkema.hotbackuppro.tasks;

import net.md_5.bungee.api.ChatColor;
import nl.michielarkema.hotbackuppro.BackupUtil;
import nl.michielarkema.hotbackuppro.services.BackupService;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public abstract class BackupTask extends BukkitRunnable {

    protected final BackupService backupService;
    protected final List<File> collectedFiles = new ArrayList<>();
    protected File zipFile;

    private String zipFileName;
    private Path storagePath;

    protected BackupTask(BackupService backupService) {
        this.backupService = backupService;
    }

    protected abstract void runBackup();

    @Override
    public void run() {

        this.zipFileName = BackupUtil.getFormattedLocalTime() + "_backup.zip";
        this.storagePath = BackupUtil.getRootPath();

        this.collectFiles();
        this.createZipFile();
        this.runBackup();
    }

    /**
     * Collects all the files for the backup process.
     */
    private void collectFiles() {
        this.backupService.sendMessage(ChatColor.YELLOW + "Collecting files to backup...");
        final String absolutePath = BackupUtil.getRootPath().toString();
        for (String backupPath : this.backupService.getBackupPaths()) {
            try {
                this.collectedFiles.addAll(Files.walk(Paths.get(absolutePath, backupPath))
                        .filter(Files::isRegularFile)
                        .map(Path::toFile)
                        .filter(file -> !this.backupService.getFilesBlacklist().contains(file.getName())
                                && !file.getName().equals(this.zipFileName))
                        .collect(Collectors.toList()));
            }
            catch (IOException e)  {
                e.printStackTrace();
            }
        }
        this.backupService.sendMessage(ChatColor.YELLOW + "Successfully collected " + this.collectedFiles.size() + " files.");
    }

    /**
     * Creates the backup zip file.
     */
    private void createZipFile() {
        this.backupService.sendMessage(ChatColor.GREEN + "Preparing backup process...");

        this.zipFile = new File(Paths.get(storagePath.toString(), zipFileName).toString());

        try {
            this.backupService.sendMessage(ChatColor.GREEN + "Backing up collected files...");
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(this.zipFile));
            for (File file : this.collectedFiles) {

                String fileName = file.getPath();
                byte[] buffer = Files.readAllBytes(file.toPath());

                ZipEntry entry = new ZipEntry(fileName);
                entry.setSize(buffer.length);

                out.putNextEntry(entry);
                out.write(buffer, 0, buffer.length);
                out.closeEntry();
            }
            out.close();
            this.backupService.sendMessage(ChatColor.GREEN  + "Backup successfully finished.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
