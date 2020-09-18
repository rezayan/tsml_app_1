package ir.topcoders.pol.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static void moveFile(File from, File to) {
        if (from.exists())
            from.renameTo(to);
    }

    public static void copyFile(String from, String to) throws IOException {
        File fromFile = new File(from);
        File toFile = new File(to);
        toFile.getParentFile().mkdirs();
        toFile.createNewFile();
        org.apache.commons.io.FileUtils.copyFile(fromFile, toFile);
    }

    public static void copyFolder(String from, String to) throws IOException {
        File fromFile = new File(from);
        File toFile = new File(to);
        org.apache.commons.io.FileUtils.copyDirectory(fromFile, toFile);
    }

    public static void deleteFile(String filePath) {
        deleteFile(new File(filePath));
    }

    public static void deleteFile(File file) {
        if (file != null && file.exists())
            file.delete();
    }

    public static void deleteFolder(String path) {
        String deleteCmd = "rm -r " + path;
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(deleteCmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
