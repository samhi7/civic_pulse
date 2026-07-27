class Rectangle {
    int length;
    int breadth;

    int area() {
        return length * breadth;
    }
}

public class Rect {
    public static void main(String[] args) {
        Rectangle r = new Rectangle();

        r.length = 10;
        r.breadth = 5;

        System.out.println("Area = " + r.area());
    }
}