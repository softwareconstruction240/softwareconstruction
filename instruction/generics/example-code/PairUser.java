import java.util.Date;

public class PairUser {
    public static void main(String[] args) {
        Pair<String, Integer> pair1 = new Pair<String, Integer>("Hello", 123);
        Pair<String, Integer> pair2 = new Pair<>("Hello", 123);
        StringPair pair3 = new StringPair("Hello", "BYU");
        KeyValuePair<Date> pair4 = new KeyValuePair<>("Hello", new Date());
    }
}
