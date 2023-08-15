package demo;


public class JavaThreadExample {
	
	public static void main(String[] args) {
	
		CountingThread countUp = new CountingThread("UP", 0, 50, 1);
		CountingThread countDown = new CountingThread("DOWN", 50, 0, -1);
		
		countUp.start();
		countDown.start();
		
		System.out.println("Leaving Main Thread");
	}
}


class CountingThread extends Thread {
	
	private String _name;
	private int _start;
	private int _stop;
	private int _increment;
	
	public CountingThread(String name, int start, int stop, int increment) {
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


