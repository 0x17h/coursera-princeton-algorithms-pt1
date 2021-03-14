import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {
    private final int[][] board;
    private final int n;
    private final int blankRow;
    private final int blankColumn;
    private final int manhattan;
    private final int hamming;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles[0].length;
        board = new int[n][n];

        int blankIndex = 0;
        int hammingTmp = 0;
        int manhattanTmp = 0;

        for (int iRow = 0; iRow < n; ++iRow) {
            for (int iColumn = 0; iColumn < n; ++iColumn) {
                final int tile = tiles[iRow][iColumn];
                final int flatIndex = toFlatIndex(iRow, iColumn);
                final int targetTileIndex = tile - 1;
                board[iRow][iColumn] = tile;

                if (tile == 0) {
                    blankIndex = flatIndex;
                }
                else if (targetTileIndex != flatIndex) {
                    hammingTmp++;
                    final int tileRow = targetTileIndex / n;
                    final int tileColumn = targetTileIndex % n;
                    manhattanTmp += Math.abs(iRow - tileRow) + Math.abs(iColumn - tileColumn);
                }
            }
        }

        this.blankRow = blankIndex / n;
        this.blankColumn = blankIndex % n;
        this.manhattan = manhattanTmp;
        this.hamming = hammingTmp;
    }

    // string representation of this board
    public String toString() {
        StringBuilder b = new StringBuilder(n * n * 5);
        b.append(n);
        b.append("\n");

        for (int iRow = 0; iRow < n; ++iRow) {
            for (int iColumn = 0; iColumn < n; ++iColumn) {
                final int tile = board[iRow][iColumn];
                b.append(String.format("%2d ", tile));
            }
            b.append("\n");
        }

        // b.append("manhattan = " + manhattan + "\n");
        // b.append("hamming = " + hamming + "\n");
        // b.append("blankIndex = " + blankIndex() + "\n");

        return b.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }

        Board rhs = (Board) y;
        return Arrays.deepEquals(board, rhs.board);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> ns = new Stack<>();
        tryPushNeighbor(ns, blankRow - 1, blankColumn); // North
        tryPushNeighbor(ns, blankRow, blankColumn + 1); // West
        tryPushNeighbor(ns, blankRow + 1, blankColumn); // South
        tryPushNeighbor(ns, blankRow, blankColumn - 1); // East
        return ns;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int row1 = (blankRow + 1) % n;
        int col1 = blankColumn;
        int row2 = blankRow;
        int col2 = (blankColumn + 1) % n;

        exch(row1, col1, row2, col2);
        Board twin = new Board(board);
        exch(row1, col1, row2, col2);

        return twin;
    }

    private void tryPushNeighbor(Stack<Board> neighbors, int i, int j) {
        if (0 <= i && i < n && 0 <= j && j < n) {
            exchWithBlank(i, j);
            neighbors.push(new Board(board));
            exchWithBlank(i, j);
        }
    }

    private void exchWithBlank(int row, int col) {
        exch(blankRow, blankColumn, row, col);
    }

    private void exch(int row1, int col1, int row2, int col2) {
        final int t = board[row1][col1];
        board[row1][col1] = board[row2][col2];
        board[row2][col2] = t;
    }

    private int toFlatIndex(int row, int col) {
        return row * n + col;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        StdOut.println(initial);
        for (Board b : initial.neighbors()) {
            StdOut.println(b);
        }

        StdOut.println("Twin:\n" + initial.twin());
    }
}

