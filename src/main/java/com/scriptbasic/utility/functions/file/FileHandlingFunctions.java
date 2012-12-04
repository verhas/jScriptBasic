package com.scriptbasic.utility.functions.file;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import com.scriptbasic.Function;
import com.scriptbasic.executors.rightvalues.BasicArrayValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.utility.UtilityUtility;

/**
 * This class implements static methods, which can be used from BASIC programs
 * to access the file system.
 * <p>
 * Note that these functions are NOT registered into the BASIC interpreter by
 * default. The embedding application has to ask the interpreter to register the
 * methods of this class if it wants BASIC programs access the file system.
 * 
 * @author Peter Verhas
 * 
 */
public class FileHandlingFunctions {
	private FileHandlingFunctions() {
		UtilityUtility.throwExceptionToEnsureNobodyCallsIt();
	}

	/**
	 * Return the first character as a string.
	 * 
	 * @param sb
	 * @return the first character as string
	 */
	private static String firstChar(StringBuilder sb) {
		String result = "";
		if (sb.length() > 0) {
			result = sb.substring(0, 1);
		}
		return result;
	}

	/**
	 * Cut off the first character from the string.
	 * 
	 * @param sb
	 */
	private static void chomp(StringBuilder sb) {
		if (sb.length() > 0) {
			sb.delete(0, 1);
		}
	}

	private static String getABMode(StringBuilder sb, String... a) {
		String result = null;
		String mode = firstChar(sb);
		for (String m : a) {
			if (m.equals(mode)) {
				chomp(sb);
				result = m;
			}
		}
		if (result == null) {
			result = a[0];
		}
		return result;
	}

	private static String fetchReadWriteMode(StringBuilder sb) {
		return getABMode(sb, "r", "w", "a", "u");
	}

	private static String fetchTextBinaryMode(StringBuilder sb) {
		return getABMode(sb, "t", "b");
	}

