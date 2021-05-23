package module.ms;

import module.magic_square.MagicSquare;
import module.magic_square.Mutate;

import java.util.ArrayList;
import java.util.Random;

/**
 * LLH2: Select two rows, columns or diagonals randomly to swap as a whole.
 */
public class Mutate2S21 implements MutateNew {
    @Override
    public boolean mutate(MS ms) {

        int dis = Algo2.rdn.getNext() % 2;

        if (dis == 0) {
            // row

            int r1, r2;
            do {
                r1 = Algo2.rdn.getNext();
            } while (ms.rowCnst[r1]);
            do {
                r2 = Algo2.rdn.getNext();
            } while (ms.rowCnst[r2] || r1 == r2);

            int cur_err;
            cur_err = ms.evl2;

            for (int i = 0; i < ms.n; ++i) {
                ms.swap2(r1, i, r2, i);
            }

            if (ms.evl2 <= cur_err || Math.exp((cur_err - ms.evl2) / Algo2.t) >= Math.random()) {
//            System.out.printf("%d %d %d %d\n", x1, y1, x2, y2);
                return true;
            }

            for (int i = 0; i < ms.n; ++i) {
                ms.swap2(r1, i, r2, i);
            }

        } else {

            int l1, l2;
            do {
                l1 = Algo2.rdn.getNext();
            } while (ms.colCnst[l1]);
            do {
                l2 = Algo2.rdn.getNext();
            } while (ms.colCnst[l2] || l1 == l2);

            int cur_err;
            cur_err = ms.evl2;

            for (int i = 0; i < ms.n; ++i) {
                ms.swap2(i, l1, i, l2);
            }

            if (ms.evl2 <= cur_err || Math.exp((cur_err - ms.evl2) / Algo2.t) >= Math.random()) {
//            System.out.printf("%d %d %d %d\n", x1, y1, x2, y2);
                return true;
            }

            for (int i = 0; i < ms.n; ++i) {
                ms.swap2(i, l1, i, l2);
            }
        }

        return false;
    }
}
