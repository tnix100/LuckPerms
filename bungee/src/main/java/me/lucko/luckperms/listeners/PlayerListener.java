package me.lucko.luckperms.listeners;

import lombok.AllArgsConstructor;
import me.lucko.luckperms.LPBungeePlugin;
import me.lucko.luckperms.commands.Util;
import me.lucko.luckperms.constants.Message;
import me.lucko.luckperms.users.User;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class PlayerListener implements Listener {
    private static final TextComponent WARN_MESSAGE = new TextComponent(Util.color(
            Message.PREFIX + "Permissions data could not be loaded. Please contact an administrator.")
    );
    private final LPBungeePlugin plugin;

    @EventHandler
    public void onPlayerPostLogin(PostLoginEvent e) {
        final ProxiedPlayer player = e.getPlayer();
        final WeakReference<ProxiedPlayer> p = new WeakReference<>(player);

        // Create user async and at post login. We're not concerned if data couldn't be loaded, the player won't be kicked.
        plugin.getDatastore().loadOrCreateUser(player.getUniqueId(), player.getName(), success -> {
            if (!success) {
                plugin.getProxy().getScheduler().schedule(plugin, () -> {
                    final ProxiedPlayer pl = p.get();
                    if (pl != null) {
                        pl.sendMessage(WARN_MESSAGE);
                    }
                }, 3, TimeUnit.SECONDS);

            } else {
                final ProxiedPlayer pl = p.get();
                if (pl != null) {
                    final User user = plugin.getUserManager().getUser(pl.getUniqueId());
                    user.refreshPermissions();
                }
            }
        });

        plugin.getDatastore().saveUUIDData(player.getName(), player.getUniqueId(), success -> {});
    }

    @EventHandler
    public void onPlayerQuit(PlayerDisconnectEvent e) {
        final ProxiedPlayer player = e.getPlayer();

        // Unload the user from memory when they disconnect
        final User user = plugin.getUserManager().getUser(player.getUniqueId());
        plugin.getUserManager().unloadUser(user);
    }
}
