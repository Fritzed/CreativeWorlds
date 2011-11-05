/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package me.fritz.creativeworlds;

import java.util.Iterator;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;

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

    public boolean addWorld(String worldName, Environment env, String seed) {
        WorldCreator wc = new WorldCreator(worldName);
        wc.environment(env);
        if (seed != null) {
            wc.seed(seedString(seed));
        }
        plugin.getServer().createWorld(wc);
        plugin.config.addWorld(worldName);
        return true;
    }
    
    public boolean removeWorld(String worldName) {
        plugin.getServer().unloadWorld(worldName, true);
        plugin.config.removeWorld(worldName);
        return true;
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
    
    public static Long seedString(String seed) {
        try {
            return Long.parseLong(seed);
        }
        catch (Exception ex) {
            return (long)seed.hashCode();
        }
    }
}