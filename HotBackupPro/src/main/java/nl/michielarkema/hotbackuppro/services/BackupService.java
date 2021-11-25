package nl.michielarkema.hotbackuppro.services;

import nl.michielarkema.hotbackuppro.HotBackup;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;

import java.util.List;

public abstract class BackupService {

    protected final HotBackup plugin;
    protected final CommandSender commandSender;

    public List<String> getBackupPaths() {
        return backupPaths;
    }

    private final List<String> backupPaths;

    public List<String> getFilesBlacklist() {
        return filesBlacklist;
    }

    private final List<String> filesBlacklist;


    protected BackupService(final CommandSender commandSender) {
        this.plugin = HotBackup.getInstance();
        this.commandSender = commandSender;

        Configuration config = HotBackup.config;
        this.backupPaths = config.getStringList("target-paths");
        this.filesBlacklist = config.getStringList("files-blacklist");
    }

    /**
     * Starts the backup service.
     */
    public abstract void start();


    /**
     * Sends a message to the backup executor synchronously.
     * @param message The message.
     */
    public void sendMessage(String message) {
        Bukkit.getScheduler().callSyncMethod(this.plugin, () -> {
            this.commandSender.sendMessage(message);
            return true;
        });
    }
}
