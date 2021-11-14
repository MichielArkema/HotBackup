package nl.michielarkema.hotbackupfree.commands;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import nl.michielarkema.hotbackupfree.HotBackup;
import nl.michielarkema.hotbackupfree.services.LocalBackupService;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

public final class BackupCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            this.showHelp(sender);
            return true; 
        }
        this.handleArguments(sender, args);
        return true;
    }

    private void handleArguments(CommandSender sender, String[] args) {
        switch (args[0]) {
            case "start":
                this.startCommand(sender, args);
                break;
            case "status":
                this.statusCommand(sender);
                break;
            case "list":
                //Todo: Show inventory screen with a list of backup files.
                Player player = (Player) sender;

                TextComponent txt = new TextComponent(ChatColor.YELLOW + "Hover of an item to see its information.");
                player.spigot()
                        .sendMessage(ChatMessageType.ACTION_BAR, txt);
                break;
        }
    }

    private void startCommand(CommandSender sender, String[] args) {
        LocalBackupService backupService = new LocalBackupService(sender);
        backupService.backUp();
    }

    private void statusCommand(CommandSender sender) {
        SidebarString[] lines = new SidebarString[] {
                new SidebarString("Version: 1.0.0-TESTING"),
        };
        Sidebar sideBar = new Sidebar(ChatColor.GOLD + "Backup Status", HotBackup.getInstance(), 60, lines);
        sideBar.showTo((Player) sender);
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------");

        sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "HotBackup(Free) Version: 1.0.0-TESTING");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GREEN + "Usage: /hbu start - " + ChatColor.GOLD + "Starts a new backup process.");
        sender.sendMessage(ChatColor.GREEN + "Usage: /hbu status - " + ChatColor.GOLD + "Shows the backup status.");
        sender.sendMessage(ChatColor.GREEN + "Usage: /hbu list - " + ChatColor.GOLD + "Shows the list of backup files.");
        sender.sendMessage("");

        TextComponent txt = new TextComponent(ChatColor.LIGHT_PURPLE + "You are using the free version. Get the full version");
        txt.addExtra(" HERE");
        txt.setItalic(true);
        txt.setBold(true);
        txt.setUnderlined(true);
        txt.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://google.com"));
        sender.spigot().sendMessage(txt);

        sender.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------");
    }
}
