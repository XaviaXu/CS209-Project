package module.magic_square;

import java.util.Random;

/**
 * Swap two cells randomly
 */
public class MutateS1 implements Mutate {
    @Override
    public int[][] mutate(MagicSquare magicSquare) {
//        Set<Integer> row = new HashSet<>();
//
//        for (int i = 0; i < MagicSquare.n; ++i) {
//            int sum = 0;
//            for (int j = 0; j < magicSquare.n; ++j) {
//                sum += magicSquare.board[i][j];
//            }
//            if (sum - magicSquare.mn != 0) {
//                row.add(i);
//            }
//        }

        int[][] nxt = new int[magicSquare.n][];
        for (int i = 0; i < magicSquare.n; ++i) {
            nxt[i] = magicSquare.board[i].clone();
        }

        Random random = new Random();
        int x1, x2, y1, y2;
        do {
            x1 = random.nextInt(magicSquare.n);
            y1 = random.nextInt(magicSquare.n);
        } while (magicSquare.cnst[x1][y1] != 0);

        do {
            x2 = random.nextInt(magicSquare.n);
            y2 = random.nextInt(magicSquare.n);
        } while ((x1 == x2 && y1 == y2) || magicSquare.cnst[x2][y2] != 0);

        int tmp = nxt[x1][y1];
        nxt[x1][y1] = nxt[x2][y2];
        nxt[x2][y2] = tmp;

        return nxt;
    }
}
