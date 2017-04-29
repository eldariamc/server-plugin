package fr.dabsunter.eldaria.commons.commands;

import fr.dabsunter.eldaria.commons.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by David on 08/04/2017.
 */
public class NewsSetCommand extends AbstractCommand {

	private final Main plugin;

	public NewsSetCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1)
			return false;
		String message = join(args);
		plugin.getConfig().set("news.message", message);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.GREEN + "Le message de news a bien été modifié :");
		plugin.sendNews(sender);
		return true;
	}
}
