import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Clock {
    private static final int MIN_BALLS = 27;
    private static final int MAX_BALLS = 127;

    private static final int MINUTE_QUEUE_MAX = 4;
    private static final int FIVE_MINUTE_QUEUE_MAX = 11;
    private static final int HOUR_QUEUE_MAX = 11;

    private final Deque<Ball> minutes = new LinkedList<>();
    private final Deque<Ball> fiveMinutes = new LinkedList<>();
    private final Deque<Ball> hours = new LinkedList<>();
    private List<Ball> queue;

    private int twelveHourCounter = 0;

    public static void main(String [] args) {
        try {
            long startMillis = System.currentTimeMillis();
            Clock clock = new Clock();

            String outputString;
            switch(args.length) {
                case 1:
                    outputString = clock.run(Integer.parseInt(args[0]), 0);
                    break;
                case 2:
                    outputString = clock.run(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid number of arguments: " + args.length);
            }

            System.out.println(outputString);
            long endMillis = System.currentTimeMillis();
            System.out.println("Completed in " + (endMillis - startMillis) + " milliseconds (" + ((endMillis - startMillis) / 1000.0) + " seconds)");
        } catch (IllegalArgumentException ex) {
            printUsage(ex.getMessage());
        }
    }

    private static void printUsage(String message) {
        System.out.println(message);
        System.out.println();
        System.out.println("Usage: java Clock numbBalls <numbMinutes>");
    }

    String run(int numbBalls, int numbMinutes) {
        validateParams(numbBalls, numbMinutes);
        initializeQueue(numbBalls);

        if(numbMinutes > 0) {
            for(int i = 0; i < numbMinutes; i++) {
                tick();
            }

            return getClockState();
        } else {
            do {
                tick();
            } while(!clockInInitialState(numbBalls));

            return getOutputString(numbBalls, twelveHourCounter);
        }
    }

    private void validateParams(int numbBalls, int numbMinutes) {
        if(numbBalls < MIN_BALLS || numbBalls > MAX_BALLS) {
            throw new IllegalArgumentException("Invalid number of balls (" + numbBalls + "). Must be between " +
                    MIN_BALLS + " and " + MAX_BALLS + ".");
        }

        if(numbMinutes < 0) {
            throw new IllegalArgumentException("Number of minutes may not be negative (" + numbMinutes + ")");
        }
    }

    private void initializeQueue(int numbBalls) {
        queue = new ArrayList<>(numbBalls);
        for(int i = 0; i < numbBalls; i++) {
            queue.add(new Ball(i + 1));
        }
    }

    private void tick() {
        Ball currentBall = queue.remove(0);
        if(minutes.size() < MINUTE_QUEUE_MAX) {
            minutes.add(currentBall);
        } else {
            dumpMinuteQueue(currentBall);
        }
    }

    private void dumpMinuteQueue(Ball currentBall) {
        dumpQueue(minutes);

        if(fiveMinutes.size() < FIVE_MINUTE_QUEUE_MAX) {
            fiveMinutes.add(currentBall);
        } else {
            dumpFiveMinuteQueue(currentBall);
        }
     }

    private void dumpQueue(Deque<Ball> source) {
        while(source.size() > 0) {
            queue.add(source.removeLast());
        }
    }

    private void dumpFiveMinuteQueue(Ball currentBall) {
        dumpQueue(fiveMinutes);

        if(hours.size() < HOUR_QUEUE_MAX) {
            hours.add(currentBall);
        } else {
            dumpHourQueue(currentBall);
        }
    }

    private void dumpHourQueue(Ball currentBall) {
        dumpQueue(hours);
        queue.add(currentBall);
        twelveHourCounter++;
    }

    private boolean clockInInitialState(int numbBalls) {
        boolean returnValue = true;

        if(queue.size() != numbBalls) {
            returnValue = false;
        } else {
             for(int i = 0; i < numbBalls; i++) {
                 if(!(queue.get(i).getId() == i + 1)) {
                     returnValue = false;
                     break;
                 }
             }
        }

        return returnValue;
    }

    private String getOutputString(int numbBalls, int twelveHourCounter) {
        return numbBalls + " balls cycle after " + (twelveHourCounter / 2) + " days.";
    }

    private String getClockState() {
        return "{\"Min\":[" + getQueueState(minutes) + "],\"FiveMin\":[" + getQueueState(fiveMinutes) + "],\"Hour\":[" +
                getQueueState(hours) + "],\"Main\"[" + getQueueState(queue) + "]}";
    }

    private String getQueueState(Iterable<Ball> iterable) {
        StringBuilder builder = new StringBuilder();

        for(Ball ball : iterable) {
            if(builder.length() > 0) {
                builder.append(',');
            }

            builder.append(ball.getId());
        }

        return builder.toString();
    }
}
