package com.k1ui;

import processing.core.PApplet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class App extends PApplet {
    public static String centerText = "";
    public void settings() {
        size(800, 800);
        //fullScreen();
    }

    public void setup() {
    }

    public void capture() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] gs = ge.getScreenDevices();

            for (GraphicsDevice gd : gs) {
                for (GraphicsConfiguration conf : gd.getConfigurations()) {
                    System.out.println(conf.getBounds());
                    break;
                }
            }

            Robot robot = new Robot();
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();
            System.out.println(screenSize);
            BufferedImage capture = robot.createScreenCapture(new Rectangle(screenSize));

            //robot.mouseMove(500, 500); // works

            File imageFile = new File("/home/kelvin/Downloads/a.png");
            ImageIO.write(capture, "png", imageFile);
        } catch (AWTException | IOException e) {
            e.printStackTrace();
        }
    }

    public void mouseClicked() {
        //capture();
        System.out.println("Clicked");
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
