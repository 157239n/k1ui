package com.k1ui;

import java.awt.*;
import java.util.ArrayList;

/**
 * So, actual screen locations are kinda strange and thus needs a bit of mapping to be consistent.
 * My setup is as follows:
 *
 * X     Y-----|
 * |-----|  1  |-----|
 * |  3  |-----|  2  |
 * |-----|     |-----|
 *
 * Where screen 1 is the primary screen, 2 and 3 are secondary screens
 *
 * Java's Robot class consider X the true origin, but native hook
 * considers Y the true origin instead. So, we're going to convert
 * everything into Java's Robot version, so that they can talk to
 * each other fine
 */
public class Screen {

    public static Screen[] screens;
    public static int offsetX = 0; // robot = native + offsetX
    public static int offsetY = 0;
    public static Rectangle selection; // user can select a specific rectangle on the screen, and all coordinates info will reference that region
    private final Rectangle robotBounds;

    private Screen(Rectangle robotBounds) {
        this.robotBounds = robotBounds;
    }

    public static String selectionJs() {
        Rectangle s = selection;
        return JS.obj("x", s.x, "y", s.y, "w", s.width, "h", s.height).toString();
    }

    /**
     * Tries to get all available screens information, like where are they,
     * what resolution and whatnot, to then deposit that into Screen.screens.
     */
    public static void setup() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();

        ArrayList<Screen> screens = new ArrayList<>();
        for (GraphicsDevice gd : gs) {
            for (GraphicsConfiguration conf : gd.getConfigurations()) {
                screens.add(new Screen(conf.getBounds()));
                break;
            }
        }
        Screen.screens = screens.toArray(new Screen[]{});
        offsetX = screens.get(0).robotBounds.x;
        offsetY = screens.get(0).robotBounds.y;
        int maxX = 0, maxY = 0;
        for (Screen s : screens) {
            maxX = Math.max(maxX, s.robotBounds.x + s.robotBounds.width);
            maxY = Math.max(maxY, s.robotBounds.y + s.robotBounds.height);
        }
        selection = new Rectangle(0, 0, maxX, maxY);
    }
}
