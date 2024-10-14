package io.gitlab.shdima.fw;

import io.gitlab.shdima.fw.commands.GiveFreezeWandCommand;
import io.gitlab.shdima.fw.commands.SetLanguageCommand;
import io.gitlab.shdima.fw.data.player.PlayerDataManager;
import io.gitlab.shdima.fw.events.listeners.FreezeListener;
import io.gitlab.shdima.fw.events.listeners.PlayerMoveListener;
import io.gitlab.shdima.fw.language.LanguageManager;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public final class FreezeWand extends JavaPlugin {

    private final NamespacedKey isItemFreezeWandKey = new NamespacedKey(this, "is_freeze_wand");
    private final ArrayList<UUID> frozenPlayers = new ArrayList<>();
    private PlayerDataManager playerDataManager;
    private LanguageManager languageManager;

    public NamespacedKey getIsItemFreezeWandKey() {
        return isItemFreezeWandKey;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public ArrayList<UUID> getFrozenPlayers() {
        return frozenPlayers;
    }

    @Override
    public void onEnable() {
        getDataFolder().mkdir();
        saveDefaultConfig();

        CommandAPIBukkitConfig commandAPIConfig = new CommandAPIBukkitConfig(this);

        CommandAPI.onLoad(commandAPIConfig);
        CommandAPI.onEnable();

        playerDataManager = new PlayerDataManager(this);
        languageManager = new LanguageManager(this);

        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new FreezeListener(this), this);
        pluginManager.registerEvents(new PlayerMoveListener(this), this);

        new SetLanguageCommand(this);

        new GiveFreezeWandCommand(this);
    }

    @Override
    public void onDisable() {
        if (playerDataManager != null) {
            playerDataManager.save();
        }
    }
}
