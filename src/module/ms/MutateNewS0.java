package module.ms;

/**
 * Swap two cells randomly
 */
public class MutateNewS0 implements MutateNew {
    @Override
    public boolean mutate(MS ms) {

        int cur_err, x1, y1, x2, y2;
        cur_err = ms.evl1;

        do {
            x1 = Algo1.rdn.getNext();
            y1 = Algo1.rdn.getNext();
        } while (ms.cnst[x1][y1] != 0);

        do {
            x2 = Algo1.rdn.getNext();
            y2 = Algo1.rdn.getNext();
        } while ((x1 == x2 && y1 == y2) || ms.cnst[x2][y2] != 0);

        ms.swap1(x1, y1, x2, y2);

        if (ms.evl1 <= cur_err || Math.exp((cur_err - ms.evl1) / Algo1.t) >= Math.random()) {
//            System.out.printf("%d %d %d %d\n", x1, y1, x2, y2);
            return true;
        }

        ms.swap1(x1, y1, x2, y2);
        return false;
    }
}
