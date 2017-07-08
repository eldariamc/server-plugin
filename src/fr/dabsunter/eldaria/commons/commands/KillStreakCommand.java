package fr.dabsunter.eldaria.commons.commands;

import fr.dabsunter.eldaria.commons.Main;
import fr.dabsunter.eldaria.commons.modules.KillStreaks;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by David on 07/07/2017.
 */
public class KillStreakCommand extends AbstractCommand {
	private final Main plugin;

	public KillStreakCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 1)
			return false;
		OfflinePlayer player;
		if (args.length == 1) {
			player = plugin.getServer().getPlayer(args[0]);
			if (player == null) {
				player = plugin.getServer().getOfflinePlayer(args[0]);
				if (player == null) {
					sender.sendMessage("§cLe joueur " + args[0] + " n'as pas été trouvé !");
					return true;
				}
			}
		} else if (sender instanceof OfflinePlayer) {
			player = (OfflinePlayer) sender;
		} else {
			sender.sendMessage("Cette commande est destinée aux joueurs !");
			return true;
		}
		KillStreaks.KsProfile profile = KillStreaks.get(player);

		for (String line : plugin.getConfig().getStringList("kill-streak-format"))
			sender.sendMessage(format(line, player, profile));

		return true;
	}

	private static String format(String line, OfflinePlayer player, KillStreaks.KsProfile profile) {
		return ChatColor.translateAlternateColorCodes('&', line)
				.replace("{PLAYER}", player.isOnline() ? player.getPlayer().getDisplayName() : player.getName())
				.replace("{KILLS}", String.valueOf(profile.getKills()))
				.replace("{DEATHS}", String.valueOf(profile.getDeaths()))
				.replace("{RATIO}", profile.getRatio());
	}
}
