package fr.dabsunter.eldaria.commons;

import fr.dabsunter.eldaria.commons.commands.*;
import fr.dabsunter.eldaria.commons.modules.*;
import fr.dabsunter.eldaria.commons.network.CustomPacketHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by David on 02/04/2017.
 */
public class Main extends JavaPlugin {

	public boolean isMuted = false;

	private FileConfiguration ksConfig = null;
	private File ksConfigFile = null;

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
		getCommand("killstreak").setExecutor(new KillStreakCommand(this));

		new CustomPacketHandler(this).register();

		LuckyOre.load(getConfig().getConfigurationSection("lucky-ore"));
		KillStreaks.load(getKsConfig());
	}

	@Override
	public void onDisable() {
		// Ferme tout les inventaires avant un reload
		for (Player player : getServer().getOnlinePlayers())
			player.closeInventory();

		KillStreaks.save(getKsConfig());
		saveKsConfig();
	}

	public FileConfiguration getKsConfig() {
		if (ksConfig == null) {
			reloadKsConfig();
		}
		return ksConfig;
	}

	@Override
	public void reloadConfig() {
		super.reloadConfig();
		reloadKsConfig();
	}

	public void saveKsConfig() {
		if (ksConfig == null || ksConfigFile == null) {
			return;
		}
		try {
			getKsConfig().save(ksConfigFile);
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE, "Could not save config to " + ksConfigFile, ex);
		}
	}

	public void reloadKsConfig() {
		if (ksConfigFile == null) {
			ksConfigFile = new File(getDataFolder(), "kill-streaks.yml");
		}
		ksConfig = YamlConfiguration.loadConfiguration(ksConfigFile);
	}

	public void sendNews(CommandSender sender) {
		String message = getConfig().getString("news.format");
		message = message.replace("{MESSAGE}", getConfig().getString("news.message"));
		message = ChatColor.translateAlternateColorCodes('&', message);
		sender.sendMessage(message);
	}
}
