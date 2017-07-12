package fr.dabsunter.eldaria.commons.modules;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by David on 07/07/2017.
 */
public class KillStreaks {

	private static HashMap<UUID, KsProfile> profiles;
	private static LinkedHashMap<UUID, KsProfile> sortedProfiles;

	public static void load(FileConfiguration config) {
		profiles = new HashMap<>();
		for (String key : config.getKeys(false)) {
			ConfigurationSection section = config.getConfigurationSection(key);
			KsProfile profile = get(UUID.fromString(key));
			profile.kills = section.getInt("kills");
			profile.deaths = section.getInt("deaths");
		}
		sort();
	}

	public static void save(FileConfiguration config) {
		checkState();
		for (Map.Entry<UUID, KsProfile> entry : profiles.entrySet()) {
			String key = entry.getKey().toString();
			ConfigurationSection section = config.getConfigurationSection(key);
			if (section == null)
				section = config.createSection(key);
			section.set("kills", entry.getValue().kills);
			section.set("deaths", entry.getValue().deaths);
		}
	}

	public static void sort() {
		sortedProfiles =
				profiles.entrySet().stream().
						sorted(Map.Entry.comparingByValue()).
						collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
								(e1, e2) -> e1, LinkedHashMap::new));
	}

	public static Set<Map.Entry<UUID, KsProfile>> sortedProfiles() {
		return sortedProfiles.entrySet();
	}

	public static void addKill(OfflinePlayer player) {
		get(player).kills++;
	}

	public static void addDeath(OfflinePlayer player) {
		get(player).deaths++;
	}

	public static KsProfile get(OfflinePlayer player) {
		return get(player.getUniqueId());
	}

	public static KsProfile get(UUID uuid) {
		checkState();
		return profiles.computeIfAbsent(uuid, k -> new KsProfile());
	}

	private static void checkState() {
		if (profiles == null)
			throw new IllegalStateException("KillStreaks module has not been loaded !");
	}

	public static class KsProfile implements Comparable<KsProfile> {
		int kills;
		int deaths;

		public int getKills() {
			return kills;
		}

		public int getDeaths() {
			return deaths;
		}

		public float getRawRatio() {
			return (float) kills / (float) deaths;
		}

		public String getRatio() {
			String ratio = Float.toString(getRawRatio());
			for (int i = 0; i < ratio.length(); i++) {
				if (ratio.charAt(i) == '.') {
					ratio = ratio.substring(0, Math.min(i + 3, ratio.length()));
					break;
				}
			}
			return ratio;
		}

		@Override
		public int compareTo(KsProfile that) {
			return that.kills - this.kills;
		}
	}
}
