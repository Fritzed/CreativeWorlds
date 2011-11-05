package me.fritz.creativeworlds;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;

/** Manages loading or creating the configuration file for SeattleSummer
 * 
 * @author Fritz
 *
 */
public class CreativeWorldsConfigHandler {

    protected List<String> configWorlds = new ArrayList<String>();
    private List<String> configCreativeWorlds = new ArrayList<String>();
    private static CreativeWorlds plugin;
    FileConfiguration config;

    /** Loads configuration, creates default config file if none exists.
     * 
     * @param instance The main SeattleSummer plugin instance
     */
    @SuppressWarnings("unchecked")
    public CreativeWorldsConfigHandler (CreativeWorlds instance) {
        plugin = instance;

        this.config = plugin.getConfig();

        this.configWorlds = config.getList("worlds");
        this.configCreativeWorlds = config.getList("creativeWorlds");

        config.options().copyDefaults(true);
        plugin.saveConfig();
    }
    
    /** Checks if a world is excluded by config file
     * 
     * @param worldName the name of the world being checked
     * 
     * @return false if world is in exclusion list
     */
    public boolean isCreative(String worldName) {
        return configCreativeWorlds.contains(worldName);
    }
    
    public boolean addWorld(String worldName) {
        if (!(configWorlds.contains(worldName))) {
            configWorlds.add(worldName);
            this.config.set("worlds", configWorlds);
            plugin.saveConfig();
            return true;
        }
        return false;
    }
    
    public boolean removeWorld(String worldName) {
        if (configWorlds.contains(worldName)) {
            configWorlds.remove(worldName);
            this.config.set("worlds", configWorlds);
            plugin.saveConfig();
            return true;
        }
        return false;
    }
    
    public boolean setWorldMode(String worldName, String mode) {
        return true;
    }
    
    public boolean addCreative(String worldName) {
        if (!(configCreativeWorlds.contains(worldName))) {
            configCreativeWorlds.add(worldName);
            this.config.set("creativeWorlds", configCreativeWorlds);
            plugin.saveConfig();
            return true;
        }
        return false;
    }
    
    public boolean removeCreative(String worldName) {
        if (configCreativeWorlds.contains(worldName)) {
            configCreativeWorlds.remove(worldName);
            this.config.set("creativeWorlds", configCreativeWorlds);
            plugin.saveConfig();
            return true;
        }
        return false;
    }
}
