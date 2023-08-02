package nestedClassExample.innerClass;

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
        return new DataStructureIterator(increment);
    }

    /**
     * An inner class that implements the Iterator pattern. Notice that the inner
     * class has direct access to the outer object's fields, so we do not need to pass
     * them to a constructor.
     */
    private class DataStructureIterator implements Iterator {

        int increment;

        // Start stepping through the array from the beginning
        private int next = 0;

        DataStructureIterator(int increment) {
            this.increment = increment;
        }

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
    }
}