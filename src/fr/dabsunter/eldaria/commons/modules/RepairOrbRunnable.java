package fr.dabsunter.eldaria.commons.modules;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 03/06/2017.
 */
public class RepairOrbRunnable implements Runnable {
	private final Plugin plugin;

	private final int period;
	private final int repair;

	public  RepairOrbRunnable(Plugin plugin, ConfigurationSection config) {
		this.plugin = plugin;
		this.period = config.getInt("period", 100);
		plugin.getLogger().info("RepairOrb period: " + period);
		this.repair = config.getInt("repair", 50);
		plugin.getLogger().info("RepairOrb repair: " + repair);
	}

	public void setup() {
		BukkitTask task = plugin.getServer().getScheduler().runTaskTimer(plugin, this, period, period);
		plugin.getLogger().info("Started RepairOrb task: " + task);
	}

	@Override
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers()) {

			plugin.getLogger().info("player = " + player.getName());
			PlayerInventory pi = player.getInventory();
			ItemStack orb = pi.getOrb();

			plugin.getLogger().info("orb = " + orb);

			if (orb == null || orb.getType() != Material.REPAIR_ORB && orb.getType() != Material.DIVINE_ORB)
				continue;

			int dura = orb.getType().getMaxDurability() - orb.getDurability();
			dura = Math.min(repair, dura);

			plugin.getLogger().info("dura = " + orb.getType().getMaxDurability() + " - " + orb.getDurability() + " = " + dura);

			List<ItemStack> stacks = new ArrayList<>(40);
			for (int i = 0; i < 40; i++) {
				ItemStack stack = pi.getItem(i);
				if (isArmor(stack))
					stacks.add(stack);
			}

			if (stacks.isEmpty())
				continue;

			int usedDura = 0;
			int d = dura / stacks.size();
			for (int i = 0; i < stacks.size(); i++) {
				ItemStack stack = stacks.get(i);
				if (stack.getDurability() < d) {
					usedDura += stack.getDurability();
					stack.setDurability((short) 0);
					stacks.remove(i);
					if (stacks.isEmpty())
						break;
					d = (dura - usedDura) / stacks.size();
					i = 0;
				}
			}

			for (ItemStack stack : stacks) {
				stack.setDurability((short) (stack.getDurability() - d));
				usedDura += d;
			}

			d = orb.getDurability() + usedDura;
			if (d >= orb.getType().getMaxDurability())
				pi.setOrb(null);
			else
				orb.setDurability((short) d);
		}
	}

	private static boolean isArmor(ItemStack stack) {
		if (stack == null)
			return false;
		switch (stack.getType()) {
			case GOLD_HELMET:
			case IRON_HELMET:
			case ZINC_HELMET:
			case KOBALT_HELMET:
			case CRONYXE_HELMET:
			case DIAMOND_HELMET:
			case LEATHER_HELMET:
			case ELDARIUM_HELMET:
			case CHAINMAIL_HELMET:
			case GOLD_CHESTPLATE:
			case IRON_CHESTPLATE:
			case ZINC_CHESTPLATE:
			case KOBALT_CHESTPLATE:
			case CRONYXE_CHESTPLATE:
			case DIAMOND_CHESTPLATE:
			case LEATHER_CHESTPLATE:
			case ELDARIUM_CHESTPLATE:
			case CHAINMAIL_CHESTPLATE:
			case GOLD_LEGGINGS:
			case IRON_LEGGINGS:
			case ZINC_LEGGINGS:
			case KOBALT_LEGGINGS:
			case CRONYXE_LEGGINGS:
			case DIAMOND_LEGGINGS:
			case LEATHER_LEGGINGS:
			case ELDARIUM_LEGGINGS:
			case CHAINMAIL_LEGGINGS:
			case GOLD_BOOTS:
			case IRON_BOOTS:
			case ZINC_BOOTS:
			case KOBALT_BOOTS:
			case CRONYXE_BOOTS:
			case DIAMOND_BOOTS:
			case LEATHER_BOOTS:
			case ELDARIUM_BOOTS:
			case CHAINMAIL_BOOTS:
				return true;
			default:
				return false;
		}
	}
}
