package module.magic_square;

import java.util.ArrayList;
import java.util.Random;

/**
 * LLH2: Select two rows, columns or diagonals randomly to swap as a whole.
 */
public class MutateL2 implements Mutate {
    @Override
    public int[][] mutate(MagicSquare magicSquare) {
        int[][] nxt = magicSquare.boardCopy();

        Random random = new Random();

        int dis = random.nextInt(2 * magicSquare.n + 2);

        ArrayList<Integer> lst = new ArrayList<>();


        if (dis < magicSquare.n) {
            // row
            for (int i = 0; i < magicSquare.n; ++i) {
                boolean check = true;
                for (int j = 0; j < magicSquare.n; ++j) {
                    if (magicSquare.cnst[i][j] != 0) {
                        check = false;
                        break;
                    }
                }
                if (check) {
                    lst.add(i);
                }
            }

            if (lst.size() >= 2) {
                int r1, r2;
                r1 = random.nextInt(lst.size());
                do {
                    r2 = random.nextInt(lst.size());
                } while (r1 == r2);

                r1 = lst.get(r1);
                r2 = lst.get(r2);

                int tmp;
                for (int i = 0; i < magicSquare.n; ++i) {
                    tmp = nxt[r1][i];
                    nxt[r1][i] = nxt[r2][i];
                    nxt[r2][i] = tmp;
                }
            }
        } else if (dis < 2 * magicSquare.n) {
            // column
            for (int i = 0; i < magicSquare.n; ++i) {
                boolean check = true;
                for (int j = 0; j < magicSquare.n; ++j) {
                    if (magicSquare.cnst[j][i] != 0) {
                        check = false;
                        break;
                    }
                }
                if (check) {
                    lst.add(i);
                }
            }

            if (lst.size() >= 2) {
                int l1, l2;
                l1 = random.nextInt(lst.size());
                do {
                    l2 = random.nextInt(lst.size());
                } while (l1 == l2);

                l1 = lst.get(l1);
                l2 = lst.get(l2);
                int tmp;
                for (int i = 0; i < magicSquare.n; ++i) {
                    tmp = nxt[i][l1];
                    nxt[i][l1] = nxt[i][l2];
                    nxt[i][l2] = tmp;
                }
            }
        } else {
            // diagonal
            boolean check = true;
            for (int i = 0; i < magicSquare.n; ++i) {
                if (magicSquare.cnst[i][i] != 0 || magicSquare.cnst[i][magicSquare.n - i - 1] != 0) {
                    check = false;
                    break;
                }
            }
            if(check){
                int tmp;
                for (int i = 0; i < magicSquare.n; ++i) {
                    tmp = nxt[i][i];
                    nxt[i][i] = nxt[i][magicSquare.n - i - 1];
                    nxt[i][magicSquare.n - i - 1] = tmp;
                }
            }
        }

        return nxt;
    }
}
