package magicSquare;

import java.io.FileInputStream;
import java.io.IOException;
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

    /**
     * Instantiates a new Magic square. Add mutate methods to List
     *
     * @param n the board size
     */
    MagicSquare(int n) {
        this.n = n;
        this.cnst = new int[n][n];
        this.board = new int[n][n];
        mn = n * (n * n + 1) / 2;
        mutates = new ArrayList<>();

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
    public int evl(int[][] b){
        return evl1(b) + evl2(b);
    }

    /**
     * Mutate the magic square
     *
     * @return the mutated square
     */
    public int[][] getNext(){
        return mutates.get(0).mutate(this);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        MagicSquare ms = new MagicSquare(4);

        ms.readMs("res/1.in");
        ms.fill();
        ms.show();
        System.out.println(ms.evl(ms.board));

        int[][] best = ms.board.clone();
        int min_error = ms.evl(ms.board);

        Random random = new Random();

        for (int gen = 0; gen < 1000000; ++gen) {
            int[][] nxt = ms.getNext();
            int eval = ms.evl(nxt);
            if (eval <= min_error) { // better solution
                min_error = eval;
                best = nxt;
                ms.board = nxt;
            } else if (random.nextDouble() < 0.001){ // worse solution with probability
                ms.board = nxt;
            }
            System.out.printf("gen: %d, error: %d\n", gen, ms.evl(ms.board));
            if(eval == 0){
                break;
            }
        }

        ms.board = best;
        System.out.println(ms.evl(ms.board));
        ms.show();
    }
}
