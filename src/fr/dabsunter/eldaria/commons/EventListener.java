package fr.dabsunter.eldaria.commons;

import fr.dabsunter.eldaria.commons.modules.KillStreaks;
import fr.dabsunter.eldaria.commons.modules.LuckyOre;
import fr.dabsunter.eldaria.commons.modules.UnclaimFinder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

import static fr.dabsunter.eldaria.commons.Utils.randInRange;

/**
 * Created by David on 02/04/2017.
 */
public class EventListener implements Listener {
	private static final Random XP_RAND = new Random("XpOre".hashCode() << 32 + "RandGen".hashCode());
	private final Main plugin;

	public EventListener(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onItemConsume(PlayerItemConsumeEvent event) {
		if (event.getItem().getType() == Material.MILK_DONUT) {
			Player player = event.getPlayer();
			player.removePotionEffect(PotionEffectType.SLOW);
			player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
			player.removePotionEffect(PotionEffectType.CONFUSION);
			player.removePotionEffect(PotionEffectType.BLINDNESS);
			player.removePotionEffect(PotionEffectType.HUNGER);
			player.removePotionEffect(PotionEffectType.WEAKNESS);
			player.removePotionEffect(PotionEffectType.POISON);
			player.removePotionEffect(PotionEffectType.WITHER);
		}
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
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof LivingEntity))
			return;
		LivingEntity damager = (LivingEntity) event.getDamager();
		for (PotionEffect effect : damager.getActivePotionEffects()) {
			if (effect.getType() == PotionEffectType.INCREASE_DAMAGE) {
				int level = effect.getAmplifier() + 1;
				event.setDamage(event.getDamage() - level * 2.5);
				break;
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player killed = event.getEntity();
		KillStreaks.addDeath(killed);
		Player killer = killed.getKiller();
		if (killer != null)
			KillStreaks.addKill(killer);
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

	@SuppressWarnings({"deprecation"})
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		switch (block.getType()) {
			case LUCKY_ORE:
				block.setTypeId(LuckyOre.pick().getId(), false);
				event.setExpToDrop(block.getExpDrop(event.getPlayer()));
				break;
			case XP_ORE:
				event.setExpToDrop(randInRange(XP_RAND, 24, 48));
				break;
		}
	}
}
