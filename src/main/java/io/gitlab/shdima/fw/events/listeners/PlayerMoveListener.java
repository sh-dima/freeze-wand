package io.gitlab.shdima.fw.events.listeners;

import io.gitlab.shdima.fw.FreezeWand;
import io.gitlab.shdima.fw.language.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerMoveListener implements Listener {

    private final FreezeWand plugin;

    public PlayerMoveListener(@NotNull final FreezeWand plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(@NotNull final PlayerMoveEvent event) {
        final Player player = event.getPlayer();

        if (plugin.getFrozenPlayers().contains(player.getUniqueId())) {
            event.setCancelled(true);

            player.sendMessage(plugin.getLanguageManager().getMessage(Message.PLAYER_CANT_MOVE, player));
        }
    }
}
