package module.magic_square;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class MagicSquare {
    public int n;
    public int[][] board;
    public int[][] cnst;
    public int mn;
    public ArrayList<Mutate> mutates;
    public ArrayList<Mutate> mutates2;
    public Mutate local1;
    public Mutate local2;

    public double t;
//    public int min_err;
//    public int[][] min_brd;

    public static final double EPS = 1e-8;
    public static final double DELT = 0.95;
    public static final double T = 1e4;
    public static final int S = 500;

    public static final double EPS2 = 1e-16;
    public static final double DELT2 = 0.95;
    public static final double T2 = 1e3;
    public static final int S2 = 1;

    /**
     * Instantiates a new Magic square.
     *
     * @param n the magic square size
     */
    public MagicSquare(int n) {
        this.n = n;
        this.cnst = new int[n][n];
        this.board = new int[n][n];
        mn = n * (n * n + 1) / 2;
        mutates = new ArrayList<>();
        mutates2 = new ArrayList<>();
        local1 = new MutateSL1();
        local2 = new MutateSL2();

        for (int i = 0; i < 4; ++i)
            mutates.add(new MutateS0());
        for (int i = 0; i < 6; ++i)
            mutates.add(new MutateS11());
        for (int i = 0; i < 4; ++i)
            mutates.add(new MutateS12());
        for (int i = 0; i < 2; ++i)
            mutates.add(new MutateS13());
        for (int i = 0; i < 1; ++i)
            mutates.add(new MutateL1());

        for (int i = 0; i < 1; ++i)
            mutates2.add(new MutateS21());
    }


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        MagicSquare ms = new MagicSquare(4);

        ms.readMs("input/4.in");

        int rnd = 30;
        double[] times1 = new double[rnd];
        double[] times2 = new double[rnd];
        double[] tot_times = new double[rnd];

        for (int it = 0; it < rnd; ++it) {
            ms.fill();

            // step 1
            System.out.println("new test: " + it + "\n" +
                    "step1:=======");
            long t1 = System.currentTimeMillis();

            int gen = 0;
            long cnt = 0;
            do {
                if (gen % 2 == 0) {
                    System.out.println("round " + gen + ": " + ms.evl1(ms.board));
//                    ms.fill();
                }

                ms.algoInit();
                while (ms.t >= EPS && ms.evl1(ms.board) > 0) {
                    for (int i = 0; i < S; ++i) {
                        boolean ret = ms.getNextGeneration();
                    }
                    ms.t *= DELT;
                }

                ++gen;
            } while (ms.evl1(ms.board) > 0 && gen <= 20000);

            long t2 = System.currentTimeMillis();
            times1[it] = t2 - t1;

            System.out.printf("time: %dms, gen: %d, cnt: %d\n" +
                    "evl: %d, evl1: %d, evl2: %d\n", t2 - t1, gen, cnt, ms.evl(ms.board), ms.evl1(ms.board), ms.evl2(ms.board));

            // step2
            System.out.println("step 2: ========");
            t1 = System.currentTimeMillis();

            gen = 0;
            cnt = 0;
            do {
                if (gen % 200 == 0) {
                    System.out.println("round " + gen + ": " + ms.evl2(ms.board));
                }

                ms.algoInit2();
                while (ms.t >= EPS2 && ms.evl2(ms.board) > 0) {
                    for (int i = 0; ms.evl2(ms.board) > 0 && i < S2; ++i) {
                        boolean ret = ms.getNextGeneration2();
                    }
                    ms.t *= DELT2;
                }

                ++gen;
            } while (ms.evl2(ms.board) > 0 && gen <= 200000);

            t2 = System.currentTimeMillis();
            times2[it] += t2 - t1;

            System.out.printf("time: %dms, gen: %d, cnt: %d\n" +
                    "evl: %d, evl1: %d, evl2: %d\n", t2 - t1, gen, cnt, ms.evl(ms.board), ms.evl1(ms.board), ms.evl2(ms.board));

            tot_times[it] = times1[it] + times2[it];
        }

        // output result
        long sum;
        double dev;

        sum = (long) (MathUtil.sum(times1) / rnd);
        dev = MathUtil.popStdDev(times1);
        System.out.printf("ti1: avg time: %dms, std dev: %fms\n", sum, dev);

        sum = (long) (MathUtil.sum(times2) / rnd);
        dev = MathUtil.popStdDev(times2);
        System.out.printf("ti2: avg time: %dms, std dev: %fms\n", sum, dev);

        sum = (long) (MathUtil.sum(tot_times) / rnd);
        dev = MathUtil.popStdDev(tot_times);
        System.out.printf("tot: avg time: %dms, std dev: %fms\n", sum, dev);


        ms.show();
    }

    /**
     * Read ms.
     *
     * @param fn the file name
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
     * Board copy int [ ] [ ].
     *
     * @return a copy of ms.board
     */
    public int[][] boardCopy() {
        int[][] nxt = new int[n][];
        for (int i = 0; i < n; ++i) {
            nxt[i] = board[i].clone();
        }
        return nxt;
    }

    /**
     * Board copy int [ ] [ ].
     *
     * @param b the copied board
     * @return the board
     */
    public int[][] boardCopy(int[][] b) {
        int[][] nxt = new int[n][];
        for (int i = 0; i < n; ++i) {
            nxt[i] = b[i].clone();
        }
        return nxt;
    }

    /**
     * Fill a initial board with constraints
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
     * Show the board
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
     * Evl 1 int.
     *
     * @param b the board
     * @return the row and column error
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
     * Evl 2 int.
     *
     * @param b the board
     * @return the error of diagonals
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
     * Evl int.
     *
     * @param b the board
     * @return the error of the board
     */
    public int evl(int[][] b) {
        return evl1(b) + evl2(b);
    }

    /**
     * Init of step 1
     */
    public void algoInit() {
        t = T;
//        board = boardCopy(min_brd);
    }

    /**
     * Init of step 2
     */
    public void algoInit2() {
        t = T2;
//        board = boardCopy(min_brd);
    }

    /**
     * Get next board in step 1, the original board won't change
     *
     * @return a generated board of step 1
     */
    public int[][] getNext() {
        Random random = new Random();
        int chs = random.nextInt(mutates.size());

        return mutates.get(chs).mutate(this);
    }

    /**
     * Get next board in step 2, the original board won't change
     *
     * @return a generated board of step 2
     */
    public int[][] getNext2() {
        Random random = new Random();
        int chs = random.nextInt(mutates2.size());

        return mutates2.get(chs).mutate(this);
    }

    /**
     * Gets next generation in step 1.
     *
     * @return useless
     */
    public boolean getNextGeneration() {
        int[][] nxt = getNext();
        int nxt_err = evl1(nxt), cur_err = evl1(board);

        if (nxt_err <= cur_err || Math.exp((cur_err - nxt_err) / t) >= Math.random()) {
            board = nxt;
//            if (nxt_err < 50 * n && Math.random() < 0.001)
//                board = local1.mutate(this);
            return true;
        }

        return false;
    }

    /**
     * Gets next generation in step 2.
     *
     * @return  useless
     */
    public boolean getNextGeneration2() {
        int[][] nxt = getNext2();
        int nxt_err = evl2(nxt), cur_err = evl2(board);

        if (nxt_err <= cur_err || Math.exp((cur_err - nxt_err) / t) >= Math.random()) {
            board = nxt;
            return true;
        }

        return false;
    }

}
