package me.fritz.creativeworlds;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import me.fritz.creativeworlds.cWorld.MobMode;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;

/** Manages loading or creating the configuration file for SeattleSummer
 * 
 * @author Fritz
 *
 */
public class CreativeWorldsConfigHandler {
    private Map<String,cWorld> worlds = new HashMap<String,cWorld>();
    private CreativeWorlds plugin;
    FileConfiguration config;
    Set<String> worldSet;
    
    /** Loads configuration, creates default config file if none exists.
     * 
     * @param instance The main SeattleSummer plugin instance
     */
    @SuppressWarnings("unchecked")
    public CreativeWorldsConfigHandler (CreativeWorlds instance) {
        plugin = instance;

        this.config = plugin.getConfig();
        config.options().copyDefaults(true);
        
        this.worldSet = config.getConfigurationSection("worlds").getKeys(false);
        
        loadWorlds();
        plugin.saveConfig();
    }
    
    /** load worlds from config file
     * 
     * @param worldName the name of the world being checked
     * 
     * @return false if world is in exclusion list
     */
    private void loadWorlds() {
        String worldName;
        for (Iterator<String> i = this.worldSet.iterator(); i.hasNext(); ) {
            worldName = i.next();
            cWorld world = new cWorld(worldName, plugin);
            world.gameMode = GameMode.valueOf(this.config.getString("worlds." + worldName + ".mode").toUpperCase());
            world.env = Environment.valueOf(this.config.getString("worlds." + worldName + ".environment").toUpperCase());
            world.mobMode = MobMode.valueOf(this.config.getString("worlds." + worldName + ".mobs").toUpperCase());
            if (this.config.getBoolean("worlds." + worldName + ".load")) {
                world.load();
            }
            this.worlds.put(worldName, world);
        }
    }
    
    /** Checks if a world is excluded by config file
     * 
     * @param worldName the name of the world being checked
     * 
     * @return false if world is in exclusion list
     */
    public boolean isCreative(String worldName) {
        if (this.worlds.get(worldName).gameMode.equals(GameMode.CREATIVE)) {
            return true;
        }
        return false;
    }
    
    /** Checks if a world is excluded by config file
     * 
     * @param world the world being checked
     * 
     * @return false if world is in exclusion list
     */
    public boolean isCreative(World world) {
        return (this.isCreative(world.getName()));
    }
    
    /** Adds a world to the config file;
     * 
     * @param world the world being added
     */
    public void addWorld(cWorld world) {
        world.load();
        
        this.worlds.put(world.worldName,world);
        this.config.getConfigurationSection("worlds").createSection(world.worldName);
        this.config.set("worlds." + world.worldName + ".mode", world.gameMode.name());
        this.config.set("worlds." + world.worldName + ".environment", world.env.name());
        this.config.set("worlds." + world.worldName + ".mobs", world.mobMode.name());
        this.config.set("worlds." + world.worldName + ".load", true);
        plugin.saveConfig();
     }
    
    /** Unloads world and removes from config file
     * 
     * @param world the world being added
     */
    public void removeWorld(String worldName) {
        this.worlds.get(worldName).unload();
        this.config.set("worlds." + worldName + ".load", false);
        plugin.saveConfig();
    }
    
    /** Checks to see if a world is in the config
     * 
     * @param world the world being checked
     * 
     * @return True if the world is in the config
     */
    public boolean worldExists(String worldName) {
        return this.worlds.containsKey(worldName);
    }
    
    /** Returns the cWorld object generated from the config file
     * 
     * @param world the world being checked
     * 
     * @return The cWorld object
     */
    public cWorld getWorld(String worldName) {
        return this.worlds.get(worldName);
    }
    
    public void setWorldMode(String worldName, GameMode mode) {
        this.worlds.get(worldName).gameMode = mode;
    }
    
    public void setMobMode(String worldName, MobMode mode) {
        this.worlds.get(worldName).mobMode = mode;
        this.worlds.get(worldName).loadMobs();
    }
}
