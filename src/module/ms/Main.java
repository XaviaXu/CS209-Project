package module.ms;

import module.magic_square.MathUtil;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        int lvl = 20;
        MS ms = new MS(lvl);
        ms.readMs("input/" + lvl + ".in");

        int rnd = 30;
        double[] times1 = new double[rnd];
        double[] times2 = new double[rnd];
        double[] tot_times = new double[rnd];

        Algo1.init(lvl);
        Algo2.init(lvl);

        ms.show();
        ms.fill();

        for (int it = 0; it < rnd; ++it) {
            ms.fill();

            // step 1
            System.out.println("new test: " + it + "\n" +
                    "step1:=======");
            long t1 = System.currentTimeMillis();

            int gen = 0;
            long cnt = 0;

            ms.reEvl1();
            do {
//                if (gen % 2 == 0) {
//                    System.out.println("round " + gen + ": " + ms.evl1);
//                }

                Algo1.algoInit();
//                ms.reEvl1();
                while (Algo1.t >= Algo1.EPS && ms.evl1 > 0) {
                    for (int i = 0; i < Algo1.S; ++i) {
                        boolean ret = Algo1.nextGeneration(ms);
                        ++cnt;
                    }
                    Algo1.anneal();
                }

                ++gen;
            } while (ms.evl1 > 0 && gen <= 20000);

            long t2 = System.currentTimeMillis();
            times1[it] = t2 - t1;

            System.out.printf("time: %dms, gen: %d, cnt: %d\n" +
                    "evl1: %d, evl2: %d\n", t2 - t1, gen, cnt, ms.evl1, ms.evl2);

            // step2
            System.out.println("step 2: ========");
            t1 = System.currentTimeMillis();

            gen = 0;
            cnt = 0;

            ms.reEvl2();
            do {
//                if (gen % 200 == 0) {
//                    System.out.println("round " + gen + ": " + ms.evl2);
//                }

                Algo2.algoInit();
//                ms.reEvl1();
                while (Algo2.t >= Algo2.EPS && ms.evl2 > 0) {
                    for (int i = 0; i < Algo2.S; ++i) {
                        boolean ret = Algo2.nextGeneration(ms);
//                        if (ret) {
//                            System.out.println("accept");
//                        }
                        ++cnt;
                    }
                    Algo2.anneal();
                }

                ++gen;
            } while (ms.evl2 > 0 && gen <= 200000);

            t2 = System.currentTimeMillis();
            times2[it] += t2 - t1;

            System.out.printf("time: %dms, gen: %d, cnt: %d\n" +
                    "evl1: %d, evl2: %d\n", t2 - t1, gen, cnt, ms.evl1, ms.evl2);

            tot_times[it] = times1[it] + times2[it];
        }

        // output result
        long sum;
        double dev;

        for (double i : times1) {
            System.out.printf("%d ", (int) i);
        }
        sum = (long) (MathUtil.sum(times1) / rnd);
        dev = MathUtil.popStdDev(times1);
        System.out.printf("\nti1: avg time: %dms, std dev: %fms\n", sum, dev);


        for (double i : times2) {
            System.out.printf("%d ", (int) i);
        }
        sum = (long) (MathUtil.sum(times2) / rnd);
        dev = MathUtil.popStdDev(times2);
        System.out.printf("\nti2: avg time: %dms, std dev: %fms\n", sum, dev);

        for (double i : tot_times) {
            System.out.printf("%d ", (int) i);
        }
        sum = (long) (MathUtil.sum(tot_times) / rnd);
        dev = MathUtil.popStdDev(tot_times);
        System.out.printf("\ntot: avg time: %dms, std dev: %fms\n", sum, dev);

        ms.show();
    }
}
