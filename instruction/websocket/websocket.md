# WebSocket

🖥️ [Slides](https://docs.google.com/presentation/d/1isIXUjbcOjWPD4ZXz5j98t4xns8ImKj9/edit?usp=sharing&ouid=110961336761942794636&rtpof=true&sd=true)

🖥️ [Lecture Videos](#videos)

![webSocket](webServicesWebSocketsLogo.png)

HTTP is based on a client-server architecture. A client always initiates the request and the server responds. This is great if you are building a global document library connected by hyperlinks, but for many other use cases it just doesn't work. Applications for notifications, distributed task processing, peer-to-peer communication, or asynchronous events need communication that is initiated by two or more connected devices.

For years, web developers created hacks to work around the limitation of the client/server model. This included solutions like having the client frequently pinging the server to see if the server had anything to say, or keeping client-initiated connections open for a very long time as the client waited for some event to happen on the server. Needless to say, none of these solutions were elegant or efficient.

Finally, in 2011 the communication protocol WebSocket was created to solve this problem. The core feature of WebSocket is that it is fully duplexed. This means that after the initial connection is made from a client, using vanilla HTTP, and then upgraded by the server to a WebSocket connection, the relationship changes to a peer-to-peer connection where either party can efficiently send data at any time.

![WebSocket Upgrade](webServicesWebSocketUpgrade.jpg)

WebSocket connections are still only between two parties. So if you want to facilitate a conversation between a group of users, the server must act as the intermediary. Each peer first connects to the server, and then the server forwards messages amongst the peers.

![WebSocket Peers](webServicesWebSocketPeers.jpg)

## Creating a WebSocket Server Connection

Here is an example of a basic HTTP server that uses the `Javalin` library to support upgrading to the WebSocket protocol when the `/ws` endpoint is called by a client.

```java
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
                    ws.onClose(_ -> System.out.println("Websocket closed"));
                })
                .start(8080);
    }
}
```

This code calls Javalin.create() to create an HTTP server, and then uses a fluent API to chain calls to get, which registers code for handling an HTTP GET request; ws, which registers code for handling WebSocket connections, messages, and closures coming from a peer; and start, which starts the server on the specified port.

## Websocket Timeout

By default, if Javalin has not heard from it's client for 30 seconds, the connection is deemed closed. Once the connection is closed, no messages can be sent. If you want to send something again, you have to open a new connection.

Rather than open a new connection every 30 seconds, we can have Javalin ping it's connections. If a pong is recieved back, then we know the connection is still open. Javalin can be instructed to do this with every connection by calling `context.enableAutomaticPings()`. You'll notice in the above example that this is enabled when the connection is opened.

## Creating a WebSocket Client Connection

In order to initiate a WebSocket connection to a server from a client in Java you need a library that implements the `javax.websocket.WebSocketContainer` interface. In this course we use the `glassfish.tyrus` library to implement `WebSocketContainer`.

> Install: org.glassfish.tyrus.bundles:tyrus-standalone-client:2.1.4

Then you need to implement the `onOpen` method on the `javax.websocket.Endpoint` abstract class in order to create the class that will handle sending and receiving WebSocket messages.

Now you are ready to create your connection by calling the `connectToServer` method on the container and providing a reference to an object for the class that extends the `Endpoint` class. This will return a `Session` object that you can use to send messages over your WebSocket connection.

You receive messages by registering an `onMessage` listener with the session's `addMessageHandler`.

The following code gives you a full example.

```java
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

public class WsEchoClient extends Endpoint {
    public Session session;

    public static void main(String[] args) throws Exception {
        WsEchoClient client = new WsEchoClient();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a message you want to echo:");
        while(true) {
            client.send(scanner.nextLine());
        }
    }

    public WsEchoClient() throws Exception {
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
                System.out.println("\nEnter another message you want to echo:");
            }
        });
    }

    public void send(String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }

    // This method must be overridden, but we don't have to do anything with it
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}

```

## Demonstration code

📁 [WebSocket Client Examples](example-code/client)

📁 [WebSocket Server Examples](example-code/server)

## Videos

- 🎥 [WebSocket Introduction (10:45)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=fcd3b045-d06e-41f7-8a0e-b1a1014a7b97) - [[transcript]](https://github.com/user-attachments/files/17753884/CS_240_WebSocket_Introduction_Transcript.pdf)
- 🎥 [WebSocket Protocol (7:06)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=91c23bc0-410b-4848-be81-b1a1014dab96) - [[transcript]](https://github.com/user-attachments/files/17753890/CS_240_WebSocket_Protocol_Transcript.pdf)
- 🎥 [Simple WebSocket Example (12:05)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=27a9cc42-fd4a-42ae-b364-b2c80108d66a) - [transcript]
- 🎥 [Pet Shop WebSocket Example (21:21)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=d16b30ba-6d13-4552-84c6-b2c8010c696f) - [transcript]
- 🎥 [Chess WebSocket (18:03)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=aa2f2bd3-cbad-49ca-a0e1-b2cd014e6f2c) - [transcript]
