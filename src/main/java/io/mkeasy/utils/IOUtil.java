package io.mkeasy.utils;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class IOUtil {
	
	public static String toString(byte[] input) {
		try {
			return IOUtils.toString(input);
		} catch (IOException e) {}
		return null;
	}
	
	public static byte[] toBytes(String input) {
		try {
			return IOUtils.toByteArray(input);
		} catch (IOException e) {}
		return null;
	}

}
