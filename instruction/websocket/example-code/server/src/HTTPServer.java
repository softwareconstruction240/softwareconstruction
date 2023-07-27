import spark.Spark;

public class HTTPServer {
    public static void main(String[] args) {
        Spark.port(8080);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
    }
}



