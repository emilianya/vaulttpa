package xyz.skelly.vaulttpa;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.skelly.vaulttpa.commands.TpaAcceptCommand;
import xyz.skelly.vaulttpa.commands.TpaCommand;
import xyz.skelly.vaulttpa.commands.TpaHereCommand;

public final class VaultTPA extends JavaPlugin {
    private static Economy econ = null;
    private static Chat chat = null;
    public static FileConfiguration config;
    @Override
    public void onEnable() {
        config = this.getConfig();
        // Plugin startup logic
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        config.options().copyDefaults(true);
        saveConfig();

        this.getCommand("tpa").setExecutor(new TpaCommand());
        this.getCommand("tpahere").setExecutor(new TpaHereCommand());
        this.getCommand("tpaccept").setExecutor(new TpaAcceptCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static Chat getChat() {
        return chat;
    }
}
