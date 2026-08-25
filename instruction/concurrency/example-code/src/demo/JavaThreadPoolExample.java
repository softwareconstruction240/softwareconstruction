package demo;

import java.util.concurrent.*;


public class JavaThreadPoolExample {
	
	public static void main(String[] args) throws InterruptedException {
	
		ExecutorService threadPool = Executors.newFixedThreadPool(5);

		for (int i = 0; i < 10; ++i) {
			threadPool.submit(new Counter("UP_" + i, 0, 50, 1));
			threadPool.submit(new Counter("DOWN_" + i, 50, 0, -1));
		}
		
		threadPool.shutdown();
		
		threadPool.awaitTermination(1, TimeUnit.MINUTES);

		System.out.println("Leaving Main Thread");
	}
}


class Counter implements Runnable {
	
	private String _name;
	private int _start;
	private int _stop;
	private int _increment;
	
	public Counter(String name, int start, int stop, int increment) {
		_name = name;
		_start = start;
		_stop = stop;
		_increment = increment;
	}
	
	public void run() {
		for (int i = _start; i != _stop; i += _increment) {		
			System.out.println(_name + ": " + i);
		}
	}
}


