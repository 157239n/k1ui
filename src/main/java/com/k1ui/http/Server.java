package com.k1ui.http;

import com.k1ui.Actions;
import com.k1ui.CU;
import com.k1ui.JS;
import com.k1ui.Screen;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

public class Server {
    private static HttpServer server;

    private Server(HttpServer server) {
        Server.server = server;
    }

    /**
     * Registers new route. Handler function should return bytes
     *
     * Can work with any request types, not just GET btw
     *
     * @param path Path of route, like "/record"
     */
    public static void getBytes(String path, Function<HttpExchange, byte[]> f) {
        server.createContext(
                path,
                exchange -> {
                    byte[] res;
                    int code = 200;
                    try {
                        res = f.apply(exchange);
                        if (res == null) {
                            res = "Something went wrong, check java logs for more".getBytes(StandardCharsets.UTF_8);
                            code = 400;
                        }
                    } catch (Exception e) {
                        res = e.toString().getBytes(StandardCharsets.UTF_8);
                        code = 400;
                        e.printStackTrace();
                    }
                    exchange.sendResponseHeaders(code, res.length);
                    OutputStream out = exchange.getResponseBody();
                    out.write(res);
                    out.flush();
                    out.close();
                }
        );
    }

    /**
     * Registers new route. Handler function should return string
     *
     * @param path Looks like "/record", can't contain variables
     */
    public static void get(String path, Function<HttpExchange, String> f) {
        getBytes(
                path,
                httpExchange -> {
                    String res = f.apply(httpExchange);
                    if (res == null) return null;
                    return res.getBytes(StandardCharsets.UTF_8);
                }
        );
    }

    /**
     * Like the above, but returns null instead, hence "getN"
     */
    public static void getN(String path, Consumer<HttpExchange> f) {
        get(
                path,
                httpExchange -> {
                    f.accept(httpExchange);
                    return JS.obj("message", "ok").toString();
                }
        );
    }

    /**
     * Grabs the component in the uri. If URI is "/mouse/300/400",
     * then param(ex, 1) will return "300"
     */
    private static String param(HttpExchange httpExchange, int idx) {
        return httpExchange.getRequestURI().toString().split("/")[idx + 1];
    }

    private static int paramI(HttpExchange httpExchange, int idx) {
        return Integer.parseInt(param(httpExchange, idx));
    }

    /**
     * Initializes all the endpoints. All the javadoc here will be
     * scraped by a python script
     */
    @SuppressWarnings("DanglingJavadoc")
    private static void init() {
        getN("/test", ex -> System.out.println(ex.getRequestURI()));
        /**
         * Move mouse to specified location. Example::
         *
         *    curl localhost:9511/mouse/200/300
         */
        getN("/mouse", ex -> Actions.mouse(paramI(ex, 1), paramI(ex, 2)));
        /**
         * Move mouse wheel up or down, and by how much. Example::
         *
         *    curl localhost:9511/wheel/3
         *
         * I'm not sure about the magnitude and direction, so play around
         */
        getN("/wheel", ex -> Actions.mouseWheel(paramI(ex, 1)));
        /**
         * Begins the process of selecting an area on the screen, to be used
         * as a reference frame everywhere later on. Steps:
         * - Run `curl localhost:9511/selectAreaInteractive`
         * - Move mouse to top left of the area you want
         * - Hold down ctrl key
         * - Move mouse to bottom right of the area you want
         * - Release ctrl key
         */
        get("/selectAreaInteractive", ex -> RouteSelectArea.select());
        /**
         * Selects an area on the screen. Example::
         *
         *    curl localhost:9511/selectArea/100/200/500/600
         *
         * This will select the area with top left at (100, 200), width of 500 and height of 600
         */
        getN("/selectArea", ex -> Screen.selection = new Rectangle(paramI(ex, 1), paramI(ex, 2), paramI(ex, 3), paramI(ex, 4)));
        /**
         * Gets the current selection. Example::
         *
         *    curl localhost:9511/selection
         *
         * Response:
         *
         *    {"x": 100, "y": 200, "w": 500, "h": 600}
         */
        get("/selection", ex -> Screen.selectionJs());
        /**
         * Starts a UDP screenshot stream using ffmpeg with specific
         * output width. Example::
         *
         *    curl localhost:9511/startStream/480/9520
         *
         * Assuming your screen has a resolution of 1920x1080, then
         * running this will stream your entire screen, but with
         * resolution of 480x270, basically 4x smaller than original.
         *
         * The stream will also occupy port 9520
         *
         * Might spit out some errors, so make sure to check stdout.
         */
        get("/startStream", ex -> StreamManager.start(paramI(ex, 1), paramI(ex, 2)));
        /**
         * Stops the UDP stream. Example::
         *
         *    curl localhost:9511/stopStream/4
         */
        get("/stopStream", ex -> StreamManager.stop(paramI(ex, 1)));
        /**
         * Gets the current screenshot. Uses Java's Robot class, which
         * can be slow. It's recommended to start a UDP stream with
         * `/startStream` to improve performance and latency.
         */
        getBytes("/screen", ex -> CU.toBytes(Actions.capture()));
        server.start();
    } // end init. Just a token so that it can be parsed by python doc-making tool

    /**
     * Creates and initializes a server.
     *
     * @param port     Port to listen to
     * @param nThreads Number of worker threads to spawn
     */
    public static void setup(int port, int nThreads) {
        try {
            server = HttpServer.create(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), port), 0);
            server.setExecutor(Executors.newFixedThreadPool(nThreads));
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setup() {
        setup(9511, 10);
    }
}
