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

public class FastCollinearPoints {
    private final LineSegment[] lineSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] originalPoints) {
        if (originalPoints == null) {
            throw new IllegalArgumentException("Provided array of points is null");
        }

        for (Point p : originalPoints) {
            if (p == null) {
                throw new IllegalArgumentException("One of provided points is null");
            }
        }

        final Point[] points = new Point[originalPoints.length - 1];
        Queue<LineSegment> qLineSegments = new Queue<LineSegment>();

        for (int i = 0; i < originalPoints.length; ++i) {
            final Point pivot = originalPoints[i];
            // StdOut.println("Pivot point #" + i + ": " + pivot);

            int insertIndex = 0;
            for (int j = 0; j < originalPoints.length; ++j) {
                if (i != j) {
                    points[insertIndex++] = originalPoints[j];
                }
            }

            Arrays.sort(points, pivot.slopeOrder());

            double prevSlope = Double.NEGATIVE_INFINITY;
            int counter = 0;
            int iSlope = 0;

            for (; iSlope < points.length; ++iSlope) {
                final Point current = points[iSlope];
                final double currentSlope = pivot.slopeTo(current);
                // StdOut.println("Slope with " + current + String
                //         .format("at position %d: %.12f", iSlope, currentSlope));

                if (prevSlope == currentSlope) {
                    if (currentSlope == Double.NEGATIVE_INFINITY) {
                        throw new IllegalArgumentException(
                                "Duplicate point found for point " + current);
                    }
                    ++counter;
                }
                else {
                    if (counter >= 2) {
                        makeSegment(qLineSegments, iSlope, counter, points, pivot);
                    }
                    counter = 0;
                }

                prevSlope = currentSlope;
            }

            if (counter >= 2) {
                makeSegment(qLineSegments, iSlope, counter, points, pivot);
            }
        }

        lineSegments = new LineSegment[qLineSegments.size()];
        int counter = 0;
        for (LineSegment s : qLineSegments) {
            lineSegments[counter++] = s;
        }
    }

    private void makeSegment(Queue<LineSegment> qLineSegments, int lastPointIndex, int counter,
                             Point[] points,
                             Point pivot) {
        // If our segment has 3 points (excluding pivot),
        // then counter will be 2 => add one
        final int size = counter + 1;
        // lastPointIndex is one past the last point in our segment
        final int startIndex = lastPointIndex - size;

        Point min = pivot;
        Point max = pivot;

        // Let's suppose, we have pivot point C and our other points are A, B and D
        // If we've found out, that all these points is a line segment,
        // then we should figure out its first and last points, since A->B->C->D, C->B->D->A, etc.
        // are all the same line segment
        // We will get all those combinations iterating through different pivots.
        // To not save duplicates, we should figure min and max for this segment (start and finish),
        // because for the same line segments they will be the same no matter how points inside are
        // arranged
        // Suppose our min is A and max is D. It means, that our pivot C not in the beginning of the
        // segment, and we will add (or have already added) this segment when our pivot
        // will be (or have already been) A


        // Run min-max on part of the array with our line segment
        for (int i = startIndex; i < size + startIndex; ++i) {
            final Point p = points[i];
            // Our pivot point is not the min point -> immediate return,
            // there is no sense to look further
            if (min.compareTo(p) > 0) {
                return;
            }
            if (p.compareTo(max) > 0) {
                max = p;
            }
        }

        qLineSegments.enqueue(new LineSegment(min, max));

        // StdOut.println(
        //         "Cut at " + startIndex + " with counter " + counter + " for " + (toSort.length
        //                 - 1) + " points " + (pivot == min ? "made" : "not made") + " :" +
        //                 first + " -> " + last);
    }

    public int numberOfSegments() {        // the number of line segments
        return lineSegments.length;
    }

    public LineSegment[] segments() {                // the line segments
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdOut.println("Segments count: " + collinear.numberOfSegments());
        StdDraw.show();
    }
}
