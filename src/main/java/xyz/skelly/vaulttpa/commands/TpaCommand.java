package xyz.skelly.vaulttpa.commands;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.skelly.vaulttpa.RequestStorage;
import xyz.skelly.vaulttpa.TpRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static xyz.skelly.vaulttpa.VaultTPA.config;
import static xyz.skelly.vaulttpa.VaultTPA.getEconomy;

public class TpaCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length < 1) return false;
			Player destPlayer = Bukkit.getPlayer(args[0]);
			if (destPlayer == null) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("noSuchPlayer")));
				return true;
			}
			Economy economy = getEconomy();

			//distance maths!
			Location loc1 = player.getLocation();
			Location loc2 = destPlayer.getLocation();


			if (loc1.getWorld() != loc2.getWorld()) {
				loc1.setWorld(loc2.getWorld());
			}

			double distanceCost = round(loc1.distance(loc2) * config.getDouble("costPerBlock"), 1);
			if (distanceCost > config.getDouble("limit")) distanceCost = config.getDouble("limit");

			if (loc1.getWorld().hasCeiling() || loc2.getWorld().hasCeiling()) {
				distanceCost *= 8;
			}

			if (economy.getBalance(player) - distanceCost < 0) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("poor")));
			} else {
				TpRequest req = new TpRequest(player.getUniqueId(), destPlayer.getUniqueId(), player.getUniqueId(), destPlayer.getUniqueId());
				RequestStorage.createRequest(req);
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(config.getString("reqSentCost"), distanceCost + " " + economy.currencyNamePlural())));
				destPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(config.getString("tpReq"), player.getName())));
			}
		} else {
			sender.sendMessage("Only players may use this command.");
		}
		return true;
	}

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
}
