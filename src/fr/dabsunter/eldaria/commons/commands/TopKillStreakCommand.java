package fr.dabsunter.eldaria.commons.commands;

import fr.dabsunter.eldaria.commons.Main;
import fr.dabsunter.eldaria.commons.modules.KillStreaks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Created by David on 10/07/2017.
 */
public class TopKillStreakCommand extends AbstractCommand {
	private final Main plugin;

	public TopKillStreakCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 1)
			return false;
		if (args.length == 0) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					plugin.getConfig().getString("kill-streak.top-head-format")));

			String bodyFormat = ChatColor.translateAlternateColorCodes('&',
					plugin.getConfig().getString("kill-streak.top-body-format"));
			Iterator<Map.Entry<UUID, KillStreaks.KsProfile>> profiles = KillStreaks.sortedProfiles().iterator();
			for (int rank = 1; rank <= 10 && profiles.hasNext(); rank++)
				sender.sendMessage(format(bodyFormat, profiles.next(), rank));

			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					plugin.getConfig().getString("kill-streak.top-foot-format")));
		} else {
			OfflinePlayer player = plugin.getServer().getPlayer(args[0]);
			if (player == null)
				player = plugin.getServer().getOfflinePlayer(args[0]);

			int rank = 0;
			for (Map.Entry<UUID, KillStreaks.KsProfile> entry : KillStreaks.sortedProfiles()) {
				rank++;
				if (entry.getKey().equals(player.getUniqueId())) {
					sender.sendMessage(format(ChatColor.translateAlternateColorCodes('&',
							plugin.getConfig().getString("kill-streak.top-body-format")),
							entry, rank));
					return true;
				}
			}

			sender.sendMessage("§cLe joueur " + player.getName() + " n'a pas été trouvé !");
		}

		return true;
	}

	private static String format(String line, Map.Entry<UUID, KillStreaks.KsProfile> entry, int rank) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(entry.getKey());
		return line
				.replace("{RANK}", String.valueOf(rank))
				.replace("{PLAYER}", player.isOnline() ? player.getPlayer().getDisplayName() : player.getName())
				.replace("{KILLS}", String.valueOf(entry.getValue().getKills()))
				.replace("{DEATHS}", String.valueOf(entry.getValue().getDeaths()))
				.replace("{RATIO}", entry.getValue().getRatio());
	}
}
