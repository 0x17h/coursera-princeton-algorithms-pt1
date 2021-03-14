import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    // find a solution to the initial board (using the A* algorithm)
    private final Queue<Board> solution;

    private class BoardSolver {
        private final MinPQ<BoardNode> pq;
        private boolean isSolved = false;
        private final Stack<Board> solution;

        private class BoardNode implements Comparable<BoardNode> {
            public final BoardNode prev;
            public final Board board;
            public final int move;
            public final int priority;

            BoardNode(Board board, BoardNode prev) {
                this.board = board;
                this.prev = prev;
                this.move = prev != null ? prev.move + 1 : 0;
                this.priority = move + board.manhattan();
            }

            public int priority() {
                return priority;
            }

            public void print() {
                StdOut.println(board.toString());
                StdOut.println("priority = " + priority());
                StdOut.println("move = " + move);
            }

            public int compareTo(BoardNode rhs) {
                final int dp = priority() - rhs.priority();
                return dp < 0 ? -1 : dp == 0 ? 0 : 1;
            }

            public boolean existed(Board b) {
                return prev != null && prev.board.equals(b);
            }
        }

        public BoardSolver(Board board) {
            solution = new Stack<Board>();
            pq = new MinPQ<BoardNode>();
            pq.insert(new BoardNode(board, null));
        }

        public boolean isSolved() {
            return isSolved;
        }

        public void makeStep() {
            if (isSolved) {
                return;
            }

            if (pq.size() == 0) {
                isSolved = true;
                return;
            }

            BoardNode min = pq.delMin();
            if (min.board.isGoal()) {
                isSolved = true;
                while (min != null) {
                    solution.push(min.board);
                    min = min.prev;
                }
                return;
            }

            for (Board n : min.board.neighbors()) {
                if (!min.existed(n)) {
                    pq.insert(new BoardNode(n, min));
                }
            }
        }

        public Iterable<Board> solution() {
            return solution;
        }
    }

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        BoardSolver initialSolver = new BoardSolver(initial);
        BoardSolver twinSolver = new BoardSolver(initial.twin());

        while (!initialSolver.isSolved() && !twinSolver.isSolved()) {
            initialSolver.makeStep();
            twinSolver.makeStep();
        }

        solution = new Queue<Board>();
        for (Board s : initialSolver.solution()) {
            solution.enqueue(s);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return moves() != -1;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solution.size() - 1;
    }


    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return isSolvable() ? solution : null;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("Unsolvable puzzle");
        }
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
            StdOut.println("Solved in " + solver.moves() + " steps");
        }
    }

}
