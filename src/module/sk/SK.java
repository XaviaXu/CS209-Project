package module.sk;

import module.SquareProblem;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class SK extends SquareProblem {
    public int size;
    public Integer[][] nonFixedCells;
    public Integer[] nonFixedSquares;

    public SK(int size) {
        super(size * size);
        this.size = size;
        nonFixedCells = new Integer[n][];
    }

    @Override
    public void readMs(String fn) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(fn);

        Scanner fileScanner = new Scanner(fileInputStream);

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                cnst[i][j] = fileScanner.nextInt();
            }
        }
    }

    @Override
    public void fill() {
        for (int i = 0; i < n; ++i) {
            System.arraycopy(cnst[i], 0, square[i], 0, n);
        }

        ArrayList<Integer> squares = new ArrayList<>();
        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                // each square
                ArrayList<Integer> arr = new ArrayList<>();

                boolean[] vis = new boolean[n + 1];
                int k = 1;
                for (int i = x * size; i < x * size + size; ++i) {
                    for (int j = y * size; j < y * size + size; ++j) {
                        // each fixed cell
                        if (cnst[i][j] != 0) {
                            vis[square[i][j]] = true;
                        } else {
                            arr.add(i * n + j);
                            vis[square[i][j]] = false;
                        }
                    }
                }

                for (int i = 0, tx, ty; i < arr.size(); ++i) {
                    tx = arr.get(i) / n;
                    ty = arr.get(i) % n;
                    for (; k <= n; ++k) {
                        if (!vis[k]) {
                            vis[k] = true;
                            square[tx][ty] = k;
                            break;
                        }
                    }
                }

                if (arr.size() == 1) {
                    int xx = arr.get(0) / n;
                    int yy = arr.get(0) % n;
                    cnst[xx][yy] = square[xx][yy];
                    arr.clear();
                }
                nonFixedCells[x * size + y] = arr.toArray(new Integer[0]);
                if (arr.size() > 0) {
                    squares.add(x * size + y);
                }
            }
        }
        nonFixedSquares = squares.toArray(new Integer[0]);
    }

    public boolean swapCells(double t, double eps) {
        int a, b, ia, ib, sqr, rand, tmp;
        Random random = new Random();

        rand = random.nextInt(nonFixedSquares.length);
        sqr = nonFixedSquares[rand];

        ArrayList<Integer> cnf = new ArrayList<>();

        boolean check;
        int x, y;
        for (int cell : nonFixedCells[sqr]) {
            check = false;
            x = cell / n;
            y = cell % n;
            for (int i = 0; i < n; ++i) {
                if (x != i && square[x][y] == square[i][y]) {
                    cnf.add(cell);
                    check = true;
                    break;
                }
            }

            if (check) continue;

            for (int j = 0; j < n; ++j) {
                if (y != j && square[x][y] == square[x][j]) {
                    cnf.add(cell);
                    break;
                }
            }
        }

        if (cnf.size() <= 0)
            return false;
        a = random.nextInt(cnf.size());
        do {
            b = random.nextInt(nonFixedCells[sqr].length);
        } while (b == a);

        ia = cnf.get(a);
        ib = nonFixedCells[sqr][b];

        int cur_cost, nxt_cost;
        cur_cost = getCost();

        tmp = square[ia / n][ia % n];
        square[ia / n][ia % n] = square[ib / n][ib % n];
        square[ib / n][ib % n] = tmp;

        nxt_cost = getCost();

//        System.out.printf("%d, %d -> %d, %d\n", ia / n, ia % n, ib / n, ib % n);
        if (nxt_cost <= cur_cost || Math.exp((cur_cost - nxt_cost) / t) >= Math.random()) {
            return true;
        }

        // roll back
        tmp = square[ia / n][ia % n];
        square[ia / n][ia % n] = square[ib / n][ib % n];
        square[ib / n][ib % n] = tmp;
        return false;

    }

    public int getCost() {
        int cost = 0;
        boolean[] visx, visy;
        for (int i = 0; i < n; ++i) {
            visx = new boolean[n + 1];
            visy = new boolean[n + 1];
            for (int j = 0; j < n; ++j) {
                visx[square[i][j]] = true;
                visy[square[j][i]] = true;
            }

            for (int j = 1; j <= n; ++j) {
                if (!visx[j]) ++cost;
                if (!visy[j]) ++cost;
            }
        }
        return cost;
    }

    @Override
    public void show() {
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                System.out.printf("%3d", square[i][j]);
            }
            System.out.println();
        }
        System.out.println("=================");
    }

    public boolean is_fixed() {
        return nonFixedSquares.length == 0;
    }
}
