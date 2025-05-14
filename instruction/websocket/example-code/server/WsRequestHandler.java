import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import org.jetbrains.annotations.NotNull;

public class WsRequestHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    @Override
    public void handleConnect(@NotNull WsConnectContext ctx) {
        ctx.enableAutomaticPings();
        System.out.println("Websocket connected");
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) {
        ctx.send("WebSocket response:" + ctx.message());
    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }
}
