import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double[] thresholds;
    private final int trialsCount;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        thresholds = new double[trials];
        trialsCount = trials;

        final int nSquared = n * n;

        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int flatIndex = StdRandom.uniform(0, n * n);
                int row = flatIndex / n + 1;
                int column = flatIndex % n + 1;
                p.open(row, column);
            }

            thresholds[i] = (double) p.numberOfOpenSites() / nSquared;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - confidenceThreshold();
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + confidenceThreshold();
    }

    private double confidenceThreshold() {
        return 1.96 * stddev() / Math.sqrt((double) trialsCount);
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException();
        }

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, trials);
        String fmt = "%1$-30s = %2$.8f";
        StdOut.println(String.format(fmt, "mean", stats.mean()));
        StdOut.println(String.format(fmt, "stddev", stats.stddev()));
        StdOut.println(String.format("%1$-30s = [%2$.12f, %3$.12f]", "95% confidence interval",
                                     stats.confidenceLo(), stats.confidenceHi()));
    }
}
