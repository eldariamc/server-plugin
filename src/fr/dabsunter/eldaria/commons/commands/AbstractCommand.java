package fr.dabsunter.eldaria.commons.commands;

import org.bukkit.command.CommandExecutor;

/**
 * Created by David on 08/04/2017.
 */
public abstract class AbstractCommand implements CommandExecutor {

	protected String join(String[] args) {
		return join(args, 0);
	}

	protected String join(String[] args, int start) {
		if (start >= args.length)
			return "";
		StringBuilder stringBuilder = new StringBuilder();
		for (; start < args.length; start++)
			stringBuilder.append(' ').append(args[start]);
		return stringBuilder.substring(1);
	}
}
