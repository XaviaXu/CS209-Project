package module.sudoku;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class Sudoku implements Cloneable{
    public int size;
    public int n;
    public boolean[][] nonFixed;
    public int[][] board;
    public Integer[][] nonFixedCells;
    public Integer[] nonFixedSquares;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Sudoku() {
    }

    public Sudoku(int size) {
        this.size = size;
        n = size * size;
        nonFixed = new boolean[n][n];
        board = new int[n][n];
        nonFixedCells = new Integer[n][];
    }

    public void read(Scanner input) {
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                board[i][j] = input.nextInt();
                nonFixed[i][j] = board[i][j] == 0;
            }
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
                        if (!nonFixed[i][j]) {
                            vis[board[i][j]] = true;
                        } else {
                            arr.add(i * n + j);
                            vis[board[i][j]] = false;
                        }
                    }
                }

                for (int i = 0, tx, ty; i < arr.size(); ++i) {
                    tx = arr.get(i) / n;
                    ty = arr.get(i) % n;
                    for (; k <= n; ++k) {
                        if (!vis[k]) {
                            vis[k] = true;
                            board[tx][ty] = k;
                            break;
                        }
                    }
                }

                if (arr.size() == 1) {
                    x = arr.get(0) / n;
                    y = arr.get(0) % n;
                    nonFixed[x][y] = false;
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
        for (int cell: nonFixedCells[sqr]) {
            check = false;
            x = cell / n;
            y = cell % n;
            for (int i = 0; i < n; ++i) {
                if (x != i && board[x][y] == board[i][y]) {
                    cnf.add(cell);
                    check = true;
                    break;
                }
            }

            if(check) continue;

            for (int j = 0; j < n; ++j) {
                if (y != j && board[x][y] == board[x][j]) {
                    cnf.add(cell);
                    break;
                }
            }
        }

        if(cnf.size() <= 0)
            return false;
        a = random.nextInt(cnf.size());
        do {
            b = random.nextInt(nonFixedCells[sqr].length);
        } while (b == a);

        ia = cnf.get(a);
        ib = nonFixedCells[sqr][b];

        int cur_cost, nxt_cost;
        cur_cost = getCost();

        tmp = board[ia / n][ia % n];
        board[ia / n][ia % n] = board[ib / n][ib % n];
        board[ib / n][ib % n] = tmp;

        nxt_cost = getCost();

//        System.out.printf("%d, %d -> %d, %d\n", ia / n, ia % n, ib / n, ib % n);
        if (nxt_cost <= cur_cost || Math.exp((cur_cost - nxt_cost) / t) >= Math.random()) {
            return true;
        }

        // roll back
        tmp = board[ia / n][ia % n];
        board[ia / n][ia % n] = board[ib / n][ib % n];
        board[ib / n][ib % n] = tmp;
        return false;

    }

    public int getCost() {
        int cost = 0;
        boolean[] visx, visy;
        for (int i = 0; i < n; ++i) {
            visx = new boolean[n + 1];
            visy = new boolean[n + 1];
            for (int j = 0; j < n; ++j) {
                visx[board[i][j]] = true;
                visy[board[j][i]] = true;
            }

            for (int j = 1; j <= n; ++j) {
                if(!visx[j]) ++cost;
                if(!visy[j]) ++cost;
            }
        }
        return cost;
    }

    public void show() {
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                System.out.printf("%3d", board[i][j]);
            }
            System.out.println();
        }
        System.out.println("=================");
    }

    public boolean is_fixed(){
        return nonFixedSquares.length == 0;
    }
}

public class SimulatedAnneling {
    public static double eps = 1e-18;
    public static double delt = 0.995;
    public static int T = 500000;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();

        Sudoku cur;
        cur = new Sudoku(size);
        cur.read(scanner);

        int[][] mb;
        int cost, min;
        double t;

        System.out.println("round " + 0 + ": " + cur.getCost());
        cur.show();
        int cnt = 0;
        min = cur.getCost();
        mb = cur.board.clone();
        do {
            cur.board = mb.clone();
            cost = min;
            t = T;
            boolean ret;
            while (t >= eps && cost > 0) {
                ret = cur.swapCells(t, eps);
//                System.out.println(t + ", " + cur.getCost() + ": " + (ret ? "swap success" : "swap fail"));
                if(ret) {
                    cost = cur.getCost();
                    if(cost <= min){
                        if(cost < min){
                            System.out.println("round " + cnt + ": " + min);
                            cur.show();
                        }
                        min = cost;
                        mb = cur.board.clone();
                    }
                }
                t *= delt;
            }
            ++cnt;
            if(cnt % 200 == 0){
                System.out.println("round " + cnt + ": " + min);
            }

        } while (cur.getCost() > 0);

        System.out.println("The answer:");
        cur.show();
    }


}
