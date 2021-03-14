import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private class Node {
        private final boolean isVertical;
        private final Point2D point;

        private Node left;
        private Node right;
        private final RectHV bounding;

        public Node(Point2D point, boolean isVertical, RectHV bounding) {
            this.point = point;
            this.isVertical = isVertical;
            this.bounding = bounding;

            // StdOut.println(
            //         String.format("New node (%.6f, %.6f): %.6f, %.6f : %.6f, %.6f", point.x(),
            //                       point.y(),
            //                       bounding.xmin(), bounding.ymin(), bounding.xmax(),
            //                       bounding.ymax()));
        }

        public int compareTo(Point2D that) {
            final int v = isVertical
                          ? Double.compare(point.x(), that.x())
                          : Double.compare(point.y(), that.y());
            return v == 0
                   ? isVertical
                     ? Double.compare(point.y(), that.y())
                     : Double.compare(point.x(), that.x())
                   : v;
        }

        public boolean add(Point2D p) {
            final int v = compareTo(p);
            if (v == 0) {
                return false;
            }

            if (v > 0) { // this.p is greater than @p
                if (left != null) {
                    return left.add(p);
                }
                left = new Node(p, !isVertical, split(true));
            }
            else {
                if (right != null) {
                    return right.add(p);
                }
                right = new Node(p, !isVertical, split(false));
            }
            return true;
        }

        public boolean contains(Point2D p) {
            final int v = compareTo(p);
            return v == 0 || (v > 0 ? contains(left, p) : contains(right, p));
        }

        private boolean contains(Node n, Point2D p) {
            return n != null && n.contains(p);
        }

        public void range(RectHV query, Queue<Point2D> points) {
            if (!bounding.intersects(query)) {
                return;
            }

            if (query.contains(point)) {
                points.enqueue(point);
            }

            range(left, query, points);
            range(right, query, points);
        }

        private void range(Node n, RectHV query, Queue<Point2D> points) {
            if (n != null) {
                n.range(query, points);
            }
        }

        RectHV split(boolean isLeftSplit) {
            return isVertical
                   ? isLeftSplit
                     ? new RectHV(bounding.xmin(), bounding.ymin(), point.x(), bounding.ymax())
                     : new RectHV(point.x(), bounding.ymin(), bounding.xmax(), bounding.ymax())
                   : isLeftSplit
                     ? new RectHV(bounding.xmin(), bounding.ymin(), bounding.xmax(), point.y())
                     : new RectHV(bounding.xmin(), point.y(), bounding.xmax(), bounding.ymax());
        }

        public void draw() {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            point.draw();

            StdDraw.setPenColor(isVertical ? StdDraw.RED : StdDraw.BLUE);
            StdDraw.setPenRadius();
            if (isVertical) {
                StdDraw.line(point.x(), bounding.ymin(), point.x(), bounding.ymax());
            }
            else {
                StdDraw.line(bounding.xmin(), point.y(), bounding.xmax(), point.y());
            }

            draw(left);
            draw(right);
        }

        private void draw(Node n) {
            if (n != null) {
                n.draw();
            }
        }

        private class MinPoint {
            public Point2D point = null;
            public double distance = Double.POSITIVE_INFINITY;
        }

        public Point2D nearest(Point2D p) {
            MinPoint mp = new MinPoint();
            nearest(p, mp);
            return mp.point;
        }

        private void nearest(Point2D p, MinPoint min) {
            final double distance = point.distanceSquaredTo(p);
            if (distance < min.distance) {
                min.distance = distance;
                min.point = point;
            }

            final int v = compareTo(p);
            Node first = v > 0 ? left : right;
            Node second = v > 0 ? right : left;

            if (first != null) {
                first.nearest(p, min);
            }

            if (second != null && second.bounding.distanceSquaredTo(p) < min.distance) {
                second.nearest(p, min);
            }
        }
    }


    private static final RectHV UNIT_SQUARE = new RectHV(0.0, 0.0, 1.0, 1.0);
    private Node root;
    private int size = 0;

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (root == null) {
            root = new Node(p, true, UNIT_SQUARE); // First split is vertical
            size = 1;
            return;
        }

        if (root.add(p)) {
            ++size;
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return root != null && root.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        if (root != null) {
            root.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        Queue<Point2D> points = new Queue<Point2D>();
        if (root != null) {
            root.range(rect, points);
        }
        return points;
    }


    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return root == null
               ? null
               : root.nearest(p);
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
