package fr.dabsunter.eldaria.commons.modules;

import fr.dabsunter.eldaria.commons.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by David on 14/05/2017.
 */
public class FullEldariumRunnable extends EffectRunnable {

	public FullEldariumRunnable(Main plugin) {
		super(plugin, "FullEldarium",
				new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 0, true),
				new PotionEffect(PotionEffectType.SPEED, 1000000, 0, true)
		);
	}

	@Override
	protected boolean check(Player player) {
		PlayerInventory pi = player.getInventory();
		if (pi.getHelmet() == null || pi.getHelmet().getType() != Material.ELDARIUM_HELMET)
			return false;
		if (pi.getChestplate() == null || pi.getChestplate().getType() != Material.ELDARIUM_CHESTPLATE)
			return false;
		if (pi.getLeggings() == null || pi.getLeggings().getType() != Material.ELDARIUM_LEGGINGS)
			return false;
		if (pi.getBoots() == null || pi.getBoots().getType() != Material.ELDARIUM_BOOTS)
			return false;
		return true;
	}
}
