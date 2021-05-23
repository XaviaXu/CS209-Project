package module.ms;

import java.util.ArrayList;

public final class Algo1 {

    public static double t;
    public static int gen;

    public static ArrayList<MutateNew> mutates = new ArrayList<>();

    public static final double EPS = 1e-12;
    public static final double DELT = 0.995;
    public static final double T = 1e4;
    public static final int S = 500;

    public static RDList rdn;
    public static RDList rd_mutate;

    public static void init(int n){
        for (int i = 0; i < 4; ++i)
            mutates.add(new MutateNewS0());
//        for (int i = 0; i < 6; ++i)
//            mutates.add(new Mutate1S11());
//        for (int i = 0; i < 4; ++i)
//            mutates.add(new Mutate1S12());
//        for (int i = 0; i < 2; ++i)
//            mutates.add(new Mutate1S13());
//        for (int i = 0; i < 1; ++i)
//            mutates.add(new MutateL1());

        rd_mutate = new RDList(mutates.size());
        rdn = new RDList(n);
    }

    public static void algoInit() {
        t = T;
    }

    public static boolean nextGeneration(MS ms) {
        int chs;
        chs = rd_mutate.getNext();
        return mutates.get(chs).mutate(ms);
    }

    public static void anneal() {
        t *= DELT;
    }
}
