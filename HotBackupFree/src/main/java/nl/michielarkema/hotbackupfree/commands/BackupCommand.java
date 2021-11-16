package nl.michielarkema.hotbackupfree.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import nl.michielarkema.hotbackupfree.BackupUtil;
import nl.michielarkema.hotbackupfree.HotBackup;
import nl.michielarkema.hotbackupfree.services.LocalBackupService;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
                this.startCommand(sender);
                break;
            case "status":
                this.statusCommand(sender);
                break;
            case "list":
                try {
                    this.listCommand(sender);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void startCommand(CommandSender sender) {
        if(HotBackup.isBackupRunning) {
            sender.sendMessage(ChatColor.RED + "There is already a backup in process!");
            return;
        }
        LocalBackupService backupService = new LocalBackupService(sender);
        backupService.backUp();
    }

    private void statusCommand(CommandSender sender) {

    }

    private void listCommand(CommandSender sender) throws IOException {

        List<Path> files = Files.walk(
                Paths.get(Objects.requireNonNull(HotBackup.getInstance()
                        .getConfig().getString("backup-path"))))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());

        sender.sendMessage(ChatColor.GRAY + "----------------------------------------");
        sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "BACKUP LIST");
        sender.sendMessage("");

        int number = 0;
        for (Path path : files) {
            number++;
            File file = path.toFile();

            TextComponent text = new TextComponent(number + ". " + ChatColor.GOLD + file.getName());
            text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new Text(HotBackup.translateColor(String.format("&bSize: %s\n&eCreated at: %s",
                            BackupUtil.getFileSize(file.length()),
                            file.getName().replace("_backup.zip", "")
                    )))));
            sender.spigot().sendMessage(text);
        }
        sender.sendMessage("");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "Hover over the items to see their information.");
        sender.sendMessage(ChatColor.GRAY + "----------------------------------------");
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
