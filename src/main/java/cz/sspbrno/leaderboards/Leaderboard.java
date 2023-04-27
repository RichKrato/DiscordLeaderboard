package cz.sspbrno.leaderboards;

import cz.sspbrno.sql.SQLConnect;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Leaderboard {

    public static ArrayList<ArrayList<String>> shrink(ArrayList<String[]> list) {
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

    public static ArrayList<String> topList(int type) throws SQLException, IOException {
        ArrayList<String> leaderboard = new ArrayList<>();
        ArrayList<ArrayList<String>> list = shrink(Ranking.playerRanking(type));
        ArrayList<String[]> preList = new ArrayList<>();
        for (ArrayList<String> listEach : list) {
            String[] lol = new String[listEach.size()];
            lol[0] = listEach.get(0);
            lol[1] = listEach.get(1);
            for (int j = 1; j <= (listEach.size() - 2) / 4; j++) {
                lol[j*4-2] = listEach.get(j*4-2);
                lol[j*4-1] = listEach.get(j*4-1);
                lol[j*4] = listEach.get(j*4);
                lol[j*4+1] = listEach.get(j*4+1);
            }
            preList.add(lol);
        }
        ArrayList<ArrayList<String>> mid = sortByRank(shrink(preList));
        ArrayList<ArrayList<String>> newList = new ArrayList<>();
        for (ArrayList<String> strings : mid) if (strings.size() > 2) newList.add(strings);
        int size = 0;
        switch (type) {
            case 0:
                leaderboard.add("====================\n**Top 20 CZ/SK Hracu (+ Nejvetsi uspechy)**\n");
                size = 20;
                break;
            case 1:
                leaderboard.add("====================\n**Top 10 (" + newList.size() + ") CZ/SK Hracu na 60hz (+ Nejvetsi uspechy)**\n");
                size = newList.size();
                break;
            case 2:
                leaderboard.add("====================\n**Top 5 (" + newList.size() + ") CZ/SK Mobile Hracu (+ Nejvetsi uspechy)**\n");
                size = newList.size();
                break;
        }
        return withoutPoints(newList, leaderboard, size);
    }

    private static ArrayList<String> withPoints(ArrayList<ArrayList<String>> newList, ArrayList<String> leaderboard, int size) throws SQLException {
    	SQLConnect con = new SQLConnect();
    	int listSize = con.select("demon").size();
    	con.close();

    	ArrayList<ArrayList<String>> newNewList = new ArrayList<>(newList);
    	DecimalFormat df = new DecimalFormat("0.000");
    	for (int i = 0; i < newList.size(); i++) {
    		int playerComps = (newList.get(i).size() - 2) / 4;
    		for (int j = 0; j < playerComps; j++) {
    			String score = String.valueOf(df.format(Formula.formula(listSize, newList.get(i).get(5+j*5), newList.get(i).get(3+j*5), newList.get(i).get(4+j*5))));
    			newNewList.get(i).add(6+j*5, score);
    		}
    	}
        for (int i = 0; i < size; i++) {
            switch (newNewList.get(i).size()) {
                case 32:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s); '%s', %s %s%% (%s); '%s', %s %s%% (%s); '%s', %s %s%% (%s); '%s', %s %s%% (%s); '%s', %s %s%% (%s); '%s'",
                            i+1, newNewList.get(i).get(1),
                            newNewList.get(i).get(2), newNewList.get(i).get(3), newNewList.get(i).get(4), newNewList.get(i).get(6),
                            newNewList.get(i).get(7), newNewList.get(i).get(8), newNewList.get(i).get(9), newNewList.get(i).get(11), 
                            newNewList.get(i).get(12), newNewList.get(i).get(13), newNewList.get(i).get(14), newNewList.get(i).get(16),
                            newNewList.get(i).get(17), newNewList.get(i).get(18), newNewList.get(i).get(19), newNewList.get(i).get(21), 
                            newNewList.get(i).get(22), newNewList.get(i).get(23), newNewList.get(i).get(24), newNewList.get(i).get(26), 
                            newNewList.get(i).get(27), newNewList.get(i).get(28), newNewList.get(i).get(29), newNewList.get(i).get(31)));
                    break;
                case 27:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s); '%s', %s %s%% (%s); '%s', %s %s%% (%s); '%s', %s %s%% (%s); '%s', %s %s%% (%s); '%s'",
                            i+1, newNewList.get(i).get(1),
                            newNewList.get(i).get(2), newNewList.get(i).get(3), newNewList.get(i).get(4), newNewList.get(i).get(6),
                            newNewList.get(i).get(7), newNewList.get(i).get(8), newNewList.get(i).get(9), newNewList.get(i).get(11), 
                            newNewList.get(i).get(12), newNewList.get(i).get(13), newNewList.get(i).get(14), newNewList.get(i).get(16),
                            newNewList.get(i).get(17), newNewList.get(i).get(18), newNewList.get(i).get(19), newNewList.get(i).get(21), 
                            newNewList.get(i).get(22), newNewList.get(i).get(23), newNewList.get(i).get(24), newNewList.get(i).get(26)));
                    break;
                case 22:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s); '%s', %s %s%% (%s); '%s', %s %s%% (%s); '%s', %s %s%% (%s); '%s'",
                            i+1, newNewList.get(i).get(1),
                            newNewList.get(i).get(2), newNewList.get(i).get(3), newNewList.get(i).get(4), newNewList.get(i).get(6),
                            newNewList.get(i).get(7), newNewList.get(i).get(8), newNewList.get(i).get(9), newNewList.get(i).get(11), 
                            newNewList.get(i).get(12), newNewList.get(i).get(13), newNewList.get(i).get(14), newNewList.get(i).get(16),
                            newNewList.get(i).get(17), newNewList.get(i).get(18), newNewList.get(i).get(19), newNewList.get(i).get(21)));
                    break;
                case 17:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s); '%s', %s %s%% (%s); '%s', %s %s%% (%s); '%s'",
                            i+1, newNewList.get(i).get(1),
                            newNewList.get(i).get(2), newNewList.get(i).get(3), newNewList.get(i).get(4), newNewList.get(i).get(6),
                            newNewList.get(i).get(7), newNewList.get(i).get(8), newNewList.get(i).get(9), newNewList.get(i).get(11), 
                            newNewList.get(i).get(12), newNewList.get(i).get(13), newNewList.get(i).get(14), newNewList.get(i).get(16)));
                    break;
                case 12:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s); '%s', %s %s%% (%s); '%s'",
                            i+1, newNewList.get(i).get(1),
                            newNewList.get(i).get(2), newNewList.get(i).get(3), newNewList.get(i).get(4), newNewList.get(i).get(6),
                            newNewList.get(i).get(7), newNewList.get(i).get(8), newNewList.get(i).get(9), newNewList.get(i).get(11)));
                    break;
                case 7:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s); '%s'",
                            i+1, newNewList.get(i).get(1),
                            newNewList.get(i).get(2), newNewList.get(i).get(3), newNewList.get(i).get(4), newNewList.get(i).get(6)));
                    break;
            }
        }
    	return leaderboard;
    }

    private static ArrayList<String> withoutPoints(ArrayList<ArrayList<String>> newList, ArrayList<String> leaderboard, int size) {
        for (int i = 0; i < size; i++) {
            switch (newList.get(i).size()) {
                case 26:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s)",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4), newList.get(i).get(6), newList.get(i).get(7), newList.get(i).get(8),
                            newList.get(i).get(10), newList.get(i).get(11), newList.get(i).get(12), newList.get(i).get(14), newList.get(i).get(15), newList.get(i).get(16),
                            newList.get(i).get(18), newList.get(i).get(19), newList.get(i).get(20), newList.get(i).get(22), newList.get(i).get(23), newList.get(i).get(24)));
                    break;
                case 22:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s)",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4), newList.get(i).get(6), newList.get(i).get(7), newList.get(i).get(8),
                            newList.get(i).get(10), newList.get(i).get(11), newList.get(i).get(12), newList.get(i).get(14), newList.get(i).get(15), newList.get(i).get(16),
                            newList.get(i).get(18), newList.get(i).get(19), newList.get(i).get(20)));
                    break;
                case 18:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s), %s %s%% (%s), %s %s%% (%s), %s %s%% (%s))",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4), newList.get(i).get(6), newList.get(i).get(7), newList.get(i).get(8),
                            newList.get(i).get(10), newList.get(i).get(11), newList.get(i).get(12), newList.get(i).get(14), newList.get(i).get(15), newList.get(i).get(16)));
                    break;
                case 14:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s), %s %s%% (%s), %s %s%% (%s)",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4), newList.get(i).get(6), newList.get(i).get(7), newList.get(i).get(8),
                            newList.get(i).get(10), newList.get(i).get(11), newList.get(i).get(12)));
                    break;
                case 10:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s), %s %s%% (%s)",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4), newList.get(i).get(6), newList.get(i).get(7), newList.get(i).get(8)));
                    break;
                case 6:
                    leaderboard.add(String.format("**%s. %s** - %s %s%% (%s)",
                            i+1, newList.get(i).get(1),
                            newList.get(i).get(2), newList.get(i).get(3), newList.get(i).get(4)));
                    break;
            }
        }
    	return leaderboard;
    }

    public static ArrayList<ArrayList<String>> sortByRank(ArrayList<ArrayList<String>> list) {
        list.sort((o1, o2) -> {
            if (Float.parseFloat(o1.get(0)) < Float.parseFloat(o2.get(0))) return 1;
            else if (Float.parseFloat(o1.get(0)) > Float.parseFloat(o2.get(0))) return -1;
            return 0;
        });
        return list;
    }
}