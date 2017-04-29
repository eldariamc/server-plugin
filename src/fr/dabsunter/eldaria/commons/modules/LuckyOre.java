package fr.dabsunter.eldaria.commons.modules;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by David on 29/04/2017.
 */
public class LuckyOre {

	private static final SortedSet<LuckEntry> ORES = new TreeSet<>();
	private static final Random RANDOM = new Random("LuckyOre".hashCode());

	public static void load(ConfigurationSection config) {
		ORES.clear();
		for (String key : config.getKeys(false)) {
			ConfigurationSection section = config.getConfigurationSection(key);
			ORES.add(new LuckEntry(section.getString("ore", "0"), section.getFloat("probability", 0.0F)));
		}
		ORES.add(new LuckEntry("stone", 1.0F));
	}

	public static Material pick() {
		float rn = RANDOM.nextFloat();
		for (LuckEntry lu : ORES) {
			if (rn <= lu.probabitity) {
				return lu.ore;
			} else {
				rn -= lu.probabitity;
			}
		}

		throw new IllegalStateException("LuckyOre mudule has not been loaded yet, or not correctly.");
	}

	private static class LuckEntry implements Comparable<LuckEntry> {

		private final Material ore;
		private final float probabitity;

		private LuckEntry(String ore, float probabitity) {
			this.ore = Material.matchMaterial(ore);
			this.probabitity = probabitity;
		}

		@Override
		public int compareTo(LuckEntry that) {
			return (int) ((that.probabitity - this.probabitity) * 1000);
		}
	}
}
