package nl.michielarkema.hotbackuppro;

import org.bukkit.plugin.java.JavaPlugin;

public final class HotBackup extends JavaPlugin {

    private static HotBackup instance;
    public static HotBackup getInstance() {
        return instance;
    }

    public HotBackup() {
        instance = this;
    }
    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
