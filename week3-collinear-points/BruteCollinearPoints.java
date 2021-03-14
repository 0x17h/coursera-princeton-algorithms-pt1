/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private final LineSegment[] lineSegments;

    public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points
        if (points == null) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < points.length; ++i) {
            final Point p1 = checkPoint(points[i]);
            for (int i2 = i + 1; i2 < points.length; ++i2) {
                final Point p2 = checkPoint(points[i2]);
                if (p1.compareTo(p2) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }

        Queue<LineSegment> qLineSegments = new Queue<LineSegment>();

        for (int i1 = 0; i1 < points.length; ++i1) {
            final Point p1 = points[i1];

            for (int i2 = i1 + 1; i2 < points.length; ++i2) {
                final Point p2 = points[i2];

                for (int i3 = i2 + 1; i3 < points.length; ++i3) {
                    final Point p3 = points[i3];

                    for (int i4 = i3 + 1; i4 < points.length; ++i4) {
                        final Point p4 = points[i4];

                        double s12 = p1.slopeTo(p2);
                        double s23 = p2.slopeTo(p3);
                        double s34 = p3.slopeTo(p4);

                        if (s12 == s23 && s23 == s34) {
                            Point[] ps = { p1, p2, p3, p4 };
                            Arrays.sort(ps);
                            qLineSegments.enqueue(new LineSegment(ps[0], ps[3]));
                        }
                    }
                }
            }
        }

        lineSegments = new LineSegment[qLineSegments.size()];
        int counter = 0;
        for (LineSegment s : qLineSegments) {
            lineSegments[counter++] = s;
        }
    }

    private Point checkPoint(Point p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return p;
    }

    public int numberOfSegments() {       // the number of line segments
        return lineSegments.length;
    }

    public LineSegment[] segments() {               // the line segments
        return lineSegments.clone();
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdOut.println("Segments count: " + collinear.numberOfSegments());
        StdDraw.show();
    }
}
