package cz.sspbrno.leaderboards;

import cz.sspbrno.sql.SQLConnect;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ranking {

    public static ArrayList<String[]> playerRanking(int type) throws SQLException, IOException {
        SQLConnect con = new SQLConnect();
        List<String[]> players = con.getPlayers(1);
        ArrayList<String[]> playersRanked = new ArrayList<>();
        for (String[] strings : players) {
			ArrayList<String[]> records = con.getRecords(strings[0]);
			String[] player = new String[26];
			String[][] hundred = new String[records.size() + 2][5];
			String[][] notHundred = new String[records.size() + 2][5];
			for (int j = 0; j < records.size() + 1; j++) {
				try {
					switch (type) {
						case 0:
							if (records.get(j)[2].equals("100")) hundred[j] = records.get(j);
							else notHundred[j] = records.get(j);
							break;
						case 1:
							if (records.get(j)[3].equals("60hz")) {
								if (records.get(j)[2].equals("100")) hundred[j] = records.get(j);
								else notHundred[j] = records.get(j);
							}
							break;
						case 2:
							if (records.get(j)[3].equals("mobile")) {
								if (records.get(j)[2].equals("100")) hundred[j] = records.get(j);
								else notHundred[j] = records.get(j);
							}
							break;
					}
				} catch (IndexOutOfBoundsException ioobe) {
					player[1] = records.get(j - 1)[0];
					int listSize = con.select("demon").size();
					ArrayList<Object[]> comparison = new ArrayList<>();
					comparison.add(whichIsHigher(player, hundred, 4, notHundred, 2, listSize).toArray());
					comparison.add(whichIsHigher(player, hundred, 5, notHundred, 1, listSize).toArray());
					comparison.add(whichIsHigher(player, hundred, 6, notHundred, 0, listSize).toArray());
					int indexHigher = 0;
					double highestRank = 0;
					for (int k = 0; k < 3; k++) {
						for (int l = 0; l < 3; l++) {
							if (k == l) continue;
							double compared = Double.parseDouble((String) comparison.get(l % 3)[0]);
							if (highestRank < compared) {
								highestRank = compared;
								indexHigher = l;
							}
						}
					}
					String[] playerNew = Arrays.copyOf(comparison.get(indexHigher), comparison.get(indexHigher).length, String[].class);
					playersRanked.add(playerNew);
				}
			}
        }
        con.close();
        return playersRanked;
    }

    private static ArrayList<String> whichIsHigher(String[] player, String[][] hundred, int hundredSize, String[][] notHundred, int notHundredSize, int listSize) {
        hundred = shrink(hundred, hundredSize);
        notHundred = shrink(notHundred, notHundredSize);
        int count = 2;
        for (int k = 0; k < hundredSize; k++) {
            player[count] = hundred[k][1];
            player[count + 1] = hundred[k][2];
            player[count + 2] = hundred[k][3];
            player[count + 3] = hundred[k][4];
            count += 4;
        }
        for (int k = 0; k < notHundredSize; k++) {
            player[count] = notHundred[k][1];
            player[count + 1] = notHundred[k][2];
            player[count + 2] = notHundred[k][3];
            player[count + 3] = notHundred[k][4];
            count += 4;
        }
        player[0] = String.valueOf(Weighting.getRank(player, listSize));
        return shrink(player);
    }

    private static String[][] shrink(String[][] list, int size) {
        String[][] newList = new String[size][5];
        int count = 0;
        for (int i = 0; i < list.length-2; i++)
            if (list[i][0] != null) {
                try {
                    newList[count] = list[i];
                    count++;
                } catch (ArrayIndexOutOfBoundsException aioobe) { break; }
            }
        return newList;
    }

    private static ArrayList<String> shrink(String[] list) {
        ArrayList<String> newList = new ArrayList<>();
        for (String s : list) {
            if (s != null) newList.add(s);
        }
        return newList;
    }
}
