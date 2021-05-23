package module;

import java.io.IOException;

public abstract class SquareProblem {
    public int n;
    public int[][] square;
    public int[][] cnst;

    public SquareProblem(int n) {
        this.n = n;
        this.cnst = new int[n][n];
        this.square = new int[n][n];
    }

    public abstract void readMs(String fn) throws IOException;

    public abstract void fill();

    public abstract void show();
}
