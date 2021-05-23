package module.magic_square;

public class MutateSL1 implements Mutate {
    @Override
    public int[][] mutate(MagicSquare magicSquare) {
        int[][] nxt = magicSquare.board; // ref

        int[] rowSum = new int[magicSquare.n];
        int[] colSum = new int[magicSquare.n];

        for (int i = 0; i < magicSquare.n; ++i) {
            for (int j = 0; j < magicSquare.n; ++j) {
                rowSum[i] += nxt[i][j];
                colSum[j] += nxt[i][j];
            }
        }

        // adjust row
        for (int i = 0; i < magicSquare.n; ++i) {
            if (rowSum[i] == magicSquare.mn)
                continue;
            for (int j = i + 1; j < magicSquare.n; ++j) {
                if (rowSum[i] + rowSum[j] == 2 * magicSquare.mn) {

                    // 1 pair
                    for (int k = 0; k < magicSquare.n; ++k) {
                        if (magicSquare.cnst[i][k] == 0 &&
                                magicSquare.cnst[j][k] == 0 &&
                                rowSum[i] - nxt[i][k] + nxt[j][k] == magicSquare.mn) {
                            int tmp = nxt[i][k];
                            nxt[i][k] = nxt[j][k];
                            nxt[j][k] = tmp;
                            break;
                        }
                    }

                    // 2 pair
                    for (int k = 0; k < magicSquare.n; ++k) {
                        for (int l = k + 1; l < magicSquare.n; ++l) {
                            if (magicSquare.cnst[i][k] == 0 &&
                                    magicSquare.cnst[j][k] == 0 &&
                                    magicSquare.cnst[i][l] == 0 &&
                                    magicSquare.cnst[j][l] == 0 &&
                                    rowSum[i] - nxt[i][k] - nxt[i][l] + nxt[j][k] + nxt[j][l] == magicSquare.n) {
                                int tmp;
                                tmp = nxt[i][k];
                                nxt[i][k] = nxt[j][k];
                                nxt[j][k] = tmp;

                                tmp = nxt[i][l];
                                nxt[i][l] = nxt[j][l];
                                nxt[j][l] = tmp;
                                break;
                            }
                        }
                    }
                }
            }
        }

        // adjust col
        for (int i = 0; i < magicSquare.n; ++i) {
            if (colSum[i] == magicSquare.mn)
                continue;
            for (int j = i + 1; j < magicSquare.n; ++j) {
                if (colSum[i] + colSum[j] == 2 * magicSquare.mn) {

                    // 1 pair
                    for (int k = 0; k < magicSquare.n; ++k) {
                        if (colSum[i] - nxt[k][i] + nxt[k][j] == magicSquare.mn &&
                                magicSquare.cnst[k][i] == 0 &&
                                magicSquare.cnst[k][j] == 0) {
                            int tmp = nxt[k][i];
                            nxt[k][i] = nxt[k][j];
                            nxt[k][j] = tmp;
                            break;
                        }
                    }

                    // 2 pair
                    for (int k = 0; k < magicSquare.n; ++k) {
                        for (int l = k + 1; l < magicSquare.n; ++l) {
                            if (colSum[i] - nxt[k][i] - nxt[l][i] + nxt[j][k] + nxt[j][l] == magicSquare.n &&
                                    magicSquare.cnst[k][i] == 0 &&
                                    magicSquare.cnst[k][j] == 0 &&
                                    magicSquare.cnst[l][i] == 0 &&
                                    magicSquare.cnst[l][j] == 0) {
                                int tmp;
                                tmp = nxt[k][i];
                                nxt[k][i] = nxt[j][k];
                                nxt[j][k] = tmp;

                                tmp = nxt[l][i];
                                nxt[l][i] = nxt[j][l];
                                nxt[j][l] = tmp;
                                break;
                            }
                        }
                    }
                }
            }
        }

        return nxt;
    }
}
