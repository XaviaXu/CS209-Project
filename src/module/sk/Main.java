package module.sk;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static double eps = 1e-18;
    public static double delt = 0.995;
    public static int T = 500000;

    public static void main(String[] args) throws IOException {
        int size = 3;

        SK cur = new SK(size);

        cur.readMs("input/" + size * size + ".in");
        cur.fill();

        int[][] mb;
        int cost, min;
        double t;

        System.out.println("round " + 0 + ": " + cur.getCost());
        cur.show();
        int cnt = 0;
        min = cur.getCost();
        mb = cur.square.clone();
        do {
            cur.square = mb.clone();
            cost = min;
            t = T;
            boolean ret;
            while (t >= eps && cost > 0) {
                ret = cur.swapCells(t, eps);
//                System.out.println(t + ", " + cur.getCost() + ": " + (ret ? "swap success" : "swap fail"));
//                if(ret) {
//                    cost = cur.getCost();
//                    if(cost <= min){
//                        if(cost < min){
//                            System.out.println("round " + cnt + ": " + min);
//                            cur.show();
//                        }
//                        min = cost;
//                        mb = cur.board.clone();
//                    }
//                }
                t *= delt;
            }
            ++cnt;
            if (cnt % 200 == 0) {
                System.out.println("round " + cnt + ": " + min);
            }

        } while (cur.getCost() > 0);

        System.out.println("The answer:");
        cur.show();
    }
}
