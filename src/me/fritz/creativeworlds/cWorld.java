/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package me.fritz.creativeworlds;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;

/**
 *
 * @author fritz
 */
public class cWorld  {
    
    public GameMode gameMode = GameMode.SURVIVAL;
    public MobMode mobMode = MobMode.ALL;
    public String worldName;
    private World world;
    public Environment env = Environment.NORMAL;
    public Long seed;
    private CreativeWorlds plugin;
    
    public enum MobMode {
        /**
         * Sets all mobs to spawn
         */
        ALL,
        /**
         * Only friendly/passive mobs spawn
         */
        FRIENDLY,
        /**
         * Only hostile/aggressive mobs spawn
         */
        HOSTILE,
        /**
         * No mobs at all
         */
        NONE;
    }
    
    public cWorld(String name, CreativeWorlds instance) {
        this.plugin = instance;
        this.worldName = name;
    }
    
    public void load() {
        WorldCreator wc = new WorldCreator(this.worldName);
        wc.environment(this.env);
        
        if (this.seed != null) {
            wc.seed(this.seed);
        }            
        
        this.world = plugin.getServer().createWorld(wc);
        loadMobs();
    }
    
    public void unload() {
        plugin.getServer().unloadWorld(world, true);
    }
    
    public void loadMobs() {
        if (this.mobMode == MobMode.ALL) {
            this.world.setSpawnFlags(true, true);
        }
        else if (this.mobMode == MobMode.FRIENDLY) {
            this.world.setSpawnFlags(false, true);
        }
        else if (this.mobMode == MobMode.HOSTILE) {
            this.world.setSpawnFlags(true, false);
        }
        else if (this.mobMode == MobMode.NONE) {
            this.world.setSpawnFlags(false, false);
        }
    }
}