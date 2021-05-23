package module.magic_square;

import java.util.ArrayList;
import java.util.Random;

public class MutateL1 implements Mutate {
    @Override
    public int[][] mutate(MagicSquare magicSquare) {
        int[][] nxt = magicSquare.boardCopy();
        Random random = new Random();

        ArrayList<Integer> set1, set2;
        set1 = new ArrayList<>();

        int[] rowErr, colErr, diaErr;
        rowErr = new int[magicSquare.n];
        colErr = new int[magicSquare.n];

        for (int i = 0; i < magicSquare.n; ++i) {
            for (int j = 0; j < magicSquare.n; ++j) {
                rowErr[i] += nxt[i][j];
                colErr[j] += nxt[i][j];
            }
        }

        for (int i = 0; i < magicSquare.n; ++i) {
            for (int j = 0; j < magicSquare.n; ++j) {
                if (magicSquare.cnst[i][j] == 0 &&
                        rowErr[i] != 0 &&
                        colErr[j] != 0) {
                    set1.add(magicSquare.n * i + j);
                }
            }
        }

        if (set1.size() > 0) {
            int e, t, m, cnt;
            int ex, ey, tx, ty, ev, tv;
            int oldErr, newErr;

            e = set1.get(random.nextInt(set1.size()));
            ex = e / magicSquare.n;
            ey = e % magicSquare.n;
            ev = nxt[ex][ey];
//            System.out.printf("%d, %d\n", ex, ey);

            cnt = 0;
            t = 0;
            m = magicSquare.n * magicSquare.n;


            while (cnt < m) {
                tx = t / magicSquare.n;
                ty = t % magicSquare.n;
                tv = nxt[tx][ty];

                if (magicSquare.cnst[tx][ty] == 0 && !(tx == ex && ty == ey)) {
                    oldErr = getErr(ex, ey, magicSquare.mn, nxt);

                    nxt[ex][ey] = tv;
                    nxt[tx][ty] = ev;
                    ev = nxt[ex][ey];
                    tv = nxt[tx][ty];

                    newErr = getErr(ex, ey, magicSquare.mn, nxt);

//                    System.out.printf("%d, %d <-> %d, %d: %d->%d\n", ex, ey, tx, ty, oldErr, newErr);
                    if (newErr < oldErr) {
                        cnt = 0;
                    } else {
                        nxt[ex][ey] = tv;
                        nxt[tx][ty] = ev;
                        ev = nxt[ex][ey];
                        tv = nxt[tx][ty];
                    }
                }

                t = (t + 1) % m;
                ++cnt;
            }

        }

        return nxt;
    }

    private int getErr(int x, int y, int mn, int[][] b) {
        int err = 0, sum, n = b.length;
        sum = 0;
        for (int i = 0; i < n; ++i) {
            sum += b[x][i];
        }
        err += Math.abs(sum - mn);

        sum = 0;
        for (int i = 0; i < n; ++i) {
            sum += b[i][y];
        }
        err += Math.abs(sum - mn);

        return err;
    }
}
