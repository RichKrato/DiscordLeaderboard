package cz.sspbrno.leaderboards;

import cz.sspbrno.sql.SQLConnect;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Leaderboard {
    private final ArrayList<ArrayList<String>> list;
    private final int listSize;

    public Leaderboard() throws SQLException {
        this.list = shrink(Ranking.playerRanking());
        this.listSize = new SQLConnect().select("demon").size();
    }

    public ArrayList<ArrayList<String>> shrink(ArrayList<String[]> list) {
        ArrayList<ArrayList<String>> newList = new ArrayList<>();
        int count;
        for (String[] strings : list) {
            String[] thing = new String[strings.length];
            ArrayList<String> second = new ArrayList<>();
            count = 0;
            for (String string : strings) {
                if (string == null) {
                    thing[count] = "";
                    count++;
                } else {
                    thing[count] = string;
                    count++;
                }
            }
            for (int j = 0; j < strings.length; j++) if (!thing[j].isEmpty()) second.add(thing[j]);
            newList.add(second);
        }
        return newList;
    }

    public ArrayList<String> top20() {
        ArrayList<ArrayList<String>> newList = sortByRank(list);
        for (ArrayList<String> list: newList) System.out.println(Arrays.toString(list.toArray()));
        ArrayList<String> leaderboard = new ArrayList<>();
        leaderboard.add("====================\n**Top 20 CZ/SK Hracu (+ Nejvetsi uspechy)**\n");
        for (int i = 0; i < 20; i++) {
            switch (newList.get(i).size()) {
                case 20:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s)",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4), newList.get(i).get(5), newList.get(i).get(6), newList.get(i).get(7),
                            newList.get(i).get(8), newList.get(i).get(9), newList.get(i).get(10), newList.get(i).get(11), newList.get(i).get(12), newList.get(i).get(13),
                            newList.get(i).get(14), newList.get(i).get(15), newList.get(i).get(16), newList.get(i).get(17), newList.get(i).get(18), newList.get(i).get(19)));
                    break;
                case 17:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s)",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4), newList.get(i).get(5), newList.get(i).get(6), newList.get(i).get(7),
                            newList.get(i).get(8), newList.get(i).get(9), newList.get(i).get(10), newList.get(i).get(11), newList.get(i).get(12), newList.get(i).get(13),
                            newList.get(i).get(14), newList.get(i).get(15), newList.get(i).get(16)));
                    break;
                case 14:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s))",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4), newList.get(i).get(5), newList.get(i).get(6), newList.get(i).get(7),
                            newList.get(i).get(8), newList.get(i).get(9), newList.get(i).get(10), newList.get(i).get(11), newList.get(i).get(12), newList.get(i).get(13)));
                    break;
                case 11:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s), %s %s%% (%s), %s %s%% (%s)",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4), newList.get(i).get(5), newList.get(i).get(6), newList.get(i).get(7),
                            newList.get(i).get(8), newList.get(i).get(9), newList.get(i).get(10)));
                    break;
                case 8:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s), %s %s%% (%s)",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4), newList.get(i).get(5), newList.get(i).get(6), newList.get(i).get(7)));
                    break;
                case 5:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s)",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4)));
                    break;
            }
        }
        return leaderboard;
    }

    public ArrayList<String> top60hz() {
        ArrayList<String> leaderboard = new ArrayList<>();
        ArrayList<String[]> preList = new ArrayList<>();
        ArrayList<ArrayList<String>> mid;
        ArrayList<ArrayList<String>> newList = new ArrayList<>();
        for (ArrayList<String> listEach : list) {
            String[] lol = new String[listEach.size()];
            lol[0] = listEach.get(0);
            lol[1] = listEach.get(1);
            for (int j = 1; j <= (listEach.size() - 2) / 4; j++)
                if (listEach.get(j * 4).equals("60hz")) {
                    lol[j*4-2] = listEach.get(j*4-2);
                    lol[j*4-1] = listEach.get(j*4-1);
                    lol[j*4] = listEach.get(j*4);
                    lol[j*4+1] = listEach.get(j*4+1);
                }
            preList.add(lol);
        }
        mid = sortByRank(shrink(preList));
        for (ArrayList<String> strings : mid) if (strings.size() > 2) newList.add(strings);
        leaderboard.add("====================\n**Top 10 (" + newList.size() + ") CZ/SK Hracu na 60hz (+ Nejvetsi uspechy)**\n");
        for (int i = 0; i < newList.size(); i++) {
            switch (newList.get(i).size()) {
                case 20:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s)",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4), newList.get(i).get(5), newList.get(i).get(6), newList.get(i).get(7),
                            newList.get(i).get(8), newList.get(i).get(9), newList.get(i).get(10), newList.get(i).get(11), newList.get(i).get(12), newList.get(i).get(13),
                            newList.get(i).get(14), newList.get(i).get(15), newList.get(i).get(16), newList.get(i).get(17), newList.get(i).get(18), newList.get(i).get(19)));
                    break;
                case 17:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s)",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4), newList.get(i).get(5), newList.get(i).get(6), newList.get(i).get(7),
                            newList.get(i).get(8), newList.get(i).get(9), newList.get(i).get(10), newList.get(i).get(11), newList.get(i).get(12), newList.get(i).get(13),
                            newList.get(i).get(14), newList.get(i).get(15), newList.get(i).get(16)));
                    break;
                case 14:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s))",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4), newList.get(i).get(5), newList.get(i).get(6), newList.get(i).get(7),
                            newList.get(i).get(8), newList.get(i).get(9), newList.get(i).get(10), newList.get(i).get(11), newList.get(i).get(12), newList.get(i).get(13)));
                    break;
                case 11:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s), %s %s%% (%s), %s %s%% (%s)",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4), newList.get(i).get(5), newList.get(i).get(6), newList.get(i).get(7),
                            newList.get(i).get(8), newList.get(i).get(9), newList.get(i).get(10)));
                    break;
                case 8:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s), %s %s%% (%s)",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4), newList.get(i).get(5), newList.get(i).get(6), newList.get(i).get(7)));
                    break;
                case 5:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s)",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4)));
                    break;
            }
        }
        return leaderboard;
    }

    public ArrayList<String> topMobile() {
        ArrayList<String> leaderboard = new ArrayList<>();
        ArrayList<String[]> preList = new ArrayList<>();
        ArrayList<ArrayList<String>> mid;
        ArrayList<ArrayList<String>> newList = new ArrayList<>();
        for (ArrayList<String> listEach : list) {
            String[] lol = new String[listEach.size()];
            lol[0] = listEach.get(0);
            lol[1] = listEach.get(1);
            for (int j = 1; j <= (listEach.size() - 2) / 4; j++)
                if (listEach.get(j * 4).equals("mobile")) {
                    lol[j*4-2] = listEach.get(j*4-2);
                    lol[j*4-1] = listEach.get(j*4-1);
                    lol[j*4] = listEach.get(j*4);
                    lol[j*4+1] = listEach.get(j*4+1);
                }
            preList.add(lol);
        }
        mid = sortByRank(shrink(preList));
        for (ArrayList<String> strings : mid) if (strings.size() > 2) newList.add(strings);
        leaderboard.add("====================\n**Top 5 (" + newList.size() + ") CZ/SK Mobile Hracu (+ Nejvetsi uspechy)**\n");
        for (int i = 0; i < newList.size(); i++) {
            switch (newList.get(i).size()) {
                case 20:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s)",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4), newList.get(i).get(5), newList.get(i).get(6), newList.get(i).get(7),
                            newList.get(i).get(8), newList.get(i).get(9), newList.get(i).get(10), newList.get(i).get(11), newList.get(i).get(12), newList.get(i).get(13),
                            newList.get(i).get(14), newList.get(i).get(15), newList.get(i).get(16), newList.get(i).get(17), newList.get(i).get(18), newList.get(i).get(19)));
                    break;
                case 17:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s)",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4), newList.get(i).get(5), newList.get(i).get(6), newList.get(i).get(7),
                            newList.get(i).get(8), newList.get(i).get(9), newList.get(i).get(10), newList.get(i).get(11), newList.get(i).get(12), newList.get(i).get(13),
                            newList.get(i).get(14), newList.get(i).get(15), newList.get(i).get(16)));
                    break;
                case 14:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s))",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4), newList.get(i).get(5), newList.get(i).get(6), newList.get(i).get(7),
                            newList.get(i).get(8), newList.get(i).get(9), newList.get(i).get(10), newList.get(i).get(11), newList.get(i).get(12), newList.get(i).get(13)));
                    break;
                case 11:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s), %s %s%% (%s), %s %s%% (%s)",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4), newList.get(i).get(5), newList.get(i).get(6), newList.get(i).get(7),
                            newList.get(i).get(8), newList.get(i).get(9), newList.get(i).get(10)));
                    break;
                case 8:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s), %s %s%% (%s)",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4), newList.get(i).get(5), newList.get(i).get(6), newList.get(i).get(7)));
                    break;
                case 5:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s)",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4)));
                    break;
            }
        }
        return leaderboard;
    }

    private ArrayList<ArrayList<String>> sortByRank(ArrayList<ArrayList<String>> list) {
        ArrayList<String[]> preList = new ArrayList<>();
        ArrayList<ArrayList<String>> newList;
        for (ArrayList<String> strings : list) {
            float rank = 0;
            switch (strings.size()) {
                case 26:
                    rank += Formula.formula(listSize, strings.get(5), strings.get(3), strings.get(4)) +
                            Formula.formula(listSize, strings.get(9), strings.get(7), strings.get(8)) +
                            Formula.formula(listSize, strings.get(13), strings.get(11), strings.get(12)) +
                            Formula.formula(listSize, strings.get(17), strings.get(15), strings.get(16)) +
                            Formula.formula(listSize, strings.get(21), strings.get(19), strings.get(20)) +
                            Formula.formula(listSize, strings.get(25), strings.get(23), strings.get(24));
                    break;
                case 22:
                    rank += Formula.formula(listSize, strings.get(5), strings.get(3), strings.get(4)) +
                            Formula.formula(listSize, strings.get(9), strings.get(7), strings.get(8)) +
                            Formula.formula(listSize, strings.get(13), strings.get(11), strings.get(12)) +
                            Formula.formula(listSize, strings.get(17), strings.get(15), strings.get(16)) +
                            Formula.formula(listSize, strings.get(21), strings.get(19), strings.get(20));
                    break;
                case 18:
                    rank += Formula.formula(listSize, strings.get(5), strings.get(3), strings.get(4)) +
                            Formula.formula(listSize, strings.get(9), strings.get(7), strings.get(8)) +
                            Formula.formula(listSize, strings.get(13), strings.get(11), strings.get(12)) +
                            Formula.formula(listSize, strings.get(17), strings.get(15), strings.get(16));
                    break;
                case 14:
                    rank += Formula.formula(listSize, strings.get(5), strings.get(3), strings.get(4)) +
                            Formula.formula(listSize, strings.get(9), strings.get(7), strings.get(8)) +
                            Formula.formula(listSize, strings.get(13), strings.get(11), strings.get(12));
                    break;
                case 10:
                    rank += Formula.formula(listSize, strings.get(5), strings.get(3), strings.get(4)) +
                            Formula.formula(listSize, strings.get(9), strings.get(7), strings.get(8));
                    break;
                case 6:
                    rank += Formula.formula(listSize, strings.get(5), strings.get(3), strings.get(4));
                    break;
            }
            strings.set(0, String.valueOf(rank));
            String[] lul = new String[strings.size() - (strings.size() - 2) / 4];
            lul[0] = strings.get(0);
            lul[1] = strings.get(1);
            int count = 2;
            for (int j = 1; j <= (strings.size() - 2) / 4; j++) {
                lul[count] = strings.get(j*4-2);
                lul[count+1] = strings.get(j*4-1);
                lul[count+2] = strings.get(j*4);
                count += 3;
            }
            preList.add(lul);
        }
        newList = shrink(preList);
        newList.sort((o1, o2) -> {
            if (Float.parseFloat(o1.get(0)) < Float.parseFloat(o2.get(0))) return 1;
            else if (Float.parseFloat(o1.get(0)) > Float.parseFloat(o2.get(0))) return -1;
            return 0;
        });
        return newList;
    }
}
