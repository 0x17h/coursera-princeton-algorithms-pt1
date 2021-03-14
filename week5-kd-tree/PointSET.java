import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private final SET<Point2D> tree;

    public PointSET() {
        tree = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    // number of points in the set
    public int size() {
        return tree.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        tree.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return tree.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : tree) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        Queue<Point2D> result = new Queue<Point2D>();
        for (Point2D point : tree) {
            if (rect.contains(point)) {
                result.enqueue(point);
            }
        }
        return result;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (isEmpty()) {
            return null;
        }

        double minDistance = Double.POSITIVE_INFINITY;
        Point2D nearest = null;
        for (Point2D point : tree) {
            final double d = point.distanceSquaredTo(p);
            if (d < minDistance) {
                minDistance = d;
                nearest = point;
            }
        }

        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
