package com.k1ui;

import com.k1ui.http.Server;
import com.k1ui.ws.WSServer;
import processing.core.PApplet;

import java.awt.*;

public class Main {

    public static void main(String[] args) throws AWTException {
        Applet applet = new Applet();
        Screen.setup();
        NativeListener.setup();
        Actions.setup();
        Server.setup();
        WSServer.setup();

        System.out.println("Finished setting things up");

        PApplet.runSketch(new String[]{"Title"}, applet);
    }
}
