package nl.michielarkema.hotbackupfree;

import nl.michielarkema.hotbackupfree.commands.BackupCommand;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class HotBackup extends JavaPlugin {

    private static HotBackup instance;

    public HotBackup() {
        this.instance = this;
    }

    public static HotBackup getInstance() {
        return instance;
    }
    public static String translateColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("hbu").setExecutor(new BackupCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
