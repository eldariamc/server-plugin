package fr.dabsunter.eldaria.commons;

import fr.dabsunter.eldaria.commons.commands.*;
import fr.dabsunter.eldaria.commons.modules.ExplorationBootsRunnable;
import fr.dabsunter.eldaria.commons.modules.FullEldariumRunnable;
import fr.dabsunter.eldaria.commons.modules.LuckyOre;
import fr.dabsunter.eldaria.commons.modules.RepairOrbRunnable;
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
	public void onLoad() {
		saveDefaultConfig();
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new EventListener(this), this);

		getServer().getScheduler().runTaskTimer(this, new ExplorationBootsRunnable(this), 10L, 10L);
		getServer().getScheduler().runTaskTimer(this, new FullEldariumRunnable(this), 10L, 10L);
		new RepairOrbRunnable(this, getConfig().getConfigurationSection("repair-orb")).setup();

		getCommand("newsset").setExecutor(new NewsSetCommand(this));
		getCommand("mutechat").setExecutor(new MuteChatCommand(this));
		getCommand("announce").setExecutor(new AnnounceCommand(this));
		getCommand("announcebar").setExecutor(new AnnounceBarCommand(this));
		getCommand("bossbar").setExecutor(new BossBarCommand(this));

		new CustomPacketHandler(this).register();

		LuckyOre.load(getConfig().getConfigurationSection("lucky-ore"));
	}

	public void sendNews(CommandSender sender) {
		String message = getConfig().getString("news.format");
		message = message.replace("{MESSAGE}", getConfig().getString("news.message"));
		message = ChatColor.translateAlternateColorCodes('&', message);
		sender.sendMessage(message);
	}
}
