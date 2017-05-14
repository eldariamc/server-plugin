package fr.dabsunter.eldaria.commons.modules;

import fr.dabsunter.eldaria.commons.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by David on 02/04/2017.
 */
public class ExplorationBootsRunnable extends EffectRunnable {

	public ExplorationBootsRunnable(Main plugin) {
		super(plugin, "ExplorationBoots",
				new PotionEffect(PotionEffectType.SPEED, 100, 1, true),
				new PotionEffect(PotionEffectType.FEATHER_FALLING, 100, 0, true)
		);
	}

	@Override
	protected boolean check(Player player) {
		ItemStack stack = player.getInventory().getBoots();
		return stack != null && stack.getType() == Material.EXPLORATION_BOOTS;
	}
}
