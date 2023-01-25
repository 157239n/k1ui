package com.k1ui.http;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.k1ui.Applet;
import com.k1ui.NativeListener;
import com.k1ui.Screen;
import com.k1ui.Wrapper;

import java.awt.*;
import java.util.function.Consumer;

public class RouteSelectArea {
    /**
     * Selects specific area on screen.
     */
    public static String select() {
        Wrapper stage = new Wrapper(0);
        Point begin = new Point(0, 0);
        Point end = new Point(0, 0);
        Consumer<Object> f2 = o -> {
            if ((((NativeKeyEvent) o).getModifiers() & NativeKeyEvent.CTRL_MASK) == 0 && ((int) stage.value) >= 1) {
                end.x = NativeListener.mouseX;
                end.y = NativeListener.mouseY;
                stage.value = 2;
            }
        };
        Consumer<Object> f1 = o -> {
            if ((((NativeKeyEvent) o).getModifiers() & NativeKeyEvent.CTRL_MASK) > 0) {
                begin.x = NativeListener.mouseX;
                begin.y = NativeListener.mouseY;
                stage.value = 1;
            }
        };
        NativeListener.registerCb("keyPressed", f1);
        NativeListener.registerCb("keyReleased", f2);
        while ((int) stage.value < 2) {
            Applet.centerText = "Selecting area.\n\nHold down ctrl, move mouse,\nthen lift ctrl to select area\non screen\n\nBegin: (" + begin.x + ", " + begin.y + ")\nEnd: (" + NativeListener.mouseX + ", " + NativeListener.mouseY + ")";
            Thread.yield();
        }
        NativeListener.removeCb("keyPressed", f1);
        NativeListener.removeCb("keyReleased", f2);
        Applet.centerText = "";
        if ((int) stage.value >= 2) {
            int x1 = Math.min(begin.x, end.x);
            int x2 = Math.max(begin.x, end.x);
            int y1 = Math.min(begin.y, end.y);
            int y2 = Math.max(begin.y, end.y);
            int w = x2 - x1;
            int h = y2 - y1;
            Screen.selection = new Rectangle(x1, y1, w, h);
            return Screen.selectionJs();
        }
        return null;
    }
}
