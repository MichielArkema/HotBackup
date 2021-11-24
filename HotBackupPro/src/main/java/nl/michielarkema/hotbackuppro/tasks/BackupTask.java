package nl.michielarkema.hotbackuppro.tasks;

import nl.michielarkema.hotbackuppro.services.BackupService;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public abstract class BackupTask extends BukkitRunnable {

    protected final BackupService backupService;
    protected final List<String> collectedFiles = new ArrayList<>();

    protected BackupTask(BackupService backupService) {
        this.backupService = backupService;
    }

    protected abstract void runBackup();

    @Override
    public void run() {
        this.collectFiles();
        this.createZipFile();
        this.runBackup();
    }

    /**
     * Collects all the files for the backup process.
     */
    private void collectFiles() {
    }

    /**
     * Creates the backup zip file.
     */
    private void createZipFile() {
    }
}
