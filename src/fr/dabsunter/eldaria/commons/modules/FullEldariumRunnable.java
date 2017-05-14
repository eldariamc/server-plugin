package fr.dabsunter.eldaria.commons.modules;

import fr.dabsunter.eldaria.commons.Main;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by David on 14/05/2017.
 */
public class FullEldariumRunnable extends EffectRunnable {

	public FullEldariumRunnable(Main plugin) {
		super(plugin, "FullEldarium",
				new PotionEffect(PotionEffectType.NIGHT_VISION, 100, 0, true),
				new PotionEffect(PotionEffectType.SPEED, 100, 0, true)
		);
	}

	@Override
	protected boolean check(Player player) {
		for (ItemStack stack : player.getInventory().getArmorContents())
			if (stack == null || stack.getType().getId() < 544 && stack.getType().getId() > 547) // Check par ID (plus opti)
				return false;
		return true;
	}
}
