package io.mkeasy.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Stack;
import org.apache.commons.io.FileUtils;

public class Shell {
	public static boolean move(File source, File target) throws IOException {
		if (source.isDirectory()) {
			return rename(source, target);
		}
		return renameForce(source, target);
	}

	public static boolean rename(File source, File target) throws IOException {
		if (source.exists()) {
			source = source.getCanonicalFile();

			File parent = target.getParentFile();
			if ((parent != null) && (!(parent.exists()))) {
				forceMkdir(parent);
			}

			return source.renameTo(target);
		}
		return false;
	}

	public static boolean renameForce(File source, File target) {
		try {
			moveFile(source, target);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void copy(File source, File target) throws IOException {
		source = source.getCanonicalFile();
		if (source.exists())
			if (source.isDirectory()) {
				if ((target.exists()) && (!(target.isDirectory()))) {
					throw new IOException("the target is not a directory.");
				}
				String sPath = source.getAbsolutePath();
				int sPathLength = sPath.length();

				Stack s = new Stack();
				File src = source;
				s.push(src);
				while (!(s.empty())) {
					src = (File) s.pop();
					if (src.isDirectory()) {
						File[] fs = src.listFiles();
						int i = 0;
						for (int length = fs.length; i < length; ++i)
							s.push(fs[i]);
					} else {
						File dst = new File(target, src.getAbsolutePath().substring(sPathLength));
						copyFile(src, dst);
					}
				}
			} else {
				copyFile(source, target);
			}
	}

	private static void copyFile(File source, File target) throws IOException {
		if (!(source.isFile())) {
			throw new IOException("the source is not a file.");
		}

		forceMkdir(target.getParentFile());

		if (!(target.exists())) {
			target.createNewFile();
		}

		FileUtils.copyFile(source, target);
	}

	public static void remove(File target) {
		if (!(target.isDirectory())) {
			target.delete();
		} else {
			Stack s = new Stack();
			s.addAll(Arrays.asList(target.listFiles()));
			while (true)
				try {
					remove((File) s.pop());
				} catch (EmptyStackException localEmptyStackException) {
					target.delete();
				}
		}
	}

	public static boolean forceMkdir(File directory) {
		if (!(directory.exists())) {
			synchronized (Shell.class) {
				if (!(directory.exists())) {
					return directory.mkdirs();
				}
			}
		}

		return (!(directory.isFile()));
	}

	public static void moveFile(File srcFile, File destFile) throws IOException {
		if (srcFile == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destFile == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (!(srcFile.exists())) {
			throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
		}
		if (srcFile.isDirectory()) {
			throw new IOException("Source '" + srcFile + "' is a directory");
		}

		if (destFile.isDirectory()) {
			throw new IOException("Destination '" + destFile + "' is a directory");
		}
		boolean rename = srcFile.renameTo(destFile);
		if (!(rename)) {
			copyFile(srcFile, destFile);
			if (!(srcFile.delete())) {
				deleteQuietly(destFile);
				throw new IOException(
						"Failed to delete original file '" + srcFile + "' after copy to '" + destFile + "'");
			}
		}
	}

	public static boolean deleteQuietly(File file) {
		if (file == null)
			return false;
		try {
			if (file.isDirectory())
				cleanDirectory(file);
		} catch (Exception localException1) {
		}
		try {
			return file.delete();
		} catch (Exception e) {
		}
		return false;
	}

	public static void cleanDirectory(File directory) throws IOException {
		if (!(directory.exists())) {
			String message = directory + " does not exist";
			throw new IllegalArgumentException(message);
		}

		if (!(directory.isDirectory())) {
			String message = directory + " is not a directory";
			throw new IllegalArgumentException(message);
		}

		File[] files = directory.listFiles();
		if (files == null) {
			throw new IOException("Failed to list contents of " + directory);
		}

		IOException exception = null;
		for (int i = 0; i < files.length; ++i) {
			File file = files[i];
			try {
				forceDelete(file);
			} catch (IOException ioe) {
				exception = ioe;
			}
		}

		if (exception != null)
			throw exception;
	}

	public static void forceDelete(File file) throws IOException {
		if (file.isDirectory()) {
			deleteDirectory(file);
		} else {
			boolean filePresent = file.exists();
			if (!(file.delete())) {
				if (!(filePresent)) {
					throw new FileNotFoundException("File does not exist: " + file);
				}
				String message = "Unable to delete file: " + file;
				throw new IOException(message);
			}
		}
	}

	public static void deleteDirectory(File directory) throws IOException {
		if (!(directory.exists())) {
			return;
		}

		cleanDirectory(directory);
		if (!(directory.delete())) {
			String message = "Unable to delete directory " + directory + ".";
			throw new IOException(message);
		}
	}
}