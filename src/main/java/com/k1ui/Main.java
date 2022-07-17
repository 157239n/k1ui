package com.k1ui;

import com.k1ui.routes.RouteApply;
import com.k1ui.routes.RouteRecord;
import com.k1ui.routes.RouteSelect;
import processing.core.PApplet;

import java.awt.*;

public class Main {
    public static void main(String[] args) throws AWTException {
        App app = new App();
        Screen.setup();
        NativeListener nativeListener = new NativeListener();
        Robot robot = new Robot();

        Server server = Server.init();
        server.get("/test", httpExchange -> {
            System.out.println(httpExchange.getRequestURI());
            return "Ok";
        });
        server.get("/mouse", httpExchange -> {
            String[] splits = httpExchange.getRequestURI().toString().split("/");
            int x = Integer.parseInt(splits[2]);
            int y = Integer.parseInt(splits[3]);
            robot.mouseMove(x + Screen.selection.x, y + Screen.selection.y);
            return "Ok";
        });
        server.get("/wheel", httpExchange -> {
            String[] splits = httpExchange.getRequestURI().toString().split("/");
            robot.mouseWheel(Integer.parseInt(splits[2]));
            return "Ok";
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
