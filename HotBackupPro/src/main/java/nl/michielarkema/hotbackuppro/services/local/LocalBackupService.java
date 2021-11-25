package nl.michielarkema.hotbackuppro.services.local;

import nl.michielarkema.hotbackuppro.HotBackup;
import nl.michielarkema.hotbackuppro.services.BackupService;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public final class LocalBackupService extends BackupService {

    private final Path backupStoragePath;

    public LocalBackupService(final CommandSender commandSender) {
        super(commandSender);

        Configuration config = HotBackup.config;
        this.backupStoragePath = Paths.get(Objects.requireNonNull(config.getString("services.local.backup-path")));

    }

    @Override
    public void start() {

    }
}
