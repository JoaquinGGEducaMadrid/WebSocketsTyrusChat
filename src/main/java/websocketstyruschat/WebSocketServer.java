package websocketstyruschat;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.tyrus.server.Server;

public class WebSocketServer {

    public static void main(String[] args) {

        // Render asigna el puerto WebSocket en $PORT
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

        // 1. Servidor HTTP para cliente.html
        HttpServer httpServer = HttpServer.createSimpleServer(null, port);
        httpServer.getServerConfiguration().addHttpHandler(
                new StaticHttpHandler("src/main/resources/public/"), "/"
        );

        // 2. Servidor WebSocket Tyrus
        Server wsServer = new Server("0.0.0.0", port, "/ws",null, ChatServer.class);

        try {
            httpServer.start();
            wsServer.start();

            System.out.println("HTTP en http://localhost:" + port);
            System.out.println("WS   en ws://localhost:" + port + "/ws/chat");

            Thread.currentThread().join();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wsServer.stop();
            httpServer.shutdownNow();
        }
    }
}

