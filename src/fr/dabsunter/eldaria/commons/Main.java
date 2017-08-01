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
import org.bukkit.scheduler.BukkitRunnable;

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

	private FileConfiguration msConfig = null;
	private File msConfigFile = null;

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
		new BukkitRunnable() {
			@Override
			public void run() {
				KillStreaks.sort();
			}
		}.runTaskTimerAsynchronously(this, 200L, 200L);

		getCommand("newsset").setExecutor(new NewsSetCommand(this));
		getCommand("mutechat").setExecutor(new MuteChatCommand(this));
		getCommand("announce").setExecutor(new AnnounceCommand(this));
		getCommand("announcebar").setExecutor(new AnnounceBarCommand(this));
		getCommand("bossbar").setExecutor(new BossBarCommand(this));
		getCommand("killstreak").setExecutor(new KillStreakCommand(this));
		getCommand("topkillstreak").setExecutor(new TopKillStreakCommand(this));
		getCommand("mobstand").setExecutor(new MobStandCommand());

		new CustomPacketHandler(this).register();

		LuckyOre.load(getConfig().getConfigurationSection("lucky-ore"));
		KillStreaks.load(getKsConfig());
		MobStands.load(getMsConfig());
	}

	@Override
	public void onDisable() {
		// Ferme tout les inventaires avant un reload
		for (Player player : getServer().getOnlinePlayers())
			player.closeInventory();

		KillStreaks.save(getKsConfig());
		saveKsConfig();
		MobStands.save(getMsConfig());
		saveMsConfig();
	}

	public FileConfiguration getKsConfig() {
		if (ksConfig == null) {
			reloadKsConfig();
		}
		return ksConfig;
	}

	public FileConfiguration getMsConfig() {
		if (msConfig == null) {
			reloadKsConfig();
		}
		return msConfig;
	}

	@Override
	public void reloadConfig() {
		super.reloadConfig();
		reloadKsConfig();
		reloadMsConfig();
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
			if (!ksConfigFile.exists())
				try {
					ksConfigFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		ksConfig = YamlConfiguration.loadConfiguration(ksConfigFile);
	}

	public void saveMsConfig() {
		if (msConfig == null || msConfigFile == null) {
			return;
		}
		try {
			getKsConfig().save(msConfigFile);
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE, "Could not save config to " + ksConfigFile, ex);
		}
	}

	public void reloadMsConfig() {
		if (msConfigFile == null) {
			msConfigFile = new File(getDataFolder(), "mob-stands.yml");
			if (!msConfigFile.exists())
				try {
					msConfigFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		msConfig = YamlConfiguration.loadConfiguration(msConfigFile);
	}

	public void sendNews(CommandSender sender) {
		String message = getConfig().getString("news.format");
		message = message.replace("{MESSAGE}", getConfig().getString("news.message"));
		message = ChatColor.translateAlternateColorCodes('&', message);
		sender.sendMessage(message);
	}
}
