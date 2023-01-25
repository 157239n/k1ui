package com.k1ui.http;

import com.k1ui.JS;
import com.k1ui.Screen;
import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages ffmpeg udp streams
 *
 * Core ffmpeg commands:
 * - Record screen to file: ffmpeg -f x11grab  -s 1000x1000 -i :0.0+2560,0 -r 25 -vcodec libx264 -y abc.mp4;
 * - Start stream: ffmpeg -f x11grab -s 1000x1000 -i :0.0+2560,0 -vf scale=320:-1 -r 30 -vcodec h264 -tune zerolatency -preset superfast -fflags nobuffer -q 50 -f mpegts udp://0.0.0.0:9515
 */
public class StreamManager {
    // port to process
    private static final Map<Integer, Process> processes = new HashMap<>();

    public static String start(int sw, int port) {
        int x = Screen.selection.x;
        int y = Screen.selection.y;
        int w = Screen.selection.width;
        int h = Screen.selection.height;
        int sh = h * sw / w;
        String cmd = "ffmpeg -f x11grab -s %dx%d -i :0.0+%d,%d -vf scale=%d:%d -r 30 -vcodec h264 -tune zerolatency -preset superfast -fflags nobuffer -f mpegts udp://0.0.0.0:%d";
        String formattedCmd = String.format(cmd, w, h, x, y, sw, sh, port);
        System.out.println("ffmpeg cmd: " + formattedCmd);
        try {
            Process p = Runtime.getRuntime().exec(formattedCmd);
            processes.put(port, p);
            System.out.println("-------------- started stream, pid: " + p.pid());
            return JS.obj("message", "ok").toString();
        } catch (IOException e) {
            e.printStackTrace();
            return JS.obj("message", "failed to start stream (width: " + sw + ", port: " + port + ") with error: " + e.getMessage()).toString();
        }
    }

    private static long kill(long pid) {
        try {
            if (SystemUtils.IS_OS_WINDOWS) Runtime.getRuntime().exec("taskkill /F /PID " + pid);
            else Runtime.getRuntime().exec("kill -SIGINT " + pid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pid;
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String stop(int port) {
        System.out.println("-------------- stopping stream --------------" + processes.get(port));
        if (!processes.containsKey(port) || processes.get(port) == null)
            return JS.obj("message", "no streams are running on port " + port).toString();
        processes.get(port).destroy();
        processes.put(port, null);
        return JS.obj("message", "ok").toString();
    }
}
