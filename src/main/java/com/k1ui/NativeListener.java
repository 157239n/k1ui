package com.k1ui;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelListener;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * How this works is that native hook will call certain methods in this
 * class, like nativeMouseClicked(e), and passed in NativeKeyEvent objects
 *
 * That will in turn call the method `common(e, "mouseClicked")`, where
 * "mouseClicked" is the "type" of event
 *
 * Then, `common()` will create a JSON object capturing the event from
 * NativeKeyEvent (and NativeMouseEvent) objects and append it to events
 */
public class NativeListener implements NativeKeyListener, NativeMouseInputListener, NativeMouseWheelListener {

    private static final Map<String, List<Consumer<Object>>> callbacks = new HashMap<>();
    public static int mouseX = 0, mouseY = 0;
    public static boolean ctrl = false, shift = false, alt = false;

    private NativeListener() {
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(this);
            GlobalScreen.addNativeMouseListener(this);
            GlobalScreen.addNativeMouseMotionListener(this);
            GlobalScreen.addNativeMouseWheelListener(this);
        } catch (NativeHookException e) {
            System.err.println("There was a problem registering the native hook!");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        registerCb(
                "keyPressed",
                o -> {
                    NativeKeyEvent e = (NativeKeyEvent) o;
                    ctrl = (e.getModifiers() & NativeKeyEvent.CTRL_MASK) > 0;
                    shift = (e.getModifiers() & NativeKeyEvent.SHIFT_MASK) > 0;
                    alt = (e.getModifiers() & NativeKeyEvent.ALT_MASK) > 0;
                }
        );
        registerCb(
                "keyReleased",
                o -> {
                    NativeKeyEvent e = (NativeKeyEvent) o;
                    ctrl = (e.getModifiers() & NativeKeyEvent.CTRL_MASK) > 0;
                    shift = (e.getModifiers() & NativeKeyEvent.SHIFT_MASK) > 0;
                    alt = (e.getModifiers() & NativeKeyEvent.ALT_MASK) > 0;
                }
        );
    }

    public static void setup() {
        new NativeListener();
    }

    private static void append(JSONObject obj) {
        executeCbs("addedJsonEvent", obj);
    }

    public static void registerCb(String type, Consumer<Object> cb) {
        if (!callbacks.containsKey(type)) callbacks.put(type, new ArrayList<>());
        callbacks.get(type).add(cb);
    }

    public static void removeCb(String type, Consumer<Object> cb) {
        callbacks.get(type).remove(cb);
    }

    /**
     * Executes all callbacks of a certain type.
     *
     * @param type     Type of callback, like "keyPressed", or "addedJsonEvent"
     * @param metadata Some metadata specific to the type of callback. Can be JSON object, can be native events, can be whatever
     */
    private static void executeCbs(String type, Object metadata) {
        if (callbacks.containsKey(type)) {
            // do this to prevent java.util.ConcurrentModificationException
            List<Consumer<Object>> cbs = new ArrayList<>(callbacks.get(type));
            for (Consumer<Object> cb : cbs) cb.accept(metadata);
        }
    }

    private static void common(NativeKeyEvent e, String type) {
        JSONObject a = JS.obj(
                "type",
                type,
                "keyChar",
                e.getKeyChar(),
                "keyCode",
                e.getKeyCode(),
                "javaKeyCode",
                Dict.toJava(e.getKeyCode()),
                "keyLocation",
                e.getKeyLocation(),
                "rawCode",
                e.getRawCode(),
                "modifiers",
                e.getModifiers(),
                "isActionKey",
                e.isActionKey(),
                "timestamp",
                System.currentTimeMillis()
        );
        JSONObject b = JS.obj(
                "keyText",
                NativeKeyEvent.getKeyText(e.getKeyCode()),
                "modifiersText",
                NativeKeyEvent.getModifiersText(e.getModifiers()),
                "shift",
                (e.getModifiers() & NativeKeyEvent.SHIFT_MASK) > 0,
                "ctrl",
                (e.getModifiers() & NativeKeyEvent.CTRL_MASK) > 0,
                "alt",
                (e.getModifiers() & NativeKeyEvent.ALT_MASK) > 0,
                "numLock",
                (e.getModifiers() & NativeKeyEvent.NUM_LOCK_MASK) > 0,
                "capsLock",
                (e.getModifiers() & NativeKeyEvent.CAPS_LOCK_MASK) > 0,
                "scrollLock",
                (e.getModifiers() & NativeKeyEvent.SCROLL_LOCK_MASK) > 0
        );
        append(JS.join(a, b));
        executeCbs(type, e);
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        common(e, "keyPressed");
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        common(e, "keyReleased");
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        common(e, "keyTyped");
    }

    private void common(NativeMouseEvent e, String type) {
        mouseX = e.getX() + Screen.offsetX;
        mouseY = e.getY() + Screen.offsetY;
        JSONObject a = JS.obj("type", type, "button", e.getButton(), "clickCount", e.getClickCount(), "x", mouseX - Screen.selection.x, "y", mouseY - Screen.selection.y, "when", e.getWhen(), "id", e.getID(), "modifiers", e.getModifiers(), "timestamp", System.currentTimeMillis());
        JSONObject b = JS.obj("modifiersText", NativeKeyEvent.getModifiersText(e.getModifiers()));
        append(JS.join(a, b));
        executeCbs(type, e);
    }

    public void nativeMouseClicked(NativeMouseEvent e) {
        common(e, "mouseClicked");
    }

    public void nativeMousePressed(NativeMouseEvent e) {
        common(e, "mousePressed");
    }

    public void nativeMouseReleased(NativeMouseEvent e) {
        common(e, "mouseReleased");
    }

    public void nativeMouseMoved(NativeMouseEvent e) {
        common(e, "mouseMoved");
    }

    public void nativeMouseDragged(NativeMouseEvent e) {
        common(e, "mouseDragged");
    }

    public void nativeMouseWheelMoved(NativeMouseWheelEvent e) {
        mouseX = e.getX() + Screen.offsetX;
        mouseY = e.getY() + Screen.offsetY;
        JSONObject a = JS.obj(
                "type",
                "mouseWheelMoved",
                "button",
                e.getButton(),
                "clickCount",
                e.getClickCount(),
                "x",
                mouseX - Screen.selection.x,
                "y",
                mouseY - Screen.selection.y,
                "id",
                e.getID(),
                "modifiers",
                e.getModifiers(),
                "scrollType",
                e.getScrollType(),
                "scrollAmount",
                e.getScrollAmount(),
                "wheelRotation",
                e.getWheelRotation(),
                "wheelDirection",
                e.getWheelDirection(),
                "timestamp",
                System.currentTimeMillis()
        );
        JSONObject b = JS.obj(
                "modifiersText",
                NativeKeyEvent.getModifiersText(e.getModifiers()),
                "shift",
                (e.getModifiers() & NativeKeyEvent.SHIFT_MASK) > 0,
                "ctrl",
                (e.getModifiers() & NativeKeyEvent.CTRL_MASK) > 0,
                "alt",
                (e.getModifiers() & NativeKeyEvent.ALT_MASK) > 0,
                "numLock",
                (e.getModifiers() & NativeKeyEvent.NUM_LOCK_MASK) > 0,
                "capsLock",
                (e.getModifiers() & NativeKeyEvent.CAPS_LOCK_MASK) > 0,
                "scrollLock",
                (e.getModifiers() & NativeKeyEvent.SCROLL_LOCK_MASK) > 0
        );
        append(JS.join(a, b));
        executeCbs("mouseWheelMoved", e);
    }
}
