import io.javalin.Javalin;

public class SimpleWsEchoServer {
    public static void main(String[] args) {
        Javalin.create()
                .get("/echo/{msg}", ctx -> ctx.result("HTTP response: " + ctx.pathParam("msg")))
                .ws("/ws", ws -> {
                    ws.onConnect(ctx -> {
                        ctx.enableAutomaticPings();
                        System.out.println("Websocket connected");
                    });
                    ws.onMessage(ctx -> ctx.send("WebSocket response:" + ctx.message()));
                    ws.onClose(ctx -> System.out.println("Websocket closed"));
                })
                .start(8080);
    }
}
