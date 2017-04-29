package fr.dabsunter.eldaria.commons.commands;

import fr.dabsunter.eldaria.commons.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by David on 09/04/2017.
 */
public class MuteChatCommand extends AbstractCommand {
	private final Main plugin;

	public MuteChatCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length != 0)
			return false;

		plugin.isMuted = label.equalsIgnoreCase("mutechat");

		plugin.getServer().broadcastMessage(
				plugin.getConfig().getString(plugin.isMuted ? "mute.mute-format" : "mute.unmute-format")
					.replace("{PLAYER}", sender.getName())
		);
		return true;
	}
}
