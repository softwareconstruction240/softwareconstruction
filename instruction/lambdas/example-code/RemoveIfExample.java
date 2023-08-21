import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoveIfExample {
    public static void main(String[] args) {
        List<Integer> intList = new ArrayList<>();
        intList.addAll(Arrays.asList(23, 5, 10, 71, 100, 1203, 4, 7, 748));

        intList.removeIf(x -> x >= 100);

        for(Integer value : intList) {
            System.out.print(value + " ");
        }

    }
}
