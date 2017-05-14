package fr.dabsunter.eldaria.commons.modules;

import fr.dabsunter.eldaria.commons.Main;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValueAdapter;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;
import java.util.List;

/**
 * Created by David on 14/05/2017.
 */
public abstract class EffectRunnable implements Runnable {
	private final Main plugin;
	private final String metadata;
	private final List<PotionEffect> effects;

	protected EffectRunnable(Main plugin, String metadata, PotionEffect... effects) {
		this.plugin = plugin;
		this.metadata = metadata;
		this.effects = Arrays.asList(effects);
	}

	@Override
	public void run() {
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			if (check(player)) {
				for (PotionEffect effect : effects)
					player.addPotionEffect(effect, true);
				player.setMetadata(metadata, new HasEffectMeta());
			} else if (player.hasMetadata(metadata)) {
				for (PotionEffect effect : effects)
					player.removePotionEffect(effect.getType());
				player.removeMetadata(metadata, plugin);
			}
		}
	}

	protected abstract boolean check(Player player);

	private class HasEffectMeta extends MetadataValueAdapter {
		private HasEffectMeta() {
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
