package module.ms;

import java.util.Random;

public class RDList {

    public static int L = (int)1e6;
    public int[] rdList;
    public int p;

    public RDList(int n){
        Random random = new Random();
        p = 0;
        rdList = new int[L];
        for (int i = 0; i < L; ++i) {
            rdList[i] = random.nextInt(n);
        }
    }

    public int getNext(){
        p = (p + 1) % L;
        return rdList[p];
    }
}
