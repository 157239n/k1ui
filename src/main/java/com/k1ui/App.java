package com.k1ui;

import processing.core.PApplet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class App extends PApplet {
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
        capture();
        System.out.println("Clicked");
    }

    public void draw() {
        background(0, 0, 0);
        ellipse(mouseX, mouseY, 20, 20);
    }
}
