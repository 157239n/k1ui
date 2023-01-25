package com.k1ui.ws;

import com.k1ui.Actions;
import com.k1ui.CU;
import com.k1ui.JS;
import com.k1ui.NativeListener;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.json.JSONObject;

import java.util.function.Consumer;

/**
 * WSServer will sort of handle all the session ids, then reroute
 * to the appropriate WSSession instance automatically. Separated
 * WSSession into its own class so that states can be contained
 * nicely here.
 */
public class WSSession {
    private final WebSocket ws;
    private Consumer<Object> nativeCb;

    WSSession(WebSocket ws) {
        this.ws = ws;
    }

    public void onOpen(ClientHandshake handshake) {
        System.out.println("onOpen ws");
        nativeCb = o -> {
            if (ws.isOpen()) ws.send(JS.obj("type", "newEvent", "event", o).toString());
        };
        NativeListener.registerCb("addedJsonEvent", nativeCb);
    }

    public void onClose(int code, String reason, boolean remote) {
        System.out.println("onClose ws: " + reason);
        NativeListener.removeCb("addedJsonEvent", nativeCb);
    }

    public void onMessage(JSONObject obj) {
        String type = obj.getString("type");
        System.out.print("\rmessage: " + type + "; " + Math.random() + "                  ");
        if (type.equals("test")) {
            ws.send(JS.obj("type", "client close").toString());
            return;
        }
        if (type.equals("ping")) {
            ws.send(JS.obj("type", "pong").toString());
            return;
        }
        if (type.equals("close")) {
            ws.send(JS.obj("type", "close").toString());
            ws.close();
            return;
        }
        if (type.equals("execute")) {
            Actions.execute(obj.getJSONObject("event"));
            return;
        }
        if (type.equals("screenshot")) {
            new Thread(() -> {
                ws.send(JS.obj("type", "screenshot", "screenshot", CU.toBase64(Actions.capture())).toString());
            }).start();
            return;
        }
    }

    public void onError(Exception ex) {
        System.out.println("onError ws: " + ex.getMessage());
    }
}
