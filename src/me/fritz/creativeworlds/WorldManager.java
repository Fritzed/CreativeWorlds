/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package me.fritz.creativeworlds;

import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

/**
 *
 * @author fritz
 */
public class WorldManager {
    CreativeWorlds plugin;
    
    public WorldManager(CreativeWorlds instance) {
        plugin = instance;
    }
    
    public void loadWorlds() {
        for (Iterator<String> i = plugin.config.configWorlds.iterator(); i.hasNext(); ) {
            plugin.getServer().createWorld(new WorldCreator(i.next()));
        }
    }
    
    public void worldCommandHandler(Player player, String[] args) {
        String command;
        
        if (args.length == 1) {
            command = "fail";            
        }
        else {
            command = args[1];
        }
        
        if (command.equals("list")) {
            worldList(player);
        }
        else if (command.equals("create")) {
            worldCreate(player, args);
        }
        else if (command.equals("load")) {
            worldLoad(player, args);
        }
        else if (command.equals("remove")) {
            worldRemove(player, args);
        }
        else {
            player.sendMessage("Invalid command, try " + ChatColor.RED + "/cw help" + ChatColor.WHITE + " for command options.");
        }
    }
    
    public void settingCommandHandler(Player player, String[] args) {
        String command;
        
        if (args.length == 1) {
            command = "fail";            
        }
        else {
            command = args[1];
        }
        
        if (command.equals("creative")) {
            setCreative(player, args);
        }
        else if (command.equals("survival")) {
            setSurvival(player, args);
        }
        else {
            player.sendMessage("Invalid command, try " + ChatColor.RED + "/cw help" + ChatColor.WHITE + " for command options.");
        }
        
        
    }
    
    private void worldList(Player player) {
        if (player.hasPermission("cw.world.list")) {            
            List<World> worlds = plugin.getServer().getWorlds();
            
            for (Iterator<World> i = worlds.iterator(); i.hasNext(); ) {
                player.sendMessage( getWorldString(i.next()) );
            }
        }
        else {
            player.sendMessage("You do not have permission to list all worlds.");
        }
    }
    
    private void worldCreate(Player player, String[] args) {
        if (player.hasPermission("cw.world.create")) {
            if (args.length > 2) {
                String worldName = args[2];
                
                if (plugin.getServer().getWorld(worldName) == null) {
                    WorldCreator wc = new WorldCreator(worldName);
                    if (args.length > 3) {
                        if (args[3].equals("nether")) {
                            wc.environment(Environment.NETHER);
                        }
                        else if (args[3].equals("normal")) {
                            wc.environment(Environment.NORMAL);
                        }
                        else if (args[3].equals("skylands")) {
                            wc.environment(Environment.SKYLANDS);
                        }
                        else {
                            player.sendMessage(args[3] + " is not a valid world type.");
                            return;
                        }
                            
                        if (args.length > 4) {
                            wc.seed(seedString(args[4]));
                        }
                    }
                    plugin.getServer().createWorld(wc);
                    plugin.config.addWorld(worldName);
                    player.sendMessage("World created:");
                    player.sendMessage(getWorldString(plugin.getServer().getWorld(worldName)));
                }
                else {
                    player.sendMessage("World already exists! Try to load world instead.");
                }
            }
            else {
                player.sendMessage("Usage: " + ChatColor.RED + "/cw world create " + ChatColor.GRAY + "<world name>" + ChatColor.DARK_GRAY + "<environment> <seed>");
            }
        }
        else {
            player.sendMessage("You do not have permission to create a new world.");
        }
    }
    
    private void worldRemove(Player player, String[] args) {
        if (player.hasPermission("cw.world.remove")) {
            if (args.length > 2) {
                String worldName = args[2];
                
                plugin.getServer().unloadWorld(worldName, true);
                plugin.config.removeWorld(worldName);
                player.sendMessage(worldName + " has been removed. To completely destroy it, it's data folder still exists");
            }
            else {
                player.sendMessage("Usage: " + ChatColor.RED + "/cw world remove " + ChatColor.GRAY + "<world name>");
            }
        }
        else {
            player.sendMessage("You do not have permission to remove a world.");
        }
    }
    
    private void worldLoad(Player player, String[] args) {
        if (player.hasPermission("cw.world.load")) {
            if (args.length > 2) {
                String worldName = args[2];
        
                plugin.getServer().createWorld(new WorldCreator(worldName));
                plugin.config.addWorld(worldName);
                player.sendMessage(getWorldString(plugin.getServer().getWorld(worldName)));
            }
            else {
                player.sendMessage("Usage: " + ChatColor.RED + "/cw world load " + ChatColor.GRAY + "<world name>");
            }
        }
        else {
            player.sendMessage("You do not have permission to load a world.");
        }
    }
    
    private void setCreative(Player player, String[] args) {
        if (player.hasPermission("cw.set.creative")) {
            String worldName;
            
            if (args.length > 2) {
                worldName = args[2];
            }
            else {
                worldName = player.getWorld().getName();
            }
            if (plugin.config.addCreative(worldName)) {
                player.sendMessage(worldName + " now allows creative building.");
            }
            else {
                player.sendMessage(worldName + " is already set to creative mode.");
            }
        }
        else {
            player.sendMessage("You do not have permission to change a world mode.");
        }        
    }
    
    private void setSurvival(Player player, String[] args) {
        if (player.hasPermission("cw.set.creative")) {
            String worldName;
            
            if (args.length > 2) {
                worldName = args[2];
            }
            else {
                worldName = player.getWorld().getName();
            }
            if (plugin.config.removeCreative(worldName)) {
                player.sendMessage(worldName + " no longer allows creative building.");
            }
            else {
                player.sendMessage(worldName + " is already set to survival mode.");
            }
            
            for (Iterator<Player> i = plugin.getServer().getWorld(worldName).getPlayers().iterator(); i.hasNext(); ) {
                i.next().setGameMode(GameMode.SURVIVAL);
            }
        }
        else {
            player.sendMessage("You do not have permission to change a world mode.");
        }        
    }
    
    public String getWorldString(World world) {
        String output = world.getName() + " (";
        output = output + "creative: " + plugin.config.isCreative(world.getName());
        output = output + " | ";
        output = output + "mobs: " + getMobConfig(world);
        output = output + ")";
        return output;
    }
    
    public String getMobConfig(World world) {
        if (world.getAllowAnimals()) {
            if (world.getAllowMonsters()) {
                return "all";
            }                
            return "animals";
        }
        else if (world.getAllowMonsters()) {
            return "monsters";
        }
        else {
            return "none";
        }
    }
    
    public Long seedString(String seed) {
        try {
            return Long.parseLong(seed);
        }
        catch (Exception ex) {
            return (long)seed.hashCode();
        }
    }
}