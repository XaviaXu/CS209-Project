package module.sk;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static double eps = 1e-16;
    public static double delt = 0.99;
    public static double T = 1e4;

    public static void main(String[] args) throws IOException {
        int size = 3;

        SK cur = new SK(size);

        cur.readMs("input/" + size * size + ".in");
        cur.fill();

        int[][] mb;
        int cost, min;
        double t;

        long t1 = System.currentTimeMillis();
        System.out.println("round " + 0 + ": " + cur.getCost());
        cur.show();
        int cnt = 0;
        min = cur.getCost();
        mb = cur.square.clone();
        cost = cur.getCost();
        do {
//            cur.square = mb.clone();
//            cost = min;
            t = T;
            boolean ret;
            while (t >= eps && cur.getCost() > 0) {
                ret = cur.swapCells(t, eps);
                t *= delt;
            }
            ++cnt;
            if (cnt % 200 == 0) {
                System.out.println("round " + cnt + ": " + cur.getCost());
            }

        } while (cur.getCost() > 0);

        System.out.println("The answer:");
        cur.show();

        long t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);
    }
}
