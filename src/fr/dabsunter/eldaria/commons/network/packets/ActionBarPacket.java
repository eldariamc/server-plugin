package fr.dabsunter.eldaria.commons.network.packets;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import fr.dabsunter.eldaria.commons.network.CustomPacket;

/**
 * Created by David on 20/05/2017.
 */
public class ActionBarPacket extends CustomPacket {
	private String message;
	private int duration;
	private boolean rainbow;

	public ActionBarPacket() {
		super(2);
	}

	public ActionBarPacket(String message, int duration, boolean rainbow) {
		this();

		this.message = message;
		this.duration = duration;
		this.rainbow = rainbow;
	}

	@Override
	public void read(ByteArrayDataInput in) {
		message = in.readUTF();
		duration = in.readUnsignedShort();
		rainbow = in.readBoolean();
	}

	@Override
	public void write(ByteArrayDataOutput out) {
		out.writeUTF(message);
		out.writeShort(duration);
		out.writeBoolean(rainbow);
	}

	@Override
	public void process() {}
}
