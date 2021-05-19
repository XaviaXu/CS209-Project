package module.magic_square;

import java.util.Random;

/**
 * Swap two cells randomly
 */
public class MutateS0 implements Mutate {
    @Override
    public int[][] mutate(MagicSquare magicSquare) {

        int[][] nxt = magicSquare.boardCopy();

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
