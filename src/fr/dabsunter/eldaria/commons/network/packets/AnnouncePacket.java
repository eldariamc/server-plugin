package fr.dabsunter.eldaria.commons.network.packets;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import fr.dabsunter.eldaria.commons.network.CustomPacket;

/**
 * Created by David on 09/04/2017.
 */
public class AnnouncePacket extends CustomPacket {
	private String message;
	private int duration;

	public AnnouncePacket() {
		super(1);
	}

	public AnnouncePacket(String message, int duration) {
		this();

		this.message = message;
		this.duration = duration;
	}

	public String getMessage() {
		return message;
	}

	public int getDuration() {
		return duration;
	}

	@Override
	public void read(ByteArrayDataInput in) {
		this.message = in.readUTF();
		this.duration = in.readUnsignedShort();
	}

	@Override
	public void write(ByteArrayDataOutput out) {
		out.writeUTF(message);
		out.writeShort(duration);
	}

	@Override
	public void process() {}
}
