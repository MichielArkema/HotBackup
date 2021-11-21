package nl.michielarkema.hotbackupfree.services;

import nl.michielarkema.hotbackupfree.BackupUtil;
import nl.michielarkema.hotbackupfree.HotBackup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public final class BackupAutomationService {

    private final ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private ConfigurationSection automationSection;

    private LocalTime nextScheduledBackupDate;

    public boolean isAutomationEnabled() {
        return automationEnabled;
    }

    private boolean automationEnabled;

    public BackupAutomationService() {
        Configuration config = HotBackup.getInstance().getConfig();
        this.automationSection = config.getConfigurationSection("automation");

        this.automationEnabled = Objects.requireNonNull(automationSection).getBoolean("enabled");
        this.consoleSender.sendMessage("Backup automation: " + this.automationEnabled);

        if(!this.automationEnabled) return;

        this.resetScheduledBackupDate();
        this.startBackupTimer();
        this.consoleSender.sendMessage(ChatColor.GREEN + "The next backup is scheduled at: " + ChatColor.GOLD + "" + this.getNextScheduledBackupDate());

    }

    private void startBackupTimer() {
        final ConfigurationSection timeSpanSection = automationSection.getConfigurationSection("time-span");

        final int hour = Objects.requireNonNull(timeSpanSection).getInt("hour");
        final int minute = timeSpanSection.getInt("minute");
        final int seconds = timeSpanSection.getInt("second");

        final long ticks = BackupUtil.getTickCount(LocalTime.of(hour, minute, seconds));
        Bukkit.getScheduler().scheduleSyncRepeatingTask(
                HotBackup.getInstance(),
                this::startAutomatedBackup,
                ticks, ticks);
    }

    private void startAutomatedBackup() {
        this.consoleSender.sendMessage(ChatColor.GREEN + "Starting automated backup process...");
        LocalBackupService localBackupService = new LocalBackupService(this.consoleSender);
        localBackupService.startBackup();
        this.resetScheduledBackupDate();
    }

    private void resetScheduledBackupDate() {
        final ConfigurationSection timeSpanSection = automationSection.getConfigurationSection("time-span");

        final int hour = Objects.requireNonNull(timeSpanSection).getInt("hour");
        final int minute = timeSpanSection.getInt("minute");
        final int seconds = timeSpanSection.getInt("second");

        this.nextScheduledBackupDate = LocalTime.now()
                .plusHours(hour)
                .plusMinutes(minute)
                .plusSeconds(seconds);
    }

    @NotNull
    public String getNextScheduledBackupDate() {
        return this.nextScheduledBackupDate.format(this.formatter);
    }
}
