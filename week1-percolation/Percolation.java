import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final byte OPENED = 1 << 0;
    private static final byte BOTTOM = 1 << 1;
    private static final byte TOP = 1 << 2;
    private static final byte PERCOLATED_MASK = (byte) (OPENED | TOP | BOTTOM);
    private static final byte FULL_MASK = (byte) (OPENED | TOP);

    private final WeightedQuickUnionUF grid;
    private final byte[] openGrid;
    private int openSitesCount = 0;
    private final int n;
    private final int nSquared;
    private boolean isPercolated = false;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        this.n = n;
        nSquared = n * n;

        grid = new WeightedQuickUnionUF(nSquared);
        openGrid = new byte[nSquared]; // The same
    }

    // opens the site (row, col) if it is not open already
    public void open(int row1, int col1) {
        if (isOpen(row1, col1)) {
            return;
        }

        final int row = row1 - 1;
        final int col = col1 - 1;
        final int flatIndex = toFlatIndex(row, col);

        openSitesCount++;
        openGrid[flatIndex] |= OPENED;

        if (row == n - 1) { // The last row - mark this cell as BOTTOM
            openGrid[flatIndex] |= BOTTOM;
        }

        if (row == 0) { // First row - connect with virtual top and mark as TOP
            openGrid[flatIndex] |= TOP;
            if (n == 1) {
                isPercolated = true;
            }
        }

        tryToUnite(flatIndex, row - 1, col); // North
        tryToUnite(flatIndex, row, col + 1); // East
        tryToUnite(flatIndex, row + 1, col); // South
        tryToUnite(flatIndex, row, col - 1); // West
    }

    // is the site (row, col) open?
    public boolean isOpen(int row1, int col1) {
        final int row = row1 - 1;
        final int col = col1 - 1;
        checkCell(row, col);
        return isOpen(toFlatIndex(row, col));
    }

    // is the site (row, col) full?
    public boolean isFull(int row1, int col1) {
        final int row = row1 - 1;
        final int col = col1 - 1;
        checkCell(row, col);
        final int flatIndex = toFlatIndex(row, col);
        return (openGrid[grid.find(flatIndex)] & FULL_MASK) == FULL_MASK;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSitesCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return isPercolated;
    }

    private void checkCell(int row, int col) {
        if (!isValidCell(row, col)) {
            throw new IllegalArgumentException();
        }
    }

    private int toFlatIndex(int row, int col) {
        return row * n + col;
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && col >= 0 && row < n && col < n;
    }

    private boolean isOpen(int flatIndex) {
        return (openGrid[flatIndex] & OPENED) != 0;
    }

    private void tryToUnite(int sourceFlatIndex, int row, int col) {
        if (!isValidCell(row, col)) {
            return;
        }

        int flatIndex = toFlatIndex(row, col);
        if (!isOpen(flatIndex)) {
            return;
        }

        final int sourceRoot = grid.find(sourceFlatIndex);
        final int root = grid.find(flatIndex);

        grid.union(sourceFlatIndex, flatIndex);

        // Copy properties of old roots to the new root
        final int newRoot = grid.find(sourceFlatIndex);
        openGrid[newRoot] |= (byte) (openGrid[sourceRoot] | openGrid[root]);
        if ((openGrid[newRoot] & PERCOLATED_MASK) == PERCOLATED_MASK) {
            isPercolated = true;
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        StdOut.println("Hello");
    }
}
