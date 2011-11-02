package me.fritz.creativeworlds;

import java.util.logging.Logger;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/** Main CreativeWorld Class
 *  
 * @Author Fritz
 * @version 0.4
 */
public class CreativeWorlds extends JavaPlugin{
    
    /**
     * Config object
     */
    public CreativeWorldsConfigHandler config;
    
    private CreativeWorldsBlockListener blockListener = new CreativeWorldsBlockListener(this);
    private CreativeWorldsCommandExecutor commandExecutor = new CreativeWorldsCommandExecutor(this);
    static final Logger log = Logger.getLogger("Minecraft");
    public WorldManager wm;

    /** Called when the plugin is enabled
     */
    @Override
    public void onEnable() {
        
        config = new CreativeWorldsConfigHandler(this);
        
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
        getCommand("cw").setExecutor(commandExecutor);
        getCommand("cwtp").setExecutor(commandExecutor);
        getCommand("creative").setExecutor(commandExecutor);
        getCommand("survival").setExecutor(commandExecutor);

        wm = new WorldManager(this);
        wm.loadWorlds();
        
        log.info("[CreativeWorlds] plugin enabled.");
    }

    /** Called when the plugin is disabled
     */
    @Override
    public void onDisable() {
            log.info("[CreativeWorlds] plugin disabled.");
    }
}