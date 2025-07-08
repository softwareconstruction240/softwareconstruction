import io.javalin.Javalin;

public class WsEchoServer {
    public static void main(String[] args) {
        Javalin javalinServer = Javalin.create();
        createHandlers(javalinServer);
        javalinServer.start(8080);
    }

    private static void createHandlers(Javalin javalinServer) {

        javalinServer.get("/echo/{msg}", new HttpEchoRequestHandler());

        WsRequestHandler wsHandler = new WsRequestHandler();

        javalinServer.ws("/ws", ws -> {
            ws.onConnect(wsHandler);
            ws.onClose(wsHandler);
            ws.onMessage(wsHandler);
        });
    }
}
