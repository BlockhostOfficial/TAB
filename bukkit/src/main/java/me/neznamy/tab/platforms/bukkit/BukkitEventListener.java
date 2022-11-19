package me.neznamy.tab.platforms.bukkit;

import com.github.puregero.multilib.MultiLib;
import me.neznamy.tab.api.TabAPI;
import net.kyori.adventure.pointer.Pointer;
import org.apache.commons.lang.SerializationUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.Serializable;

/**
 * The core for bukkit forwarding events into all enabled features
 */
public class BukkitEventListener implements Listener {

    /** Platform instance */
    private final BukkitPlatform platform;

    /**
     * Constructs new instance with given parameter
     *
     * @param   platform
     *          Platform instance
     */
    public BukkitEventListener(BukkitPlatform platform) {
        this.platform = platform;
    }
    
    /**
     * Listener to PlayerQuitEvent to remove player data and forward the event to features
     *
     * @param   e
     *          quit event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e){
        if (TabAPI.getInstance().isPluginDisabled()) return;
        TabAPI.getInstance().getThreadManager().runTask(() -> TabAPI.getInstance().getFeatureManager().onQuit(TabAPI.getInstance().getPlayer(e.getPlayer().getUniqueId())));
        MultiLib.notify("tab-player-quit", e.getPlayer().getUniqueId().toString());
    }
    
    /**
     * Listener to PlayerJoinEvent to create player data and forward the event to features
     *
     * @param   e
     *          join event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e) {
        if (TabAPI.getInstance().isPluginDisabled()) return;
        TabAPI.getInstance().getThreadManager().runTask(() ->
                TabAPI.getInstance().getFeatureManager().onJoin(new BukkitTabPlayer(e.getPlayer(), platform.getProtocolVersion(e.getPlayer()))));
        MultiLib.notify("tab-player-join", e.getPlayer().getUniqueId() + ":" + platform.getProtocolVersion(e.getPlayer()));
    }

    /**
     * Listener to PlayerChangedWorldEvent to forward the event to features
     *
     * @param   e
     *          world changed event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onWorldChange(PlayerChangedWorldEvent e){
        if (TabAPI.getInstance().isPluginDisabled()) return;
        TabAPI.getInstance().getThreadManager().runTask(() ->
                TabAPI.getInstance().getFeatureManager().onWorldChange(e.getPlayer().getUniqueId(), e.getPlayer().getWorld().getName()));
        MultiLib.notify("tab-player-world-change", e.getPlayer().getUniqueId() + ":" + e.getPlayer().getWorld().getName());
    }

    /**
     * Listener to PlayerChangedWorldEvent to forward the event to features
     *
     * @param   e
     *          command preprocess event
     */
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (TabAPI.getInstance().isPluginDisabled()) return;
        if (TabAPI.getInstance().getFeatureManager().onCommand(
                TabAPI.getInstance().getPlayer(e.getPlayer().getUniqueId()), e.getMessage())) e.setCancelled(true);
    }
}