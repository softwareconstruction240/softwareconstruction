public class Rectangle {

    private Point topLeft;
    private Point bottomRight;

    public Rectangle() {
        topLeft = new Point(0, 0);
        bottomRight = new Point(0, 0);
    }

    public Rectangle(Point tl, Point br) {
        topLeft = tl;
        bottomRight = br;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(Point value) {
        topLeft = value;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(Point value) {
        bottomRight = value;
    }

    public String toString() {
        return String.format("Rectangle[%s, %s]", topLeft, bottomRight);
    }
}
