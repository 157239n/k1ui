package com.k1ui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Common utilities
 */
public class CU {
    public static void sleep(long ms) {
        if (ms < 1) return;
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage capture() {
        try {
            return new Robot().createScreenCapture(Screen.selection);
        } catch (AWTException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] imageToBytes(BufferedImage image) {
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

    public static byte[] zip(BufferedImage... images) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(out));

        try {
            for (int i = 0; i < images.length; i++) {
                zos.putNextEntry(new ZipEntry("image-" + i + ".jpg"));
                zos.write(imageToBytes(images[i]));
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
}
