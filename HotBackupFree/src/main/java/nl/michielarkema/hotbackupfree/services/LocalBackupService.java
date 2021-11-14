package nl.michielarkema.hotbackupfree.services;

import nl.michielarkema.hotbackupfree.HotBackup;
import nl.michielarkema.hotbackupfree.tasks.LocalBackupTask;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class LocalBackupService {
    private final HotBackup plugin;
    private final FileConfiguration config;

    public Path getBackupStoragePath() {
        return backupStoragePath;
    }

    public final Path backupStoragePath;

    public final List<String> backupPaths;

    public final List<String> filesBlacklist;

    public List<File> collectedFiles = new ArrayList<>();

    public CommandSender commandSender;

    public LocalBackupService(CommandSender commandSender) {
        this.commandSender = commandSender;
        this.plugin = HotBackup.getInstance();
        this.config = this.plugin.getConfig();

        this.backupStoragePath = Paths.get(Objects.requireNonNull(this.config.getString("backup-path")));
        this.backupPaths = this.config.getStringList("target-paths");
        this.filesBlacklist = this.config.getStringList("files-blacklist");

    //    this.filesBlacklist.add(this.backupStoragePath.toFile().getName());
    }

    public void backUp()
    {
        LocalBackupTask backupTask = new LocalBackupTask(this);
        backupTask.runTaskAsynchronously(this.plugin);
    }
}
