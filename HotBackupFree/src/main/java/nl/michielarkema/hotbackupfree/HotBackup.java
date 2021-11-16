package nl.michielarkema.hotbackupfree;

import nl.michielarkema.hotbackupfree.commands.BackupCommand;
import nl.michielarkema.hotbackupfree.services.LocalBackupService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


public final class HotBackup extends JavaPlugin {

    private static HotBackup instance;

    public static final int MAX_BACKUP_FILES = 5;
    public static boolean isBackupRunning = false;

    private ConsoleCommandSender consoleSender;

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
        this.startBackupAutomation();

        this.consoleSender.sendMessage(ChatColor.AQUA + "------------------------------------------------");
    }

    private void startBackupAutomation() {
        final ConfigurationSection automationSection = this.getConfig().getConfigurationSection("automation");
        final boolean automation = Objects.requireNonNull(automationSection).getBoolean("enabled");
        this.consoleSender.sendMessage("Backup automation: " + automation);

        if(!automation) return;

        final ConfigurationSection timeSpanSection = automationSection.getConfigurationSection("time-span");

        final int hour = Objects.requireNonNull(timeSpanSection).getInt("hour");
        final int minute = timeSpanSection.getInt("minute");
        final int seconds = timeSpanSection.getInt("second");

        LocalTime nowTime = LocalTime.now()
                .plusHours(hour)
                .plusMinutes(minute)
                .plusSeconds(seconds);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.consoleSender.sendMessage(ChatColor.GREEN + "The next backup is scheduled at: " + ChatColor.GOLD + "" + nowTime.format(dtf));

        final long ticks = BackupUtil.getTickCount(LocalTime.of(hour, minute, seconds));
        this.consoleSender.sendMessage("Ticks: " + ticks);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Bukkit.getLogger().info("Backup timer executed :)");
        }, ticks, ticks);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
