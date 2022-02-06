package io.mkeasy.utils;

import java.io.IOException;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

// get from ionframework
public class ObjectId implements Comparable, Serializable {

	private static final long serialVersionUID = 1355451616459500476L;

	private final int _time;
	private final int _machine;
	private final int _inc;
	private static AtomicInteger _nextInc = new AtomicInteger((new Random()).nextInt());
	private static final int _genmachine;

	static {
		try {
			StringBuilder sb = new StringBuilder();
			NetworkInterface ni;
			for (Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements(); sb.append(ni.toString()))
				ni = (NetworkInterface) e.nextElement();

			int machinePiece = sb.toString().hashCode() << 16;
			int processPiece = ManagementFactory.getRuntimeMXBean().getName().hashCode() & 65535;
			_genmachine = machinePiece | processPiece;
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}
	
	public ObjectId() {
		_time = _curtime();
		_machine = _genmachine;
		_inc = _nextInc.getAndIncrement();
	}

	public String toIdString() {
		byte b[] = toByteArray();
		StringBuilder buf = new StringBuilder(24);
		for (int i = 0; i < b.length; i++) {
			int x = b[i] & 255;
			String s = Integer.toHexString(x);
			if (s.length() == 1)
				buf.append("0");
			buf.append(s);
		}
		return buf.toString();
	}

	public byte[] toByteArray() {
		byte b[] = new byte[12];
		ByteBuffer bb = ByteBuffer.wrap(b);
		bb.putInt(_inc);
		bb.putInt(_machine);
		bb.putInt(_time);
		reverse(b);
		return b;
	}

	static void reverse(byte b[]) {
		for (int i = 0; i < b.length / 2; i++) {
			byte t = b[i];
			b[i] = b[b.length - (i + 1)];
			b[b.length - (i + 1)] = t;
		}
	}

	public String toString() {
		return toIdString();
	}

	public static int _flip(int x) {
		int z = 0;
		z |= x << 24 & -16777216;
		z |= x << 8 & 16711680;
		z |= x >> 8 & 65280;
		z |= x >> 24 & 255;
		return z;
	}

	private static int _curtime() {
		return _flip((int) (System.currentTimeMillis() / 1000L));
	}
	
	public int compareTo(Object x0) {
		return compareTo((ObjectId) x0);
	}

}