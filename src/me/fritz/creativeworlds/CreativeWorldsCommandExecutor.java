package me.fritz.creativeworlds;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreativeWorldsCommandExecutor implements CommandExecutor {
    private CreativeWorlds plugin;

    public CreativeWorldsCommandExecutor( CreativeWorlds instance ) {
        this.plugin = instance;
    }
    
    /** Command handler override for /creative, /survival, /cwtp, and /cw commands
     * 
     * @param sender The player who sent the command 
     * @param command The classification of the command being sent
     * @param label The actual command that is sent
     * @param args All of the arguments for the command in a String list
     * 
     * @return True if the command was successfully executed
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (label.equals("creative")) {
            return setPlayerGameMode((Player) sender, GameMode.CREATIVE);
        }
        else if (label.equals("survival")) {
            return setPlayerGameMode((Player) sender, GameMode.SURVIVAL);
        }
        else if (label.equals("cwtp")) {
            return cwtp((Player) sender, args[0]);
        }
        else if (label.equals("cw")) {
            return cw((Player) sender, args);
        }
        return false;
    }
    
    /** Handler for /creative and /survival commands
     * 
     * @param player The player who executed the command
     * @param mode The gamemode to set the player to
     */
    private boolean setPlayerGameMode(Player player, GameMode mode) {
        if (plugin.config.isCreative( player.getWorld().getName())) {
            if (player.hasPermission("cw.creative")) {
                player.setGameMode(mode);
                return true;
            }
            else {
                player.sendMessage("You do not have permission to change your game mode.");
                return false;
            }
        }
        player.sendMessage("Game mode is locked in this world.");
        return false;
    }
    
    /** Handler for /cwtp interworld teleport command
     * 
     * @param player The player who executed the command
     * @param worldName The name of the world to teleport to
     * 
     * @return True if the command was successfully executed
     */
    private boolean cwtp(Player player, String worldName) {
        if (player.hasPermission("cw.tp")) {
            World world = plugin.getServer().getWorld(worldName);
            
            if (world != null) {
                player.teleport(world.getSpawnLocation());
                return true;
            }
            else {
                player.sendMessage("World \"" + worldName + "\" does not exist");
                return false;
            }
        }
        player.sendMessage("You do not have permission to teleport between worlds.");
        return false;
    }
    
    /** Handler for ALL cw commands
     * 
     * @param player The player who executed the command
     * @param args All of the arguments sent with the command
     * 
     * @return Returns true if the command was executed successfully
     */
    private boolean cw(Player player, String[] args) {
        if (args.length >= 1) {
            if(args[0].equals("world")) {
                return cwWorld(player, args);
            }
            else if(args[0].equals("set")) {
                return cwSet(player, args);
            }
            else if (args[0].equals("help")) {
                return cwHelp(player);
            }
        }
        return cwError(player);
    }
    
    /** Handler for cw help command
     * 
     * @param player The player who executed the command
     * 
     * @return Returns true if the command was executed successfully (always)
     */
    private boolean cwHelp(Player player) {
        player.sendMessage(ChatColor.RED + "/cw world list" + ChatColor.WHITE + " - lists current worlds and their settings");
        player.sendMessage(ChatColor.RED + "/cw world create " + ChatColor.GRAY + "<world name>" + ChatColor.DARK_GRAY + "<environment> <seed>" + ChatColor.WHITE + " - Create a new world");
        player.sendMessage("  World name: unique name for the world");
        player.sendMessage("  Environment: nether, normal, or skylands (optional)");
        player.sendMessage("  Seed: Seed for the new world.");
        player.sendMessage(ChatColor.RED + "/cw world remove " + ChatColor.GRAY + "<world name>" + ChatColor.WHITE + " - unload world and prevent autoloading of that world");
        player.sendMessage(ChatColor.RED + "/cw world load " + ChatColor.GRAY + "<world name>" + ChatColor.WHITE + " - load a world and set it to autoload on server restart");
        player.sendMessage(ChatColor.RED + "/cw world load " + ChatColor.GRAY + "<world name>" + ChatColor.WHITE + " -");
        return true;
    }
    
    /** Handler for invalid cw command format
     * 
     * @param player The player who executed the command
     * 
     * @return Returns true if the command was executed successfully (never!)
     */
    private boolean cwError(Player player) {
        player.sendMessage("Invalid command, try " + ChatColor.RED + "/cw help" + ChatColor.WHITE + " for command options.");
        return false;
    }
    
    /** Handler for all "/creative set" commands
     * 
     * @param player The player who executed the command
     * @param args The arguments sent with the command
     * 
     * @return Returns true if the command was executed successfully (never!)
     */
    private boolean cwSet(Player player, String[] args) {        
        if (args.length >= 2) {        
            if (args[1].equals("creative") || args[1].equals("survival")) {
                return setWorldGameMode(player, args);
            }
        }
        return cwError(player);
    }
    
    /** Handler for invalid cw command format
     * 
     * @param player The player who executed the command
     * @param args The arguments sent with the command
     * 
     * @return Returns true if the command was executed successfully (never!)
     */
    private boolean setWorldGameMode(Player player, String[] args) {
        if (player.hasPermission("cw.set.creative")) {
            String worldName;
            
            // Gets world if specified, otherwise uses the command sender's current world
            if (args.length > 2) {
                worldName = args[2];
            }
            else {
                worldName = player.getWorld().getName();
            }
            
            plugin.config.setWorldMode(worldName, GameMode.valueOf(args[1].toUpperCase()));
            player.sendMessage(worldName + " is now set to " + args[1] + "mode.");
            return true;
        }
        player.sendMessage("You do not have permission to change a world mode.");
        return false;
    }
    
    /** Handler for all "/creative world" commands
     * 
     * @param player The player who executed the command
     * @param args The arguments sent with the command
     * 
     * @return Returns true if the command was executed successfully (never!)
     */
    private boolean cwWorld(Player player, String[] args) {        
        if (args.length >= 2) {
            if (args[1].equals("list")) {
                return worldList(player);
            }
            else if (args[1].equals("create")) {
                return worldCreate(player, args);
            }
            else if (args[1].equals("load")) {
                return worldLoad(player, args);
            }
            else if (args[1].equals("remove")) {
                return worldRemove(player, args);
            }
        }
        return cwError(player);
    }
    
    /** Handler for all "/creative world list" command
     * 
     * @param player The player who executed the command
     * 
     * @return Returns true if the command was executed successfully (always!)
     */
    private boolean worldList(Player player) {
        return true;
    }
    
    /** Handler for "/creative world create" command
     * 
     * @param player The player who executed the command
     * @param args The arguments sent with the command
     * 
     * @return Returns true if the command was executed successfully (never!)
     */
    private boolean worldCreate(Player player, String[] args) {
        if (player.hasPermission("cw.world.create")) {
            if (args.length > 2) {
                String worldName = args[2];
                if (plugin.getServer().getWorld(worldName) == null) {
                    Environment env;
                    try {
                        env = Environment.valueOf(args[3].toUpperCase());
                    }
                    catch (Exception err) {
                        player.sendMessage("Invalid environment specified: " + args[3]);
                        return false;
                    }
                    String seed = (args.length > 4) ? args[4] : null;
                    player.sendMessage("Creating world.");
                    return plugin.wm.addWorld(worldName, env, seed);
                }
                player.sendMessage("World already exists! Try to load world instead.");
                return false;
            }
            player.sendMessage("Usage: " + ChatColor.RED + "/cw world create " + ChatColor.GRAY + "<world name>" + ChatColor.DARK_GRAY + "<environment> <seed>");
            return false;
        }
        player.sendMessage("You do not have permission to create a new world.");
        return false;
    }
    
    /** Handler for "/creative world load" command
     * 
     * @param player The player who executed the command
     * @param args The arguments sent with the command
     * 
     * @return Returns true if the command was executed successfully (never!)
     */
    private boolean worldLoad(Player player, String[] args) {
        if (player.hasPermission("cw.world.load")) {
            if (args.length > 2) {
                String worldName = args[2];
                if (plugin.getServer().getWorld(worldName) != null) {
                    Environment env = Environment.NORMAL;
                    if (args.length > 3) {
                        try {
                            env = Environment.valueOf(args[3].toUpperCase());
                        }
                        catch (Exception err) {
                            player.sendMessage("Invalid environment specified: " + args[3]);
                            return false;
                        }
                    }
                    String seed = (args.length > 4) ? args[4] : null;
                    player.sendMessage("Loading world.");
                    
                    return plugin.wm.addWorld(worldName, env, seed);
                }
                player.sendMessage("World does not exist! Try to create it instead.");
                return false;
            }
            player.sendMessage("Usage: " + ChatColor.RED + "/cw world load " + ChatColor.GRAY + "<world name>" + ChatColor.DARK_GRAY + "<environment> <seed>");
            return false;
        }
        player.sendMessage("You do not have permission to load a world.");
        return false;
    }
    
    /** Handler for "/creative world remove" command
     * 
     * @param player The player who executed the command
     * @param args The arguments sent with the command
     * 
     * @return Returns true if the command was executed successfully (never!)
     */
    private boolean worldRemove(Player player, String[] args) {
        if (player.hasPermission("cw.world.remove")) {
            if (args.length > 2) {                
                if(plugin.wm.removeWorld(args[2])) {
                    player.sendMessage(args[2] + " has been removed. To completely destroy it, it's data folder still exists");
                    return true;
                }
                player.sendMessage("Unable to remove world: " + args[2]);
                return false;
            }
            player.sendMessage("Usage: " + ChatColor.RED + "/cw world remove " + ChatColor.GRAY + "<world name>");
            return false;
        }
        player.sendMessage("You do not have permission to remove a world.");
        return false;
    }
}