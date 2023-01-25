package com.k1ui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Common utilities
 */
public class CU {

    public static String sep = FileSystems.getDefault().getSeparator();

    public static void sleep(long ms) {
        if (ms < 1) return;
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void undefined() {
    }

    public static byte[] toBytes(BufferedImage image) {
        if (image == null) return null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", out);
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String toBase64(BufferedImage image) {
        return toBase64(toBytes(image));
    }

    /**
     * Creates a zip file containing a bunch of images
     */
    public static byte[] zip(BufferedImage... images) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(out));

        try {
            for (int i = 0; i < images.length; i++) {
                zos.putNextEntry(new ZipEntry("image-" + i + ".jpg"));
                zos.write(toBytes(images[i]));
                zos.closeEntry();
            }
            zos.close();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Create temporary file with specific extension. Returns null if not successful
     */
    public static String tmpFile(String ext) {
        try {
            return Files.createTempFile("", ext).toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String tmpFile() {
        return tmpFile(".tmp");
    }

    public static byte[] catByte(String file) {
        try {
            return Files.readAllBytes(Paths.get(file));
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[]{};
        }
    }

    /**
     * Writes to file. Returns whether successful or not
     */
    public static boolean file(String filename, byte[] contents) {
        try {
            Files.write(Paths.get(filename), contents);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Writes to file. Returns whether successful or not
     */
    public static boolean file(String filename, String contents) {
        try {
            Files.write(Paths.get(filename), contents.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Removes file. Returns whether successful or not
     */
    public static boolean rm(String filename) {
        return new File(filename).delete();
    }
}
