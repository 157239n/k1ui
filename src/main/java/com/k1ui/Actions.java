package com.k1ui;

import org.json.JSONObject;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * A bunch of actions to take
 */
public class Actions {

    private static Robot robot;

    public static void setup() throws AWTException {
        robot = new Robot();
    }

    /**
     * Move mouse to x, y
     *
     * @param x X location relative to selected region on screen
     * @param y Y location relative to selected region on screen
     */
    public static void mouse(int x, int y) {
        robot.mouseMove(x + Screen.selection.x, y + Screen.selection.y);
    }

    /**
     * Moves the mouse wheel a specific amount
     */
    public static void mouseWheel(int delta) {
        robot.mouseWheel(delta);
    }

    /**
     * Captures a section of the screen
     */
    public static BufferedImage capture() {
        return robot.createScreenCapture(Screen.selection);
    }

    /**
     * Executes a JSON event that looks like this:
     *
     * {'capsLock': False,
     * 'ctrl': False,
     * 'keyLocation': 0,
     * 'shift': True,
     * 'keyChar': '\x00',
     * 'alt': False,
     * 'keyText': 'Undefined',
     * 'numLock': True,
     * 'type': 'keyTyped',
     * 'rawCode': 65505,
     * 'modifiers': 8193,
     * 'keyCode': 0,
     * 'isActionKey': False,
     * 'javaKeyCode': -1,
     * 'modifiersText': 'Shift+Num Lock',
     * 'scrollLock': False,
     * 'timestamp': 1673220348925}
     *
     * TODO: this is not quite complete, might need to fix bugs here
     */
    public static void execute(JSONObject event, List<BufferedImage> screens) {
        int key;
        String type = event.getString("type");
        switch (type) {
            case "mouseMoved":
                robot.mouseMove(event.getInt("x") + Screen.selection.x, event.getInt("y") + Screen.selection.y);
                break;
            case "mousePressed":
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                break;
            case "mouseReleased":
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                break;
            case "keyPressed":
                key = grabCode(event);
                if (key >= 0) robot.keyPress(key);
                break;
            case "keyReleased":
                key = grabCode(event);
                if (key >= 0) robot.keyRelease(key);
                break;
            case "mouseWheelMoved":
                robot.mouseWheel(event.getInt("wheelRotation"));
                break;
            case "screen":
                screens.add(Actions.capture());
                break;
        }
    }

    private static int grabCode(JSONObject event) {
        if (event.has("javaKeyCode")) return event.getInt("javaKeyCode");
        else if (event.has("keyCode")) return Dict.toJava(event.getInt("keyCode"));
        else if (event.has("text")) return Dict.toJava(event.getString("text"));
        return -1;
    }

    public static void execute(JSONObject event) {
        execute(event, new ArrayList<>());
    }
}
