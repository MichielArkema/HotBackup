package nl.michielarkema.hotbackuppro;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

public final class HotBackup extends JavaPlugin {

    private static HotBackup instance;

    public static Configuration config;

    public static HotBackup getInstance() {
        return instance;
    }

    public HotBackup() {
        instance = this;
    }
    @Override
    public void onEnable() {
        config = this.getConfig();
    }

    @Override
    public void onDisable() {
    }
}
