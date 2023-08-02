import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClockTest {
    private Clock clock;

    @Before
    public void setup() {
        clock = new Clock();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooFewBalls() {
        clock.run(26,0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooManyBalls() {
        clock.run(128,0);
    }

    @Test
    public void test30BallsNoMinutes() {
        Assert.assertEquals(clock.run(30, 0),"30 balls cycle after 15 days.");
    }

    @Test
    public void test45BallsNoMinutes() {
        Assert.assertEquals(clock.run(45, 0), "45 balls cycle after 378 days.");
    }

    @Test
    public void test123BallsNoMinutes() {
        Assert.assertEquals(clock.run(123, 0), "123 balls cycle after 108855 days.");
    }

    @Test
    public void test27BallsOneMinute() {
        Assert.assertEquals(clock.run(27, 1), "{\"Min\":[1],\"FiveMin\":[],\"Hour\":[],\"Main\"[2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27]}");
    }

    @Test
    public void test27Balls4Minutes() {
        Assert.assertEquals(clock.run(27, 4), "{\"Min\":[1,2,3,4],\"FiveMin\":[],\"Hour\":[],\"Main\"[5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27]}");
    }

    @Test
    public void test27Balls5Minutes() {
        Assert.assertEquals(clock.run(27, 5), "{\"Min\":[],\"FiveMin\":[5],\"Hour\":[],\"Main\"[6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,4,3,2,1]}");
    }

    @Test
    public void test27Balls6Minutes() {
        Assert.assertEquals(clock.run(27, 6), "{\"Min\":[6],\"FiveMin\":[5],\"Hour\":[],\"Main\"[7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,4,3,2,1]}");
    }

    @Test
    public void test30Balls325Minutes() {
        Assert.assertEquals(clock.run(30, 325), "{\"Min\":[],\"FiveMin\":[22,13,25,3,7],\"Hour\":[6,12,17,4,15],\"Main\"[11,5,26,18,2,30,19,8,24,10,29,20,16,21,28,1,23,14,27,9]}");
    }
}
