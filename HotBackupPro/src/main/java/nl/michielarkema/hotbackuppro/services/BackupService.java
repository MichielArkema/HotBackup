package nl.michielarkema.hotbackuppro.services;

import nl.michielarkema.hotbackuppro.HotBackup;
import org.bukkit.configuration.Configuration;

import java.util.ArrayList;
import java.util.List;

public abstract class BackupService {

    protected final HotBackup plugin;

    protected final List<String> backupPaths;
    protected final List<String> filesBlacklist;
    protected final List<String> collectedFiles = new ArrayList<>();

    protected BackupService() {
        this.plugin = HotBackup.getInstance();

        Configuration config = this.plugin.getConfig();
        this.backupPaths = config.getStringList("target-paths");
        this.filesBlacklist = config.getStringList("files-blacklist");
    }
}
