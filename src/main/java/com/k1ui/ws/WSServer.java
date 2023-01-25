package com.k1ui.ws;

import com.k1ui.JS;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket server
 */
public class WSServer extends WebSocketServer {
    public static WSServer singleton;
    private final Map<WebSocket, WSSession> sessions = new HashMap<>();

    public WSServer(int port) {
        super(new InetSocketAddress(port));
    }

    public WSServer() {
        this(9512);
    }

    public static void setup() {
        singleton = new WSServer();
        singleton.start();
    }

    /**
     * Closes all servers
     */
    public void closeAll() {
        // TODO
    }

    @Override
    public void onOpen(WebSocket ws, ClientHandshake handshake) {
        sessions.put(ws, new WSSession(ws));
        sessions.get(ws).onOpen(handshake);
    }

    @Override
    public void onClose(WebSocket ws, int code, String reason, boolean remote) {
        sessions.get(ws).onClose(code, reason, remote);
        sessions.remove(ws);
    }

    @Override
    public void onMessage(WebSocket ws, String message) {
        sessions.get(ws).onMessage(JS.map(message));
    }

    @Override
    public void onError(WebSocket ws, Exception ex) {
        sessions.get(ws).onError(ex);
    }

    @Override
    public void onStart() {
        System.out.println("onStart ws");
    }
}
