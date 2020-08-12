package cz.sspbrno.leaderboards;

import cz.sspbrno.sql.SQLConnect;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Ranking {

    public static ArrayList<String[]> playerRanking() throws SQLException {
        SQLConnect con = new SQLConnect();
        List<String[]> players = con.getPlayers();
        ArrayList<String[]> playersRanked = new ArrayList<>();
        float rank;

        for (String[] strings : players) {
            ArrayList<String[]> records = con.getRecords(strings[0]);
            String[] player = new String[26];
            String[][] hundred = new String[records.size() + 2][5];
            String[][] notHundred = new String[records.size() + 2][5];
            rank = 0;
            for (int j = 0; j < records.size() + 1; j++) {
                try {
                    if (records.get(j)[2].equals("100")) hundred[j] = records.get(j);
                    else notHundred[j] = records.get(j);
                } catch (IndexOutOfBoundsException ioobe) {
                    hundred = shrink(hundred);
                    notHundred = shrink(notHundred);
                    int count = 2;
                    player[0] = String.valueOf(rank);
                    player[1] = records.get(j - 1)[0];
                    for (int k = 0; k < 3; k++) {
                        player[count] = hundred[k][1];
                        player[count + 1] = hundred[k][2];
                        player[count + 2] = hundred[k][3];
                        player[count + 3] = hundred[k][4];
                        player[count + 12] = notHundred[k][1];
                        player[count + 13] = notHundred[k][2];
                        player[count + 14] = notHundred[k][3];
                        player[count + 15] = notHundred[k][4];
                        count += 4;
                    }
                    playersRanked.add(player);
                }
            }
        }
        con.close();
        return playersRanked;
    }

    private static String[][] shrink(String[][] list) {
        String[][] newList = new String[3][5];
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
}
