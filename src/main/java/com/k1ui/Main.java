package com.k1ui;

import com.k1ui.routes.RouteApply;
import com.k1ui.routes.RouteRecord;
import com.k1ui.routes.RouteSelect;
import processing.core.PApplet;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        Screen.setup();
        NativeListener nativeListener = new NativeListener();

        Server server = Server.init();
        server.get("/test", httpExchange -> {
            System.out.println(httpExchange.getRequestURI());
            return null;
        });
        server.get("/descriptive", httpExchange -> {
            Settings.descriptive = true;
            return "Ok";
        });
        server.get("/events", httpExchange -> JS.of(nativeListener.data.getLast()));
        server.get("/select", httpExchange -> RouteSelect.select());
        server.getBytes("/apply", RouteApply::apply);
        server.get("/record", httpExchange -> RouteRecord.record());
        server.getBytes("/screen", httpExchange -> CU.imageToBytes(CU.capture()));
        server.start();

        PApplet.runSketch(new String[]{"Title"}, app);
    }
}
