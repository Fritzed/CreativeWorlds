/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package me.fritz.creativeworlds;

import org.bukkit.World;
import org.bukkit.World.Environment;

/**
 *
 * @author fritz
 */
public class WorldManager {
    CreativeWorlds plugin;
    
    public WorldManager(CreativeWorlds instance) {
        plugin = instance;
    }
    
    public boolean addWorld(String worldName, Environment env, String seed) {
        cWorld world = new cWorld(worldName,plugin);
        world.env = env;
        if (seed != null) {
            world.seed = seedString(seed);
        }
        
        plugin.config.addWorld(world);
        return true;
    }
    
    public boolean removeWorld(String worldName) {
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
        return plugin.config.getWorld(world.getName()).mobMode.name();
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