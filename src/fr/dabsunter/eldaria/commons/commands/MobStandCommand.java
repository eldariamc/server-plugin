package fr.dabsunter.eldaria.commons.commands;

import fr.dabsunter.eldaria.commons.modules.MobStands;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class MobStandCommand extends AbstractCommand {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player && args.length >= 1) {
			switch (args[0].toLowerCase()) {
				case "spawn":
					spawnCommand((Player) sender, label, args);
					return true;
				case "remove":
					removeCommand((Player) sender, label, args);
					return true;
			}
		}
		return false;
	}

	private void spawnCommand(Player sender, String label, String[] args) {
		if (args.length >= 3) {
			EntityType type = EntityType.fromName(args[1]);
			if (type == null) {
				sender.sendMessage("§c'" + args[1] + " n'est pas une entitée valide");
				return;
			}
			String tag = args[2];
			String displayName = ChatColor.translateAlternateColorCodes('&', join(args, 3));
			Entity entity = MobStands.spawn(type, sender.getLocation(), tag, displayName);
			sender.sendMessage("§aL'entitée " + tag + " a correctement été crée (uniqueId: " + entity.getUniqueId() + ")");
		} else {
			sender.sendMessage("§c/" + label + " spawn <type> <tag> [display name]");
		}
	}

	private void removeCommand(Player sender, String label, String[] args) {
		if (args.length == 2) {
			String tag = args[1];
			if (MobStands.remove(tag))
				sender.sendMessage("§aL'entitée " + tag + " a correctement été supprimée");
			else
				sender.sendMessage("§cL'entitée " + tag + " n'existe pas (ou déjà plus)");
		} else {
			sender.sendMessage("§c/" + label + " remove <tag>");
		}
	}
}
