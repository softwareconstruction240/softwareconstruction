package demo;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;


public class FileRaceConditionExample {

    public static void main(String[] args) throws InterruptedException {

        final File FILE_PATH = new File("count");
        final int ITERATIONS = 100;

        try (var output = new PrintWriter(FILE_PATH)) {
            output.print(0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ExecutorService threadPool = Executors.newCachedThreadPool();

        for (int i = 0; i < 2; ++i) {
            threadPool.submit(new FileCounter(i, FILE_PATH, ITERATIONS));
        }

        threadPool.shutdown();

        threadPool.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("Leaving Main Thread");
    }
}


class FileCounter implements Runnable {

    private static Object lockObject = new Object();

    private int _threadNumber;
    private File _filePath;
    private int _iterations;

    public FileCounter(int threadNumber, File filePath, int iterations) {
        _threadNumber = threadNumber;
        _filePath = filePath;
        _iterations = iterations;
    }

    public void run() {
        for (int i = 0; i < _iterations; ++i) {
            try {
                incrementCount();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void incrementCount() throws IOException {
        synchronized (lockObject) {
            int count;

            try (var input = new Scanner(_filePath)) {
                count = input.nextInt();
            }

            ++count;

            try (var output = new PrintWriter(_filePath)) {
                output.print(count);
            }

            System.out.println("Thread_" + _threadNumber + ": " + count);
        }
    }

}