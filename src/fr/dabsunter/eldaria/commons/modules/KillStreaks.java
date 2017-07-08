package fr.dabsunter.eldaria.commons.modules;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by David on 07/07/2017.
 */
public class KillStreaks {

	private static HashMap<UUID, KsProfile> profiles;

	public static void load(ConfigurationSection config) {
		profiles = new HashMap<>();
		for (String key : config.getKeys(false)) {
			ConfigurationSection section = config.getConfigurationSection(key);
			KsProfile profile = get(UUID.fromString(key));
			profile.kills = section.getInt("kills");
			profile.deaths = section.getInt("deaths");
		}
	}

	public static void save(ConfigurationSection config) {
		checkState();
		for (Map.Entry<UUID, KsProfile> entry : profiles.entrySet()) {
			ConfigurationSection section = config.getConfigurationSection(entry.getKey().toString());
			section.set("kills", entry.getValue().kills);
			section.set("deaths", entry.getValue().deaths);
		}
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
		KsProfile profile = profiles.get(uuid);
		if (profile == null)
			profiles.put(uuid, (profile = new KsProfile()));
		return profile;
	}

	private static void checkState() {
		if (profiles == null)
			throw new IllegalStateException("KillStreaks module has not been loaded !");
	}

	public static class KsProfile {
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
					ratio = ratio.substring(0, Math.min(i + 2, ratio.length()));
					break;
				}
			}
			return ratio;
		}
	}
}
