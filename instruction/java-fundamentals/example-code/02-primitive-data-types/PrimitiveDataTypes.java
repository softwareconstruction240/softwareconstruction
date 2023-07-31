public class PrimitiveDataTypes {

    public static void main(String[] args) {

        int anInt = 5;
        byte aByte = -128;
        long long1 = 10;
        long long2 = 10l;
        short aShort = 5;

        System.out.println(anInt + ", " + aByte + ", " + long1 + ", " + long2 + ", " + aShort);
        System.out.printf("%d, %d, %d, %d, %d\n", anInt, aByte, long1, long2, aShort);


        float aFloat = 2.5f;
        double aDouble = 1.0d/3.0d;

        System.out.println(aFloat + ", " + aDouble);
        System.out.printf("%.2f, %.2f\n", aFloat, aDouble);


        char char1 = 'a';
        char char2 = '\u03A3';

        System.out.println(char1 + ", " + char2);
        System.out.printf("%c, %c\n", char1, char2);


        boolean boolean1 = true;
        boolean boolean2 = false;

        System.out.println(boolean1 + ", " + boolean2);
        System.out.printf("%b, %b", boolean1, boolean2);
    }
}
