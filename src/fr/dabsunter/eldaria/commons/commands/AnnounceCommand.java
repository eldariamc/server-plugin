package fr.dabsunter.eldaria.commons.commands;

import fr.dabsunter.eldaria.commons.Main;
import fr.dabsunter.eldaria.commons.network.CustomPacketHandler;
import fr.dabsunter.eldaria.commons.network.packets.AnnouncePacket;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by David on 10/04/2017.
 */
public class AnnounceCommand extends AbstractCommand {
	private final Main plugin;

	public AnnounceCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 2)
			return false;

		int duration;
		try {
			duration = Integer.parseInt(args[0]);
		} catch (NumberFormatException ex) {
			return false;
		}

		String message = ChatColor.translateAlternateColorCodes('&', join(args, 1));

		AnnouncePacket packet = new AnnouncePacket(message, duration);
		CustomPacketHandler.dispatch(packet, plugin.getServer().getOnlinePlayers());
		return true;
	}
}
