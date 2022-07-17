package com.k1ui.routes;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.k1ui.*;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RouteApply {
    /**
     * POST /apply
     *
     * Applies mouse and keyboard actions to the screen
     */
    public static byte[] apply(HttpExchange httpExchange) {
        try {
            NativeListener nativeListener = NativeListener.singleton;
            long beginTime = System.currentTimeMillis();
            Robot robot = new Robot();
            JSONObject data = JS.map(new String(IOUtils.toByteArray(httpExchange.getRequestBody())));
            JSONArray events = data.getJSONArray("events");

            Wrapper pressedEsc = new Wrapper(false);
            Consumer<Object> cb = e -> {
                if (((NativeKeyEvent) e).getKeyChar() == '\u001b') pressedEsc.value = true;
            };
            nativeListener.registerCb("keyTyped", cb);
            App.centerText = "Replaying events. Press escape to cancel";

            List<BufferedImage> screens = new ArrayList<>();

            Long eventStartTime = null;
            for (Object e : events) {
                if ((boolean) pressedEsc.value) break;
                JSONObject event = (JSONObject) e;
                String type = event.getString("type");
                if (eventStartTime == null) eventStartTime = event.has("timestamp") ? event.getLong("timestamp") : 0;
                long eventTime = (event.has("timestamp") ? event.getLong("timestamp") : 0) - eventStartTime;
                long realTime = System.currentTimeMillis() - beginTime;
                CU.sleep(eventTime - realTime);
                int key;
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
                        key = Dict.toJava(event.getInt("keyCode"));
                        if (key >= 0) robot.keyPress(key);
                        break;
                    case "keyReleased":
                        key = Dict.toJava(event.getInt("keyCode"));
                        if (key >= 0) robot.keyRelease(key);
                        break;
                    case "mouseWheelMoved":
                        robot.mouseWheel(event.getInt("wheelRotation"));
                        break;
                    case "screen":
                        screens.add(CU.capture());
                        break;
                }
            }
            nativeListener.removeCb("keyTyped", cb);
            App.centerText = "";
            if (screens.size() > 0) {
                httpExchange.getResponseHeaders().set("Content-Type", "application/zip");
                return CU.zip(screens.toArray(new BufferedImage[]{}));
            }
            return "Ok".getBytes(StandardCharsets.UTF_8);
        } catch (IOException | AWTException e) {
            e.printStackTrace();
            return null;
        }
    }
}
