package fr.dabsunter.eldaria.commons.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.dabsunter.eldaria.commons.Main;
import fr.dabsunter.eldaria.commons.network.packets.ActionBarPacket;
import fr.dabsunter.eldaria.commons.network.packets.AnnouncePacket;
import fr.dabsunter.eldaria.commons.network.packets.AuthPacket;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.plugin.messaging.PluginMessageRecipient;

/**
 * Created by David on 10/04/2017.
 */
public class CustomPacketHandler implements PluginMessageListener {
	private static final String CHANNEL = "EldariaClient";
	private static final Class<? extends CustomPacket>[] PACKETS = new Class[]{
			AuthPacket.class,
			AnnouncePacket.class,
			ActionBarPacket.class
	};

	private static Main plugin;

	public CustomPacketHandler(Main plugin) {
		CustomPacketHandler.plugin = plugin;
	}

	public void register() {
		plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, CHANNEL, this);
		plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, CHANNEL);
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		try {
			if (channel.equals(CHANNEL)) {
				ByteArrayDataInput in = ByteStreams.newDataInput(message);
				CustomPacket packet = PACKETS[in.readUnsignedByte()].newInstance();
				packet.read(in);
				packet.process();
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static void dispatch(CustomPacket packet, PluginMessageRecipient... recipients) {
		byte[] out = toByteArray(packet);
		for (PluginMessageRecipient p : recipients)
			p.sendPluginMessage(plugin, CHANNEL, out);
	}

	public static void dispatch(CustomPacket packet, Iterable<? extends PluginMessageRecipient> recipients) {
		byte[] out = toByteArray(packet);
		for (PluginMessageRecipient p : recipients)
			p.sendPluginMessage(plugin, CHANNEL, out);
	}

	private static byte[] toByteArray(CustomPacket packet) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeByte(packet.id);
		packet.write(out);
		return out.toByteArray();
	}
}
