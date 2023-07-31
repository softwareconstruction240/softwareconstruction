public class PointAndRectangleUser {

    public static void main(String[] args) {

        Point topLeft = new Point(10, 20);
        System.out.println("Top-Left: " + topLeft);

        Point bottomRight = new Point(75, 150);
        System.out.println("Bottom-Right: " + bottomRight);

        Rectangle rect = new Rectangle(topLeft, bottomRight);
        System.out.println("Rectangle: " + rect);
    }
}
