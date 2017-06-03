package fr.dabsunter.eldaria.commons;

import fr.dabsunter.eldaria.commons.modules.LuckyOre;
import fr.dabsunter.eldaria.commons.modules.UnclaimFinder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by David on 02/04/2017.
 */
public class EventListener implements Listener {
	private final Main plugin;

	public EventListener(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onItemConsume(PlayerItemConsumeEvent event) {
		if (event.getItem().getType() == Material.COOKIE)
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2400, 1), true);
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		if (event.getEntityType().name().startsWith("WITHER"))
			event.blockList().clear();
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getMaterial() == Material.UNCLAIM_FINDER)
			UnclaimFinder.find(event.getPlayer());

		/* --- Ancien système d'orbe de réparation ---
		boolean isFullRepair = event.getMaterial() == Material.DIVINE_ORB;
		if (event.getAction().name().startsWith("RIGHT_CLICK")
				&& (event.getMaterial() == Material.REPAIR_ORB || isFullRepair)) {
			PlayerInventory pi = event.getPlayer().getInventory();
			for (ItemStack stack : pi.getArmorContents())
				repair(stack);
			if (isFullRepair)
				for (ItemStack stack : pi.getContents())
					repair(stack);
			pi.setItemInHand(null);
		}*/
	}

	private static void repair(ItemStack stack) {
		if (stack != null && stack.getType().getMaxDurability() > 0)
			stack.setDurability((short) 0);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		plugin.sendNews(event.getPlayer());
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getCause() == EntityDamageEvent.DamageCause.MAGIC) {
			event.setDamage(event.getDamage(EntityDamageEvent.DamageModifier.BASE) / 5.5);
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if (plugin.isMuted && !player.isOp() && player.hasPermission("eldaria.mute.chat")) {
			player.sendMessage(ChatColor.RED + "Vous ne pouvez parler lorsque le chat est mute.");
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPortal(PortalCreateEvent event) {
		for (Block block : event.getBlocks()) {
			if (!isInSpawn(block)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onProtal(EntityPortalEvent event) {
		if (event.getTo().getWorld().getEnvironment() == World.Environment.NETHER
				&& !isInSpawn(event.getFrom().getBlock())
				|| event.getFrom().getWorld().getEnvironment() == World.Environment.NETHER
				&& !isInSpawn(event.getTo().getBlock())) {
			event.setCancelled(true);
		}
	}

	private boolean isInSpawn(Block block) {
		return block.getX() >= plugin.getConfig().getInt("spawn.minX")
				&& block.getX() <= plugin.getConfig().getInt("spawn.maxX")
				&& block.getZ() >= plugin.getConfig().getInt("spawn.minZ")
				&& block.getZ() <= plugin.getConfig().getInt("spawn.maxZ");
	}

	@EventHandler
	public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
		String command = event.getMessage().split(" ")[0].toLowerCase();
		if (!event.getPlayer().isOp() && plugin.getConfig().getStringList("command-blacklist").contains(command))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		if (block.getType() == Material.LUCKY_ORE) {
			block.setTypeId(LuckyOre.pick().getId(), false);
			event.setExpToDrop(block.getExpDrop(event.getPlayer()));
		}
	}
}
