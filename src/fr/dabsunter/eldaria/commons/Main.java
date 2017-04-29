package fr.dabsunter.eldaria.commons;

import fr.dabsunter.eldaria.commons.commands.AnnounceCommand;
import fr.dabsunter.eldaria.commons.commands.MuteChatCommand;
import fr.dabsunter.eldaria.commons.commands.NewsSetCommand;
import fr.dabsunter.eldaria.commons.network.CustomPacketHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by David on 02/04/2017.
 */
public class Main extends JavaPlugin {

	public boolean isMuted = false;

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new EventListener(this), this);

		getServer().getScheduler().runTaskTimer(this, new ExplorationBootsRunnable(this), 10L, 10L);

		getCommand("newsset").setExecutor(new NewsSetCommand(this));
		getCommand("mutechat").setExecutor(new MuteChatCommand(this));
		getCommand("announce").setExecutor(new AnnounceCommand(this));

		new CustomPacketHandler(this).register();
	}

	public void sendNews(CommandSender sender) {
		String message = getConfig().getString("news.format");
		message = message.replace("{MESSAGE}", getConfig().getString("news.message"));
		message = ChatColor.translateAlternateColorCodes('&', message);
		sender.sendMessage(message);
	}
}
