package me.fritz.creativeworlds;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreativeWorldsCommandExecutor implements CommandExecutor {
    private CreativeWorlds plugin;

    public CreativeWorldsCommandExecutor( CreativeWorlds instance ) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (label.equals("creative")) {
            creative((Player) sender);
        }
        else if (label.equals("survival")) {
            survival((Player) sender);
        }
        else if (label.equals("cwtp")) {
            cwtp((Player) sender, args[0]);
        }
        else if (label.equals("cw")) {
            cw((Player) sender, args);
        }
        else {
            return false;
        }
        return true;
    }
    
    private void creative(Player player) {
        if (plugin.config.isCreative( player.getWorld().getName())) {
            if (player.hasPermission("cw.creative")) {
                player.setGameMode(GameMode.CREATIVE);
            }
            else {
                player.sendMessage("You do not have permission to change your game mode.");
            }
        }
        else {
            player.sendMessage("Game mode is locked in this world.");
        }
    }
    
    private void survival(Player player) {
        if (plugin.config.isCreative( player.getWorld().getName())) {
            if (player.hasPermission("cw.creative")) {
                player.setGameMode(GameMode.SURVIVAL);
            }
            else {
                player.sendMessage("You do not have permission to change your game mode.");
            }
        }
        else {
            player.sendMessage("Game mode is locked in this world.");
        }
    }
    
    private void cwtp(Player player, String worldName) {
        if (player.hasPermission("cw.tp")) {
            World world = plugin.getServer().getWorld(worldName);
            
            if (world != null) {
                player.teleport(world.getSpawnLocation());
            }
            else {
                player.sendMessage("World \"" + worldName + "\" does not exist");
            }
        }
        else {
            player.sendMessage("You do not have permission to teleport between worlds.");
        }
    }
    
    private void cw(Player player, String[] args) {
        String command;
        
        if (args.length == 0) {
            command = "fail";            
        }
        else {
            command = args[0];
        }
        
        
        if(command.equals("world")) {
            plugin.wm.worldCommandHandler(player, args);
        }
        else if(command.equals("set")) {
            plugin.wm.settingCommandHandler(player, args);
        }
        else if (command.equals("help")) {
            player.sendMessage(ChatColor.RED + "/cw world list" + ChatColor.WHITE + " - lists current worlds and their settings");
            player.sendMessage(ChatColor.RED + "/cw world create " + ChatColor.GRAY + "<world name>" + ChatColor.DARK_GRAY + "<environment> <seed>" + ChatColor.WHITE + " - Create a new world");
            player.sendMessage("  World name: unique name for the world");
            player.sendMessage("  Environment: nether, normal, or skylands (optional)");
            player.sendMessage("  Seed: Seed for the new world.");
            player.sendMessage(ChatColor.RED + "/cw world remove " + ChatColor.GRAY + "<world name>" + ChatColor.WHITE + " - unload world and prevent autoloading of that world");
            player.sendMessage(ChatColor.RED + "/cw world load " + ChatColor.GRAY + "<world name>" + ChatColor.WHITE + " - load a world and set it to autoload on server restart");
            player.sendMessage(ChatColor.RED + "/cw world load " + ChatColor.GRAY + "<world name>" + ChatColor.WHITE + " -");
        }
        else {
            player.sendMessage("Invalid command, try " + ChatColor.RED + "/cw help" + ChatColor.WHITE + " for command options.");
        }
    }
}