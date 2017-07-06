package fr.dabsunter.eldaria.commons.modules;

import fr.dabsunter.eldaria.commons.network.CustomPacketHandler;
import fr.dabsunter.eldaria.commons.network.packets.ActionBarPacket;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Created by David on 20/05/2017.
 */
public class UnclaimFinder {
	private final static int REACH = 30;

	public static void find(Player player) {
		Location center = player.getLocation();
		World world = player.getWorld();
		int minX = center.getBlockX() - REACH;
		int maxX = center.getBlockX() + REACH;
		int minZ = center.getBlockZ() - REACH;
		int maxZ = center.getBlockZ() + REACH;

		int chests = 0;

		for (int y = 0; y < world.getMaxHeight(); y++)
			for (int x = minX; x <= maxX; x++)
				for (int z = minZ; z <= maxZ; z++)
					if (isChest(world.getBlockAt(x, y, z).getType()))
						chests++;

		ActionBarPacket packet = new ActionBarPacket("Il y a " + chests + " coffres trouvés dans les environs", 200, true);
		CustomPacketHandler.dispatch(packet, player);
	}

	private static boolean isChest(Material type) {
		switch (type) {
			case CRONYXE_CHEST:
			case KOBALT_CHEST:
			case MAGIC_CHEST:
			case ZINC_CHEST:
			case CHEST:
			case TRAPPED_CHEST:
				return true;
			default:
				return false;
		}
	}
}
