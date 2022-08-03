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

public class TpaAcceptCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sndr, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (sndr instanceof Player) {
			Player recipient = (Player) sndr;
			TpRequest tpRequest = RequestStorage.getRequest(recipient.getUniqueId());
			if (tpRequest != null) {
				Player to = Bukkit.getPlayer(tpRequest.to);
				Player from = Bukkit.getPlayer(tpRequest.from);
				Player sender = Bukkit.getPlayer(tpRequest.sentBy);
				Economy economy = getEconomy();
				//distance maths!
				Location loc1 = from.getLocation();
				Location loc2 = to.getLocation();

				if (loc1.getWorld() != loc2.getWorld()) {
					loc1.setWorld(loc2.getWorld());
				}

				double distanceCost = round(loc1.distance(loc2) * config.getDouble("costPerBlock"), 1);
				if (distanceCost > config.getDouble("limit")) distanceCost = config.getDouble("limit");

				if (loc1.getWorld().hasCeiling() || loc2.getWorld().hasCeiling()) {
					distanceCost *= 8;
				}


				if (economy.getBalance(sender) < distanceCost) {
					recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("senderPoor")));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(config.getString("failedCost"), distanceCost + " " + economy.currencyNamePlural())));
				} else {
					economy.withdrawPlayer(sender, distanceCost);
					recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("reqAccept")));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(config.getString("acceptedCost"), distanceCost + " " + economy.currencyNamePlural())));
					from.teleport(to);
				}

			} else {
				recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("noPending")));
			};
		} else {
			sndr.sendMessage("Only players may use this command.");
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
