package com.scriptbasic.utility.functions.file;

import com.scriptbasic.api.BasicFunction;
import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.spi.BasicArray;
import com.scriptbasic.utility.NoInstance;

import java.io.*;

/**
 * This class implements static methods, which can be used from BASIC programs
 * to access the file system.
 * <p>
 * Note that these functions are NOT registered into the BASIC interpreter by
 * default. The embedding application has to ask the interpreter to register the
 * methods of this class if it wants BASIC programs access the file system.
 *
 * @author Peter Verhas
 */
public class FileHandlingFunctions {
    private FileHandlingFunctions() {
        NoInstance.isPossible();
    }

    /**
     * Return the first character as a string.
     *
     * @param sb parameter
     * @return the first character as string
     */
    private static String firstChar(final StringBuilder sb) {
        String result = "";
        if (sb.length() > 0) {
            result = sb.substring(0, 1);
        }
        return result;
    }

    /**
     * Cut off the first character from the string.
     *
     * @param sb parameter
     */
    private static void chomp(final StringBuilder sb) {
        if (sb.length() > 0) {
            sb.delete(0, 1);
        }
    }

    private static String getABMode(final StringBuilder sb, final String... a) {
        String result = null;
        final var mode = firstChar(sb);
        for (final String m : a) {
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

    private static String fetchReadWriteMode(final StringBuilder sb) {
        return getABMode(sb, "r", "w", "a", "u");
    }

    private static String fetchTextBinaryMode(final StringBuilder sb) {
        return getABMode(sb, "t", "b");
    }

    private static FileHandler openTextFileForRead(final String fileName) {
        FileHandler result = null;
        try {
            final var br = new BufferedReader(new FileReader(fileName));
            result = new TextFileReader(br);
        } catch (final FileNotFoundException ignored) {
        }
        return result;
    }

    private static FileHandler openBinaryFileForRead(final String fileName) {
        FileHandler result = null;
        try {
            final var is = new FileInputStream(fileName);
            result = new BinaryFileReader(is);
        } catch (final FileNotFoundException ignored) {
        }
        return result;
    }

    private static FileHandler openTextFileForWrite(final String fileName,
                                                    final boolean append) {
        FileHandler result = null;
        try {
            final var bw = new BufferedWriter(new FileWriter(fileName,
                    append));
            result = new TextFileWriter(bw);
        } catch (final IOException ignored) {
        }
        return result;
    }

    private static FileHandler openBinaryFileForWrite(final String fileName,
                                                      final boolean append) {
        FileHandler result = null;
        try {
            final var bw = new BufferedOutputStream(
                    new FileOutputStream(fileName, append));
            result = new BinaryFileWriter(bw);
        } catch (final IOException ignored) {
        }
        return result;
    }

    private static FileHandler openFileForRead(final String fileName, final boolean binary) {
        final FileHandler result;
        if (binary) {
            result = openBinaryFileForRead(fileName);
        } else {
            result = openTextFileForRead(fileName);
        }
        return result;
    }

    private static FileHandler openFileForWrite(final String fileName,
                                                final boolean append, final boolean binary) {
        final FileHandler result;
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
     * @param fileName the name of the file to be opened.
     * @param mode     the mode how to open the file
     * @return return value
     */
    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static FileHandler open(final String fileName, final String mode) {
        final FileHandler result;
        final var sb = new StringBuilder(mode);
        final var rwMode = fetchReadWriteMode(sb);
        final var append = rwMode.equals("a");
        final var binary = fetchTextBinaryMode(sb).equals("b");
        if (rwMode.equals("r")) {
            result = openFileForRead(fileName, binary);
        } else {
            result = openFileForWrite(fileName, append, binary);
        }
        return result;
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static String readLine(final FileHandler fh) throws BasicRuntimeException,
            IOException {
        if (fh instanceof TextFileReader) {
            final var tfr = (TextFileReader) fh;
            return tfr.readLine();
        } else {
            throw new BasicRuntimeException(
                    "Binary reading line from file opened to write or being bindary.");
        }
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static byte[] read(final FileHandler fh, final int len)
            throws BasicRuntimeException, IOException {
        if (fh instanceof BinaryFileReader) {
            final var bfr = (BinaryFileReader) fh;
            return bfr.read(len);
        } else {
            throw new BasicRuntimeException(
                    "Binary reading from file opened to write or text.");
        }
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static void printf(final FileHandler fh, final String line)
            throws BasicRuntimeException, IOException {
        if (fh instanceof TextFileWriter) {
            final var tfw = (TextFileWriter) fh;
            tfw.print(line);
        } else {
            throw new BasicRuntimeException(
                    "Printing to a read only or binary file");
        }
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static void printfln(final FileHandler fh, final String line)
            throws IOException, BasicRuntimeException {
        printf(fh, line);
        ((TextFileWriter) fh).newLine();
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static void write(final FileHandler fh, final byte[] buffer)
            throws BasicRuntimeException, IOException {
        if (fh instanceof BinaryFileWriter) {
            final var bfw = (BinaryFileWriter) fh;
            bfw.write(buffer);
        } else {
            throw new BasicRuntimeException(
                    "Binary writing to a file opened to reads or text.");
        }
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static void close(final FileHandler fh) throws Exception {
        fh.close();
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static void deleteFile(final String fileName) {
        //noinspection ResultOfMethodCallIgnored
        new File(fileName).delete();
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static boolean fileExists(final String fileName) {
        return new File(fileName).exists();
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static boolean fileCanExecute(final String fileName) {
        return new File(fileName).canExecute();
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static boolean fileIsReadable(final String fileName) {
        return new File(fileName).canRead();
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static boolean fileIsWritable(final String fileName) {
        return new File(fileName).canWrite();
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static boolean fileIsExecutable(final String fileName) {
        return new File(fileName).canExecute();
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static boolean isDirectory(final String fileName) {
        return new File(fileName).isDirectory();
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static boolean isFile(final String fileName) {
        return new File(fileName).isDirectory();
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static String absoluteFileName(final String fileName) {
        return new File(fileName).getAbsolutePath();
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static Long freeSpace(final String fileName) {
        return new File(fileName).getFreeSpace();
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static String parentDirectory(final String fileName) {
        return new File(fileName).getParentFile().getAbsolutePath();
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static boolean isHidden(final String fileName) {
        return new File(fileName).isHidden();
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static boolean mkdir(final String fileName) {
        return new File(fileName).mkdirs();
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static boolean renameFile(final String fileNameFrom, final String fileNameTo) {
        return new File(fileNameFrom).renameTo(new File(fileNameTo));
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static boolean setExecutable(final String fileName, final boolean executable,
                                        final boolean ownerOnly) {
        return new File(fileName).setExecutable(executable, ownerOnly);
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static boolean setReadable(final String fileName, final boolean readable,
                                      final boolean ownerOnly) {
        return new File(fileName).setReadable(readable, ownerOnly);
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static boolean setWritable(final String fileName, final boolean writable,
                                      final boolean ownerOnly) {
        return new File(fileName).setWritable(writable, ownerOnly);
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static boolean setRedOnly(final String fileName) {
        return new File(fileName).setReadOnly();
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static Long lastModified(final String fileName) {
        return new File(fileName).lastModified();
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static Long fileLength(final String fileName) {
        return new File(fileName).length();
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static boolean setLastModified(final String fileName, final Long time) {
        return new File(fileName).setLastModified(time);
    }

    @BasicFunction(classification = com.scriptbasic.classification.File.class)
    public static BasicArray listFiles(final String fileName)
            throws ScriptBasicException {
        final String[] files = new File(fileName).list();
        return BasicArray.create(files);
    }
}
