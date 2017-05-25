package fr.dabsunter.eldaria.commons.modules;

import fr.dabsunter.eldaria.commons.network.CustomPacketHandler;
import fr.dabsunter.eldaria.commons.network.packets.ActionBarPacket;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Created by David on 20/05/2017.
 */
public class UnclaimFinder {
	private final static int REACH = 16;

	public static void find(Player player) {
		Location center = player.getLocation();
		World world = player.getWorld();
		int minX = center.getBlockX() - REACH;
		int maxX = center.getBlockX() + REACH;
		int minZ = center.getBlockZ() - REACH;
		int maxZ = center.getBlockZ() + REACH;

		int chests = 0;

		for (int y = 0; y < 256; y++)
			for (int x = minX; x <= maxX; x++)
				for (int z = minZ; z <= maxZ; z++)
					if (world.getBlockAt(x, y, z).getType().name().endsWith("CHEST"))
						chests++;

		ActionBarPacket packet = new ActionBarPacket("Au moins " + chests + " coffres trouvés dans les environs", 200, true);
		CustomPacketHandler.dispatch(packet, player);
	}
}