	private static FileHandler openTextFileForRead(String fileName) {
		FileHandler result = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			result = new TextFileReader(br);
		} catch (FileNotFoundException fnfe) {
			result = null;
		}
		return result;
	}

	private static FileHandler openBinaryFileForRead(String fileName) {
		FileHandler result = null;
		try {
			InputStream is = new FileInputStream(fileName);
			result = new BinaryFileReader(is);
		} catch (FileNotFoundException fnfe) {
			result = null;
		}
		return result;
	}

	private static FileHandler openTextFileForWrite(String fileName,
			boolean append) {
		FileHandler result = null;
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName,
					append));
			result = new TextFileWriter(bw);
		} catch (IOException ioe) {
			result = null;
		}
		return result;
	}

	private static FileHandler openBinaryFileForWrite(String fileName,
			boolean append) {
		FileHandler result = null;
		try {
			BufferedOutputStream bw = new BufferedOutputStream(
					new FileOutputStream(fileName, append));
			result = new BinaryFileWriter(bw);
		} catch (IOException ioe) {
			result = null;
		}
		return result;
	}

	private static FileHandler openFileForRead(String fileName, boolean binary) {
		FileHandler result = null;
		if (binary) {
			result = openBinaryFileForRead(fileName);
		} else {
			result = openTextFileForRead(fileName);
		}
		return result;
	}

	private static FileHandler openFileForWrite(String fileName,
			boolean append, boolean binary) {
		FileHandler result = null;
		if (binary) {
			result = openBinaryFileForWrite(fileName, append);
		} else {
			result = openTextFileForWrite(fileName, append);
		}
		return result;
	}

	/**
	 * Opens a file and returns a file handler. Files can be opened read, write
	 * or append. Also the mode can be binary or text. Default is read and text.
	 * 
	 * @param fileName
	 *            the name of the file to be opened.
	 * @param mode
	 *            the mode how to open the file
	 * @return
	 */
	@Function(classification = { com.scriptbasic.classification.File.class })
	public static FileHandler open(String fileName, String mode) {
		FileHandler result = null;
		StringBuilder sb = new StringBuilder(mode);
		String rwMode = fetchReadWriteMode(sb);
		boolean append = rwMode.equals("a");
		boolean binary = fetchTextBinaryMode(sb).equals("b");
		if (rwMode.equals("r")) {
			result = openFileForRead(fileName, binary);
		} else {
			result = openFileForWrite(fileName, append, binary);
		}
		return result;
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static String readLine(FileHandler fh) throws BasicRuntimeException,
			IOException {
		if (fh instanceof TextFileReader) {
			TextFileReader tfr = (TextFileReader) fh;
			return tfr.readLine();
		} else {
			throw new BasicRuntimeException(
					"Binary reading line from file opened to write or being bindary.");
		}
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static byte[] read(FileHandler fh, int len)
			throws BasicRuntimeException, IOException {
		if (fh instanceof BinaryFileReader) {
			BinaryFileReader bfr = (BinaryFileReader) fh;
			return bfr.read(len);
		} else {
			throw new BasicRuntimeException(
					"Binary reading from file opened to write or text.");
		}
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static void printf(FileHandler fh, String line)
			throws BasicRuntimeException, IOException {
		if (fh instanceof TextFileWriter) {
			TextFileWriter tfw = (TextFileWriter) fh;
			tfw.print(line);
		} else {
			throw new BasicRuntimeException(
					"Printing to a read only or binary file");
		}
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static void printfln(FileHandler fh, String line)
			throws IOException, BasicRuntimeException {
		printf(fh, line);
		((TextFileWriter) fh).newLine();
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static void write(FileHandler fh, byte[] buffer)
			throws BasicRuntimeException, IOException {
		if (fh instanceof BinaryFileWriter) {
			BinaryFileWriter bfw = (BinaryFileWriter) fh;
			bfw.write(buffer);
		} else {
			throw new BasicRuntimeException(
					"Binary writing to a file opened to reads or text.");
		}
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static void close(FileHandler fh) throws Exception {
		fh.close();
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static void deleteFile(String fileName) {
		new File(fileName).delete();
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static boolean fileExists(String fileName) {
		return new File(fileName).exists();
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static boolean fileCanExecute(String fileName) {
		return new File(fileName).canExecute();
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static boolean fileIsReadable(String fileName) {
		return new File(fileName).canRead();
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static boolean fileIsWritable(String fileName) {
		return new File(fileName).canWrite();
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static boolean fileIsExecutable(String fileName) {
		return new File(fileName).canExecute();
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static boolean isDirectory(String fileName) {
		return new File(fileName).isDirectory();
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static boolean isFile(String fileName) {
		return new File(fileName).isDirectory();
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static String absoluteFileName(String fileName) {
		return new File(fileName).getAbsolutePath();
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static Long freeSpace(String fileName) {
		return new File(fileName).getFreeSpace();
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static String parentDirectory(String fileName) {
		return new File(fileName).getParentFile().getAbsolutePath();
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static boolean isHidden(String fileName) {
		return new File(fileName).isHidden();
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static boolean mkdir(String fileName) {
		return new File(fileName).mkdirs();
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static boolean renameFile(String fileNameFrom, String fileNameTo) {
		return new File(fileNameFrom).renameTo(new File(fileNameTo));
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static boolean setExecutable(String fileName, boolean executable,
			boolean ownerOnly) {
		return new File(fileName).setExecutable(executable, ownerOnly);
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static boolean setReadable(String fileName, boolean readable,
			boolean ownerOnly) {
		return new File(fileName).setReadable(readable, ownerOnly);
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static boolean setWritable(String fileName, boolean writable,
			boolean ownerOnly) {
		return new File(fileName).setWritable(writable, ownerOnly);
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static boolean setRedOnly(String fileName) {
		return new File(fileName).setReadOnly();
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static Long lastModified(String fileName) {
		return new File(fileName).lastModified();
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static Long fileLeLong(String fileName) {
		return new File(fileName).length();
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static boolean setLastModified(String fileName, Long time) {
		return new File(fileName).setLastModified(time);
	}

	@Function(classification = { com.scriptbasic.classification.File.class })
	public static BasicArrayValue listFiles(String fileName)
			throws ExecutionException {
		String[] files = new File(fileName).list();
		BasicArrayValue result = new BasicArrayValue();
		for (int i = 0; i < files.length; i++) {
			result.set(i, files[i]);
		}
		return result;
	}
}
