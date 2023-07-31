public class ArrayExample {
    
    public static void main(String [] args) {
        // Declare an int array
        int[] intArray;

        // Declare a String array
        String[] stringArray;

        // Create arrays
        intArray = new int[10];
        stringArray = new String[10];

        // Initialize arrays
        intArray[0] = 500;
        intArray[7] = 73;
        stringArray[9] = "CS 240 is Awesome!";

        // Declare, create, and initialize arrays
        int [] intArray2 = {2, 7, 36, 543};
        String [] stringArray2 = {"Hello", "String 2", "Another String"};

        // Get array length
        System.out.printf("The length of intArray2 is %d\n", intArray2.length);

        // Access array values
        System.out.printf("The value at stringArray2, position 0 is %s\n", stringArray2[0]);

        // Iterate an array
        System.out.println("\n Iteration example 1 (standard for loop):");
        System.out.print("\tThe values of intArray2 are: ");
        for(int i = 0; i < intArray2.length; i++) {
            if(i > 0) {
                System.out.print(", ");
            }

            System.out.print(intArray2[i]);
        }

        System.out.println("\n\n Iteration example 2 (foreach loop):");
        // Simpler syntax. Use when you don't need to access the loop counter.
        System.out.print("\tThe values of intArray2 are: ");
        for(int value : intArray2) {
            System.out.print(value);
            System.out.print(", ");
        }

        // Clean up the trailing ", " following the last value
        System.out.print("\b\b");


        // Arrays of arrays (i.e. multi-dimensional arrays)
        System.out.println("\n\nTic Tac Toe Board");
        char[][] ticTacToeBoard = new char[3][3];
        ticTacToeBoard[0][2] = 'X';
        ticTacToeBoard[1][1] = 'O';
        ticTacToeBoard[2][2] = 'X';

        for(char[] row : ticTacToeBoard) {
            for(char value : row) {
                System.out.print(value);
            }
            System.out.println();
        }
    }
}
