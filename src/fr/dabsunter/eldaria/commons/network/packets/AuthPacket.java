package fr.dabsunter.eldaria.commons.network.packets;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import fr.dabsunter.eldaria.commons.network.CustomPacket;

import java.util.UUID;

/**
 * Created by David on 04/04/2017.
 */
public class AuthPacket extends CustomPacket {
	private UUID uuid;
	private String accessToken;

	public AuthPacket(UUID uuid, String accessToken) {
		super(0);
		this.uuid = uuid;
		this.accessToken = accessToken;
	}

	@Override
	public void read(ByteArrayDataInput in) {
		this.uuid = new UUID(
				in.readLong(),
				in.readLong()
		);
		this.accessToken = in.readUTF();
	}

	@Override
	public void write(ByteArrayDataOutput out) {
		out.writeLong(uuid.getMostSignificantBits());
		out.writeLong(uuid.getLeastSignificantBits());
		out.writeUTF(accessToken);
	}

	@Override
	public void process() {}
}
