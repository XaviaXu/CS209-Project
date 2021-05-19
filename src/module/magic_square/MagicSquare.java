package module.magic_square;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * The type Magic square.
 */
public class MagicSquare {
    public int n;
    public int[][] board;
    public int[][] cnst;
    public int mn;
    public ArrayList<Mutate> mutates;

    public double t;
    public int min_err;
    public int[][] min_brd;

    public static final double EPS = 1e-13;
    public static final double DELT = 0.9999;
    public static final int T = (int) 5e8;

    /**
     * Instantiates a new Magic square. Add mutate methods to List
     *
     * @param n the board size
     */
    public MagicSquare(int n) {
        this.n = n;
        this.cnst = new int[n][n];
        this.board = new int[n][n];
        mn = n * (n * n + 1) / 2;
        mutates = new ArrayList<>();

        for (int i = 0; i < 1; ++i)
            mutates.add(new MutateL1());
        for (int i = 0; i < 1; ++i)
            mutates.add(new MutateL2());
        for (int i = 0; i < 3; ++i)
            mutates.add(new MutateS1());
    }

    /**
     * Read magic square from file
     *
     * @param fn the filename
     * @throws IOException the io exception
     */
    public void readMs(String fn) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(fn);

        Scanner fileScanner = new Scanner(fileInputStream);

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                cnst[i][j] = fileScanner.nextInt();
            }
        }
    }

    /**
     * @return board copy
     */
    public int[][] boardCopy() {
        int[][] nxt = new int[n][];
        for (int i = 0; i < n; ++i) {
            nxt[i] = board[i].clone();
        }
        return nxt;
    }

    /**
     * @return board copy
     */
    public int[][] boardCopy(int[][] b) {
        int[][] nxt = new int[n][];
        for (int i = 0; i < n; ++i) {
            nxt[i] = b[i].clone();
        }
        return nxt;
    }

    /**
     * Fill the magic square to satisfied constraints.
     */
    public void fill() {
        boolean[] vis = new boolean[n * n + 1];
        for (int[] row : cnst) {
            for (int x : row) {
                vis[x] = true;
            }
        }

        int cnt = 1;
        while (cnt < n * n + 1 && vis[cnt])
            ++cnt;

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (cnst[i][j] == 0) {
                    board[i][j] = cnt++;
                    while (cnt < n * n + 1 && vis[cnt])
                        ++cnt;
                } else {
                    board[i][j] = cnst[i][j];
                }
            }
        }
    }

    /**
     * Print magic square
     */
    public void show() {
        for (int[] row : board) {
            for (int x : row) {
                System.out.printf("%6d", x);
            }
            System.out.println();
        }
    }

    /**
     * Evaluate the error of rows and columns (Step 1 of the shorter paper)
     *
     * @param b the board
     * @return the error
     */
    public int evl1(int[][] b) {
        int error = 0, sum;

        for (int i = 0; i < n; ++i) {
            sum = 0;
            for (int j = 0; j < n; ++j) {
                sum += b[i][j];
            }
            error += Math.abs(sum - mn);

            sum = 0;
            for (int j = 0; j < n; ++j) {
                sum += b[j][i];
            }
            error += Math.abs(sum - mn);
        }

        return error;
    }

    /**
     * Evaluate the error of two diagonals (Step 2 of the shorter paper)
     *
     * @param b the board
     * @return the error
     */
    public int evl2(int[][] b) {
        int error = 0, sum;

        sum = 0;
        for (int i = 0; i < n; ++i) {
            sum += b[i][i];
        }
        error += Math.abs(sum - mn);

        sum = 0;
        for (int i = 0; i < n; ++i) {
            sum += b[i][n - i - 1];
        }
        error += Math.abs(sum - mn);

        return error;
    }

    /**
     * Evaluate the error of magic square (for the longer paper)
     *
     * @param b the board
     * @return the error
     */
    public int evl(int[][] b) {
        return evl1(b) + evl2(b);
    }

    public void algoInit() {
        t = T;
        board = boardCopy(min_brd);
    }

    /**
     * Mutate the magic square
     *
     * @return the mutated square
     */
    public int[][] getNext() {
        Random random = new Random();
        int chs = random.nextInt(mutates.size());

        return mutates.get(chs).mutate(this);
    }

    public boolean getNextGeneration() {
        int[][] nxt = getNext();
        int nxt_err = evl(nxt), cur_err = evl(board);

        if (nxt_err <= cur_err) {
            if (nxt_err < min_err) {
                min_err = nxt_err;
                min_brd = boardCopy(nxt);
            }
            board = nxt;
            return true;
        }

        if (Math.exp((cur_err - nxt_err) / t) >= Math.random()) {
            board = nxt;
//            System.out.print("true");
            return true;
        }

        return false;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {

        MagicSquare ms = new MagicSquare(10);

        ms.readMs("res/10.in");
        ms.fill();

        ms.min_err = ms.evl(ms.board);
        ms.min_brd = ms.boardCopy();

        long t1 = System.currentTimeMillis();

        int gen = 0;
        long cnt = 0;
        do {
            ms.algoInit();
//            cnt = 0;
            while (ms.t >= EPS && ms.min_err > 0) {
                boolean ret = ms.getNextGeneration();
                ms.t *= DELT;
                ++cnt;
            }

            ++gen;
            if (gen % 2 == 0) {
                System.out.println(ms.evl(ms.board) + "==========");
                System.out.println("round " + gen + ": " + ms.min_err);
            }
        } while (ms.min_err > 0 && gen <= 20);

        long t2 = System.currentTimeMillis();

        ms.board = ms.min_brd;
        System.out.printf("time: %dms, gen: %d, cnt: %d\n" +
                "evl: %d, evl1: %d, evl2: %d\n", (t2 - t1), gen, cnt, ms.evl(ms.board), ms.evl1(ms.board), ms.evl2(ms.board));
        ms.show();
    }
}
