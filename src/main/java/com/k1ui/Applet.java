package com.k1ui;

import org.apache.commons.io.IOUtils;
import processing.core.PApplet;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Processing Applet
 */
public class Applet extends PApplet {

    public static String centerText = "";

    public void settings() {
        size(800, 800);
    }

    public void setup() {
    }

    public void mouseClicked() {
        try {
            System.out.println("before");
            Process p = Runtime.getRuntime().exec("/home/kelvin/repos/java/k1ui/sl.sh 1>&2");
            System.out.println("after");
            new Thread(() -> {
                try {
                    System.out.println(IOUtils.toString(p.getInputStream(), StandardCharsets.UTF_8));
                    System.out.println(IOUtils.toString(p.getErrorStream(), StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("killing");
            Runtime.getRuntime().exec("kill -SIGINT " + p.pid());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("some err");
        }
    }

    public void draw() {
        background(0, 0, 0);
        textSize(12);
        text("mouseX: " + NativeListener.mouseX, 10, 20);
        text("mouseY: " + NativeListener.mouseY, 10, 40);
        text("ctrl: " + NativeListener.ctrl, 10, 60);
        text("shift: " + NativeListener.shift, 10, 80);
        text("alt: " + NativeListener.alt, 10, 100);
        Rectangle sel = Screen.selection;
        text("Selected area: (" + sel.x + ", " + sel.y + ", " + sel.width + ", " + sel.height + ")", 10, 120);
        textSize(16);
        text(centerText, 100, 200);
        ellipse(mouseX, mouseY, 20, 20);
    }
}
