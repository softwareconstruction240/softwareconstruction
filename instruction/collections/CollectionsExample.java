package fundamentals;

import java.util.ArrayList;
import java.util.HashMap;

public class CollectionsExample {
    public static class MountainList {
        ArrayList mountains = new ArrayList();

        public MountainList() {
            mountains.add("Nebo");
            mountains.add("Timpanogos");
            mountains.add("Lone Peak");
            mountains.add("Cascade");
            mountains.add("Provo");
            mountains.add("Spanish Fork");
            mountains.add("Santaquin");
        }

        public void print() {
            System.out.printf("There are %d mountains%n", mountains.size());
            for (var m : mountains) {
                System.out.println(m);
            }
        }

        public static void main(String[] args) {
            var mountains = new MountainList();
            mountains.print();
        }
    }

    public static class MountainMap {
        HashMap<String, Integer> mountains = new HashMap<>();

        public MountainMap() {
            mountains.put("Nebo", 11928);
            mountains.put("Timpanogos", 11750);
            mountains.put("Lone Peak", 11253);
            mountains.put("Cascade", 10908);
            mountains.put("Provo", 11068);
            mountains.put("Spanish Fork", 10192);
            mountains.put("Santaquin", 10687);
        }

        public void print() {
            System.out.printf("There are %d mountains%n", mountains.size());
            for (var m : mountains.entrySet()) {
                System.out.printf("%s, height: %d%n", m.getKey(), m.getValue());
            }
        }

        public static void main(String[] args) {
            var mountains = new MountainMap();
            mountains.print();
        }
    }

}
