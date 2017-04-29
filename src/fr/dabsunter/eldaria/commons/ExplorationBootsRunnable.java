package fr.dabsunter.eldaria.commons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValueAdapter;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by David on 02/04/2017.
 */
public class ExplorationBootsRunnable implements Runnable {
	private static final String METADATA = "ExplorationBoots";
	private final Main plugin;

	public ExplorationBootsRunnable(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			ItemStack item = player.getInventory().getBoots();
			if (item != null && item.getType() == Material.EXPLORATION_BOOTS) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1, true), true);
				player.addPotionEffect(new PotionEffect(PotionEffectType.FEATHER_FALLING, 100, 0, true), true);
				player.setMetadata(METADATA, new ExplorationMeta());
			} else if (player.hasMetadata(METADATA)) {
				player.removePotionEffect(PotionEffectType.SPEED);
				player.removePotionEffect(PotionEffectType.FEATHER_FALLING);
				player.removeMetadata(METADATA, plugin);
			}
		}
	}

	private class ExplorationMeta extends MetadataValueAdapter {
		protected ExplorationMeta() {
			super(plugin);
		}

		@Override
		public Object value() {
			return Boolean.TRUE;
		}

		@Override
		public void invalidate() {

		}
	}
}
