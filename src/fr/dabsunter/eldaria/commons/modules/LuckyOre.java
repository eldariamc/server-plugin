package fr.dabsunter.eldaria.commons.modules;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by David on 29/04/2017.
 */
public class LuckyOre {

	private static final ArrayList<LuckEntry> ORES = new ArrayList<>();
	private static final Random RANDOM = new Random("LuckyOre".hashCode());

	public static void load(ConfigurationSection config) {
		ORES.clear();
		for (String key : config.getKeys(false)) {
			ORES.add(new LuckEntry(key, (float) config.getDouble(key, 0.0)));
		}
		ORES.add(new LuckEntry("1", 1.0F));
		System.out.println("LuckyOre module : " + ORES);
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

	private static class LuckEntry {

		private final Material ore;
		private final float probabitity;

		private LuckEntry(String ore, float probabitity) {
			this.ore = Material.matchMaterial(ore);
			this.probabitity = probabitity;
		}

		@Override
		public String toString() {
			return "LuckEntry{" +
					"ore=" + ore +
					", probabitity=" + probabitity +
					'}';
		}
	}
}
