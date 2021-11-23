package nl.michielarkema.hotbackupfree;

import nl.michielarkema.hotbackupfree.commands.BackupCommand;
import nl.michielarkema.hotbackupfree.services.BackupAutomationService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public final class HotBackup extends JavaPlugin {

    private static HotBackup instance;

    public static final int MAX_BACKUP_FILES = 5;
    public static boolean isBackupRunning = false;

    private ConsoleCommandSender consoleSender;
    public BackupAutomationService backupAutomationService;


    public HotBackup() {
        instance = this;
    }

    public static HotBackup getInstance() {
        return instance;
    }
    public static String translateColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public void onEnable() {
        this.consoleSender = Bukkit.getConsoleSender();

        this.consoleSender.sendMessage(ChatColor.AQUA + "------------------------------------------------");

        this.consoleSender.sendMessage(ChatColor.GREEN + "HotBackup (Free Edition) V1.0.0-TESTING .");
        this.consoleSender.sendMessage(ChatColor.RED + "You are using the free version of HotBackup. functionality is limited!");
        this.consoleSender.sendMessage(ChatColor.GOLD + "Consider upgrading to HotBackupPro here: https://mc-market/resources/cool");

        this.saveDefaultConfig();

        Objects.requireNonNull(this.getCommand("hbu")).setExecutor(new BackupCommand());
        this.backupAutomationService = new BackupAutomationService();
        this.createBackupDirectory();
        this.consoleSender.sendMessage(ChatColor.AQUA + "------------------------------------------------");
    }

    private void createBackupDirectory() {
        Path storagePath = Paths.get(Objects.requireNonNull(this.getConfig().getString("backup-path")));

        if(!storagePath.toFile().exists()) {
            try {
                Files.createDirectory(storagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
