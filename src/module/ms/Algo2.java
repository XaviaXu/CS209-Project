package module.ms;

import module.magic_square.MutateS21;

import java.util.ArrayList;

public final class Algo2 {

    public static double t;
    public static int gen;

    public static MutateNew mutates;

    public static final double EPS = 1e-16;
    public static final double DELT = 0.95;
    public static final double T = 1e3;
    public static final int S = 1;

    public static RDList rdn;

    public static void init(int n) {
        mutates = new Mutate2S21();

        rdn = new RDList(n);
    }

    public static void algoInit() {
        t = T;
    }

    public static boolean nextGeneration(MS ms) {
        return mutates.mutate(ms);
    }

    public static void anneal() {
        t *= DELT;
    }
}
