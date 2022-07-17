package com.k1ui.routes;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.k1ui.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RouteRecord {
    public static String record() {
        NativeListener nativeListener = NativeListener.singleton;
        List<JSONArray> eventss = new ArrayList<>();
        nativeListener.registerCb("packagedEvents", o -> {
            JSONObject data = (JSONObject) o;
            eventss.add(data.getJSONArray("events"));
        });
        Wrapper pressedEsc = new Wrapper(false);
        Consumer<Object> cb = e -> {
            if (((NativeKeyEvent) e).getKeyChar() == '\u001b') pressedEsc.value = true;
        };
        nativeListener.registerCb("keyTyped", cb);
        while (!(boolean) pressedEsc.value) {
            App.centerText = "Recording events.\n\nPress escape to stop recording. Last event:\n" + JS.of(NativeListener.singleton.latestEvent()).replace(",", ",\n");
            Thread.yield();
        }
        App.centerText = "";
        nativeListener.removeCb("keyTyped", cb);
        return JS.of(JS.obj("id", -1, "events", JS.join(eventss.toArray(new JSONArray[]{}))));
    }
}
