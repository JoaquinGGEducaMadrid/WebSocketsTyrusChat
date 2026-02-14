package websocketstyruschat;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@ServerEndpoint("/chat")
public class ChatServer {

    private static Set<Session> clients = ConcurrentHashMap.newKeySet();
    private static ConcurrentHashMap<Session, String> nicks = new ConcurrentHashMap<>();
    private static AtomicInteger counter = new AtomicInteger(1);

    @OnOpen
    public void onOpen(Session session) {
        clients.add(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        // Primer mensaje = nick base
        if (!nicks.containsKey(session)) {
            String nickBase = message.trim();
            String nickFinal = nickBase + counter.getAndIncrement();
            nicks.put(session, nickFinal);

            broadcast("SERVER", nickFinal + " se ha conectado");
            sendUserList();
            return;
        }

        // Mensaje normal
        String nick = nicks.get(session);
        broadcast(nick, message);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        String nick = nicks.get(session);
        clients.remove(session);
        nicks.remove(session);

        broadcast("SERVER", nick + " se ha desconectado");
        sendUserList();
    }

    private void broadcast(String sender, String msg) throws IOException {
        String json = "{\"sender\":\"" + sender + "\",\"msg\":\"" + msg + "\"}";
        for (Session s : clients) {
            s.getBasicRemote().sendText(json);
        }
    }

    private void sendUserList() throws IOException {
        String list = nicks.values().stream()
                .collect(Collectors.joining(","));

        String json = "{\"type\":\"users\",\"list\":\"" + list + "\"}";

        for (Session s : clients) {
            s.getBasicRemote().sendText(json);
        }
    }
}

