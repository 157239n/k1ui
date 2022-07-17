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

import java.util.*;
import java.util.function.Consumer;

public class NativeListener implements NativeKeyListener, NativeMouseInputListener, NativeMouseWheelListener  {
    public static NativeListener singleton;
    public static int mouseX = 0, mouseY = 0;
    public static boolean ctrl = false, shift = false, alt = false;
    public Deque<JSONObject> data = new ArrayDeque<>(); // events data from the last 5 seconds
    private List<JSONObject> events = new ArrayList<>(); // latest data events, to be saved into data
    private long lastTime = System.currentTimeMillis();

    public JSONObject latestEvent() {
        if (events.size() == 0) return null;
        return events.get(events.size() - 1);
    }

    public NativeListener() {
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
        registerCb("keyPressed", o -> {
            NativeKeyEvent e = (NativeKeyEvent) o;
            ctrl = (e.getModifiers() & NativeKeyEvent.CTRL_MASK) > 0;
            shift = (e.getModifiers() & NativeKeyEvent.SHIFT_MASK) > 0;
            alt = (e.getModifiers() & NativeKeyEvent.ALT_MASK) > 0;
        });
        registerCb("keyReleased", o -> {
            NativeKeyEvent e = (NativeKeyEvent) o;
            ctrl = (e.getModifiers() & NativeKeyEvent.CTRL_MASK) > 0;
            shift = (e.getModifiers() & NativeKeyEvent.SHIFT_MASK) > 0;
            alt = (e.getModifiers() & NativeKeyEvent.ALT_MASK) > 0;
        });
        singleton = this;
    }

    private int id = 0;

    private void append(JSONObject obj) {
        events.add(obj);
        if (System.currentTimeMillis() - lastTime < 1000) return;
        lastTime = System.currentTimeMillis();
        data.add(JS.obj("id", id, "events", JS.arr(events.toArray())));
        id++;
        events = new ArrayList<>();
        if (data.size() > 5) data.removeFirst();
        commonAll("packagedEvents", data.getLast());
    }

    private final Map<String, List<Consumer<Object>>> callbacks = new HashMap<>();
    public void registerCb(String type, Consumer<Object> cb) {
        if (!callbacks.containsKey(type)) callbacks.put(type, new ArrayList<>());
        callbacks.get(type).add(cb);
        //KeyEvent.VK_UP == NativeKeyEvent.
        //KeyEvent.VK_1
    }
    public void removeCb(String type, Consumer<Object> cb) {
        callbacks.get(type).remove(cb);
    }
    private void commonAll(String type, Object event) {
        if (callbacks.containsKey(type))
            for (Consumer<Object> cb : callbacks.get(type))
                cb.accept(event);
    }

    private void common(NativeKeyEvent e, String type) {
        JSONObject a = JS.obj(
                "type", type,
                "keyChar", e.getKeyChar(),
                "keyCode", e.getKeyCode(),
                "javaKeyCode", Dict.toJava(e.getKeyCode()),
                "keyLocation", e.getKeyLocation(),
                "rawCode", e.getRawCode(),
                "modifiers", e.getModifiers(),
                "isActionKey", e.isActionKey(),
                "timestamp", System.currentTimeMillis());
        JSONObject b = JS.obj(
                "keyText", NativeKeyEvent.getKeyText(e.getKeyCode()),
                "modifiersText", NativeKeyEvent.getModifiersText(e.getModifiers()),
                "shift", (e.getModifiers() & NativeKeyEvent.SHIFT_MASK) > 0,
                "ctrl", (e.getModifiers() & NativeKeyEvent.CTRL_MASK) > 0,
                "alt", (e.getModifiers() & NativeKeyEvent.ALT_MASK) > 0,
                "numLock", (e.getModifiers() & NativeKeyEvent.NUM_LOCK_MASK) > 0,
                "capsLock", (e.getModifiers() & NativeKeyEvent.CAPS_LOCK_MASK) > 0,
                "scrollLock", (e.getModifiers() & NativeKeyEvent.SCROLL_LOCK_MASK) > 0
        );
        append(JS.join(a, Settings.descriptive ? b : JS.obj()));
        commonAll(type, e);
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
        JSONObject a = JS.obj(
                "type", type,
                "button", e.getButton(),
                "clickCount", e.getClickCount(),
                "x", mouseX - Screen.selection.x,
                "y", mouseY - Screen.selection.y,
                "when", e.getWhen(),
                "id", e.getID(),
                "modifiers", e.getModifiers(),
                "timestamp", System.currentTimeMillis());
        JSONObject b = JS.obj(
                "modifiersText", NativeKeyEvent.getModifiersText(e.getModifiers()));
        append(JS.join(a, Settings.descriptive ? b : JS.obj()));
        commonAll(type, e);
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
                "type", "mouseWheelMoved",
                "button", e.getButton(),
                "clickCount", e.getClickCount(),
                "x", mouseX - Screen.selection.x,
                "y", mouseY - Screen.selection.y,
                "id", e.getID(),

                "modifiers", e.getModifiers(),

                "scrollType", e.getScrollType(),
                "scrollAmount", e.getScrollAmount(),
                "wheelRotation", e.getWheelRotation(),
                "wheelDirection", e.getWheelDirection(),
                "timestamp", System.currentTimeMillis());
        JSONObject b = JS.obj(
                "modifiersText", NativeKeyEvent.getModifiersText(e.getModifiers()),
                "shift", (e.getModifiers() & NativeKeyEvent.SHIFT_MASK) > 0,
                "ctrl", (e.getModifiers() & NativeKeyEvent.CTRL_MASK) > 0,
                "alt", (e.getModifiers() & NativeKeyEvent.ALT_MASK) > 0,
                "numLock", (e.getModifiers() & NativeKeyEvent.NUM_LOCK_MASK) > 0,
                "capsLock", (e.getModifiers() & NativeKeyEvent.CAPS_LOCK_MASK) > 0,
                "scrollLock", (e.getModifiers() & NativeKeyEvent.SCROLL_LOCK_MASK) > 0
        );
        append(JS.join(a, Settings.descriptive ? b : JS.obj()));
        commonAll("mouseWheelMoved", e);
    }
}
