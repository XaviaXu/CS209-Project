package module.magic_square;

import java.util.ArrayList;
import java.util.Random;

public class MutateS13 implements Mutate {
    @Override
    public int[][] mutate(MagicSquare magicSquare) {
        int[][] nxt = magicSquare.boardCopy();
        Random random = new Random();

        ArrayList<Integer> set2;
        set2 = new ArrayList<>();

        int[] rowErr, colErr;
        rowErr = new int[magicSquare.n];
        colErr = new int[magicSquare.n];

        for (int i = 0; i < magicSquare.n; ++i) {
            for (int j = 0; j < magicSquare.n; ++j) {
                rowErr[i] += nxt[i][j];
                colErr[j] += nxt[i][j];
            }
            rowErr[i] -= magicSquare.mn;
            colErr[i] -= magicSquare.mn;
        }

        for (int i = 0; i < magicSquare.n; ++i) {
            for (int j = 0; j < magicSquare.n; ++j) {
                if (magicSquare.cnst[i][j] == 0) {
                    if (rowErr[i] != 0 || colErr[j] != 0) {
                        set2.add(magicSquare.n * i + j);
                    }
                }
            }
        }

        if (set2.isEmpty())
            return nxt;

        int x1, y1, x2, y2, c1, c2;
        c1 = set2.get(random.nextInt(set2.size()));
        do {
            c2 = random.nextInt(magicSquare.n * magicSquare.n);
        } while (c1 == c2);

        x1 = c1 / magicSquare.n;
        y1 = c1 % magicSquare.n;
        x2 = c2 / magicSquare.n;
        y2 = c2 % magicSquare.n;

        nxt[x1][y1] = magicSquare.board[x2][y2];
        nxt[x2][y2] = magicSquare.board[x1][y1];

        return nxt;
    }
}
