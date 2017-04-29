package fr.dabsunter.eldaria.commons.commands;

import fr.dabsunter.eldaria.commons.Main;
import fr.dabsunter.eldaria.commons.network.CustomPacketHandler;
import fr.dabsunter.eldaria.commons.network.packets.AnnouncePacket;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
		for (Player player : plugin.getServer().getOnlinePlayers())
			CustomPacketHandler.dispatch(player, packet);
		return true;
	}
}
