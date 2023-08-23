import spark.Spark;

public class SimpleHelloBYUServer {
    public static void main(String[] args) {
        Spark.get("/hello", (req, res) -> "Hello BYU!");
    }
}
