package com.k1ui;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class Server {
    private final HttpServer server;

    private Server(HttpServer server) {
        this.server = server;
    }

    /**
     * Registers new route. Handler function should return bytes
     */
    public void getBytes(String path, Function<HttpExchange, byte[]> f) {
        server.createContext(path, exchange -> {
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
        });
    }

    /**
     * Can work with any request types, not just GET btw
     */
    public void get(String path, Function<HttpExchange, String> f) {
        getBytes(path, httpExchange -> {
            String res = f.apply(httpExchange);
            if (res == null) return null;
            return res.getBytes(StandardCharsets.UTF_8);
        });
    }

    public void start() {
        server.start();
    }

    public static Server init(int port, int nThreads) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), port), 0);
            server.setExecutor(Executors.newFixedThreadPool(nThreads));
            return new Server(server);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Server init() {
        return init(9511, 10);
    }
}
