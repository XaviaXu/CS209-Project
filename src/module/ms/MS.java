package module.ms;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class MS {
    public int n;
    public int mn;
    public int[][] square;
    public int[][] cnst;

    public int evl1;
    public int evl2;
    public int[] rowErr;
    public int[] colErr;
    public int[] diaErr; // 0: \, 1: /
    public boolean[] rowCnst;
    public boolean[] colCnst;


    /**
     * Instantiates a new Magic square.
     *
     * @param n the magic square size
     */
    public MS(int n) {
        this.n = n;
        this.cnst = new int[n][n];
        this.square = new int[n][n];
        this.mn = n * (n * n + 1) / 2;

        rowErr = new int[n];
        colErr = new int[n];
        diaErr = new int[2];

        rowCnst = new boolean[n];
        colCnst = new boolean[n];
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
            rowCnst[i] = false;
            colCnst[i] = false;
        }

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                cnst[i][j] = fileScanner.nextInt();
            }
        }


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
                    square[i][j] = cnt++;
                    while (cnt < n * n + 1 && vis[cnt])
                        ++cnt;
                } else {
                    square[i][j] = cnst[i][j];
                }
            }
        }

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (cnst[i][j] != 0) {
                    rowCnst[i] = true;
                    colCnst[i] = true;
                }
            }
        }
    }

    /**
     * Show the board
     */
    public void show() {
        for (int[] row : square) {
            for (int x : row) {
                System.out.printf("%6d", x);
            }
            System.out.println();
        }
    }

    public void reEvl1() {

        for (int i = 0; i < n; ++i) {
            rowErr[i] = -mn;
            colErr[i] = -mn;
        }

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                rowErr[i] += square[i][j];
                colErr[j] += square[i][j];
            }
        }

        evl1 = 0;
        for (int i = 0; i < n; ++i) {
            evl1 += Math.abs(rowErr[i]) + Math.abs(colErr[i]);
        }
    }

    public void reEvl2() {
        diaErr[0] = diaErr[1] = -mn;

        for (int i = 0; i < n; ++i) {
            diaErr[0] += square[i][i];
            diaErr[1] += square[i][n - 1 - i];
        }

        evl2 = Math.abs(diaErr[0]) + Math.abs(diaErr[1]);
    }

    public void swap1(int x1, int y1, int x2, int y2) {
        int v1 = square[x1][y1];
        int v2 = square[x2][y2];
        int delt = v1 - v2;

        int r1, r2, c1, c2;
        r1 = rowErr[x1];
        r2 = rowErr[x2];
        c1 = colErr[y1];
        c2 = colErr[y2];

        rowErr[x1] += -delt;
        colErr[y1] += -delt;
        rowErr[x2] += delt;
        colErr[y2] += delt;

        evl1 += -Math.abs(r1) + Math.abs(rowErr[x1])
                - Math.abs(c1) + Math.abs(colErr[y1])
                - Math.abs(r2) + Math.abs(rowErr[x2])
                - Math.abs(c2) + Math.abs(colErr[y2]);


        square[x1][y1] = v2;
        square[x2][y2] = v1;
    }

    public void swap2(int x1, int y1, int x2, int y2) {
        int v1 = square[x1][y1];
        int v2 = square[x2][y2];
        int delt = v1 - v2, d0, d1;

        d0 = diaErr[0];
        d1 = diaErr[1];

        if (x1 == y1) {
            diaErr[0] += -delt;
        }
        if (x1 + y1 == n - 1) {
            diaErr[1] += -delt;
        }
        if (x2 == y2) {
            diaErr[0] += delt;
        }
        if (x2 + y2 == n - 1) {
            diaErr[1] += delt;
        }

        if (x1 == y1) {
            evl2 += -Math.abs(d0) + Math.abs(diaErr[0]);
        }
        if (x1 + y1 == n - 1) {
            evl2 += -Math.abs(d1) + Math.abs(diaErr[1]);
        }
        if (x2 == y2) {
            evl2 += -Math.abs(d0) + Math.abs(diaErr[0]);
        }
        if (x2 + y2 == n - 1) {
            evl2 += -Math.abs(d1) + Math.abs(diaErr[1]);
        }

        square[x1][y1] = v2;
        square[x2][y2] = v1;
    }
}
