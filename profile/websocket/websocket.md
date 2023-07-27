# WebSocket

- [Slides](https://docs.google.com/presentation/d/1jNPLDXYxK7kMvui4WvK0bshU076_OTQ1/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

The WebSocket protocol provides the ability to have bidirectional communication between a client and a server. WebSocket connections are established by upgrading the HTTP protocol.

Here is an example of a basic HTTP server that uses the `JavaSpark` package to support upgrading to the WebSocket protocol when the `/connect` endpoint is called by a client.

```java
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;

@WebSocket
public class WSServer {
    public static void main(String[] args) {
        Spark.port(8080);
        Spark.webSocket("/connect", WSServer.class);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        session.getRemote().sendString("WebSocket response: " + message);
    }
}
```

The key parts of this code include the call to `Spark.webSocket` to register the upgrade endpoint and the `onMessage` function that receives incoming WebSocket message from a peer.

## Demonstration code

- [Simple echo Client and Server](example-code)

This code provides an IntelliJ project that contains modules for both a Client and Server. Examples for both HTTP and WebSocket communication is provided. This allows you to compare and contrast how to use these protocols.
