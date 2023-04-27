package cz.sspbrno.leaderboards;

import java.util.ArrayList;

public class Weighting {
    public static float getRank(ArrayList<String> list, int listSize) {
        float rank = 0;
        int iteration = 0;
        for (int i = 0; i < (list.size() - 2) / 4; i++) {
            rank += ((float) (7-iteration)/6)*Formula.formula(listSize, list.get(i*4+5), list.get(i*4+3), list.get(i*4+4));
            iteration++;
        }
        return rank;
    }

    public static float getRank(String[] list, int listSize) {
        float rank = 0;
        int iteration = 0;
        for (int i = 0; i < (list.length - 2) / 4; i++) {
            rank += ((float) (7-iteration)/6)*Formula.formula(listSize, list[i*4+5], list[i*4+3], list[i*4+4]);
            iteration++;
        }
        return rank;
    }
}
