package module.ms;

import java.util.ArrayList;

public class MutateNewS11 implements MutateNew {
    @Override
    public boolean mutate(MS ms) {

        ArrayList<Integer> set1, set2;
        set1 = new ArrayList<>();
        set2 = new ArrayList<>();

        for (int i = 0; i < ms.n; ++i) {
            for (int j = 0; j < ms.n; ++j) {
                if (ms.cnst[i][j] == 0) {
                    if (ms.rowErr[i] != 0 && ms.colErr[j] != 0) {
                        set1.add(ms.n * i + j);
                    }
                    if (ms.rowErr[i] != 0 || ms.colErr[j] != 0) {
                        set2.add(ms.n * i + j);
                    }
                }
            }
        }

        if (set1.isEmpty() || set2.isEmpty())
            return false;

        int x1, y1, x2, y2, c1, c2;

        c1 = set1.get(Algo1.rdn.getNext());
        do {
            c2 = set2.get(Algo1.rdn.getNext());
        } while (c1 == c2);

        x1 = c1 / ms.n;
        y1 = c1 % ms.n;
        x2 = c2 / ms.n;
        y2 = c2 % ms.n;

        int cur_err;
        cur_err = ms.evl1;

        ms.swap1(x1, y1, x2, y2);
        if (ms.evl1 <= cur_err || Math.exp((cur_err - ms.evl1) / Algo1.t) >= Math.random()) {
//            System.out.printf("%d %d %d %d\n", x1, y1, x2, y2);
            return true;
        }
        ms.swap1(x1, y1, x2, y2);
        return false;
    }
}
