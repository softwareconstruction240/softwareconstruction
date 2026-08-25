package nestedClassExample.anonymousInnerClass;

import nestedClassExample.Iterator;

public class DataStructure {

    private int[] arrayOfInts;

    public static void main(String s[]) {

        // Create an instance and fill it with with integer values
        DataStructure ds = new DataStructure(10);

        // Iterate through the data structure and print it's values
        Iterator iterator = ds.iterator(2);
        while (iterator.hasNext()) {
            System.out.println(iterator.getNext() + " ");
        }
    }

    public DataStructure(int size) {

        arrayOfInts = new int[size];

        // Fill the array with ascending integer values
        for (int i = 0; i < size; i++) {
            arrayOfInts[i] = i;
        }
    }

    public Iterator iterator(int increment) {
        // An anonymous inner class that implements the Iterator pattern.
        return new Iterator() {
            // Start stepping through the array from the beginning
            private int next = 0;

            @Override
            public boolean hasNext() {
                // Check if a current element is the last in the array
                return (next <= arrayOfInts.length - 1);
            }

            @Override
            public int getNext() {
                // Get the value to be returned
                int retValue = arrayOfInts[next];

                // Increment the counter in preparation for the next call
                next += increment;

                return retValue;
            }
        };
    }
}