package fr.dabsunter.eldaria.commons.modules;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MobStands {
	private static final HashMap<String, UUID> MOBS = new HashMap<>();

	public static void load(ConfigurationSection config) {
		MOBS.clear();
		for (String key : config.getKeys(false))
			MOBS.put(key, UUID.fromString(config.getString(key)));
	}

	public static void save(ConfigurationSection config) {
		for (Map.Entry<String, UUID> entry : MOBS.entrySet())
			config.set(entry.getKey(), entry.getValue().toString());
	}

	public static Entity spawn(EntityType type, Location location, String tag, String displayName) {
		Entity entity = location.getWorld().spawnEntity(location, type);
		if (entity instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) entity;
			living.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, 6, true), true);
			living.setCanPickupItems(false);
			if (!displayName.isEmpty()) {
				living.setCustomName(displayName);
				living.setCustomNameVisible(true);
			}
		}
		MOBS.put(tag, entity.getUniqueId());
		return entity;
	}

	public static boolean remove(String tag) {
		UUID uuid = MOBS.get(tag);
		if (uuid == null)
			return false;
		MOBS.remove(tag);
		Entity entity = getEntity(uuid);
		if (entity != null) {
			entity.remove();
			return true;
		}
		return false;
	}

	public static boolean isMobStand(Entity entity) {
		return MOBS.containsValue(entity);
	}

	private static Entity getEntity(UUID uuid) {
		for (World w : Bukkit.getWorlds())
			for (Entity e : w.getEntities())
				if (e.getUniqueId().equals(uuid))
					return e;
		return null;
	}
}
