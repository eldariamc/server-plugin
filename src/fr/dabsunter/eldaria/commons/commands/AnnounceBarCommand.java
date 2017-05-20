package fr.dabsunter.eldaria.commons.commands;

import fr.dabsunter.eldaria.commons.Main;
import fr.dabsunter.eldaria.commons.network.CustomPacketHandler;
import fr.dabsunter.eldaria.commons.network.packets.ActionBarPacket;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by David on 20/05/2017.
 */
public class AnnounceBarCommand extends AbstractCommand {
	private final Main plugin;

	public AnnounceBarCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 2)
			return false;

		Player player = null;
		if (!args[0].equals("@a")) {
			player = plugin.getServer().getPlayer(args[0]);
			if (player == null)
				return false;
		}

		int duration = 60;
		boolean rainbow = false;

		int i = 1;
		try {
			for (; i < args.length; i++) {
				String arg = args[i];
				if (arg.startsWith("-duration=")) {
					duration = Integer.parseInt(arg.substring(10));
				} else if (arg.equals("-rainbow")) {
					rainbow = true;
				} else {
					break;
				}
			}
		} catch (NumberFormatException ex) {
			return false;
		}

		String message = ChatColor.translateAlternateColorCodes('&', join(args, i));

		ActionBarPacket packet = new ActionBarPacket(message, duration, rainbow);
		if (player == null)
			CustomPacketHandler.dispatch(packet, plugin.getServer().getOnlinePlayers());
		else
			CustomPacketHandler.dispatch(packet, player);

		return true;
	}
}
