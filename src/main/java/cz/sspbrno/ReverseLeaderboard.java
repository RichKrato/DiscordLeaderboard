package cz.sspbrno;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReverseLeaderboard {
    public static void reverse() throws IOException {
        /**
         * Output: Pakki 240hz sonic-wave 60
         */
        List<String> f = Config.readFile("message.txt");

        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> demons = getDemons(f);
        ArrayList<String[]> players = getPlayers(f);
        ArrayList<String[]> hz = getHz(f);
        ArrayList<String[]> percent = getPercent(f);

        for (int i = 0; i < demons.size(); i++) {
            for (int j = 0; j < players.get(i).length; j++) {
                StringBuilder out = new StringBuilder();
                out.append(players.get(i)[j]).append(" ");
                out.append(hz.get(i)[j]).append(" ");
                out.append(demons.get(i)).append(" ");
                out.append(percent.get(i)[j]);
                output.add(out.toString());
            }
        }

        File formattedF = new File("form_message.txt");
        Files.write(Paths.get(formattedF.getPath()), output);
        for (String s : output) System.out.println(s);
    }

    private static ArrayList<String> getDemons(List<String> f) {
        ArrayList<String> demons = new ArrayList<>();
        for (String s : f) {
            if (s.startsWith("#")) {
                StringBuilder demon = new StringBuilder();
                int len = 1;
                String[] arr = s.split(" ");
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i].equals("-")) {
                        len = i;
                        break;
                    }
                }
                if (len == 1) demon.append(arr[len]);
                for (int i = 1; i < len; i++) {
                    demon.append("-");
                    demon.append(arr[i].toLowerCase());
                }
                String name = demon.toString();
                demons.add(name.substring(1));
            }
        }
        return demons;
    }

    private static ArrayList<String[]> getPlayers(List<String> f) {
        ArrayList<String[]> players = new ArrayList<>();
        for (String s : f) {
            if (!s.startsWith("#")) {
                String[] demonPlayers = s.split(", ");
                String[] playerNames = new String[demonPlayers.length];
                for (int i = 0; i < demonPlayers.length; i++) {
                    String[] demonPlayer = demonPlayers[i].split(" ");
                    playerNames[i] = demonPlayer[0];
                }
                players.add(playerNames);
            }
        }
        return players;
    }

    private static ArrayList<String[]> getHz(List<String> f) {
        ArrayList<String[]> hz = new ArrayList<>();
        for (String s : f) {
            if (!s.startsWith("#")) {
                String[] demonHzs = s.split(", ");
                String[] playerHzs = new String[demonHzs.length];
                for (int i = 0; i < demonHzs.length; i++) {
                    String[] demonHz = demonHzs[i].split(" ");
                    playerHzs[i] = demonHz[2].replace("(", ")").split("\\)")[1];
                }
                hz.add(playerHzs);
            }
        }
        return hz;
    }

    private static ArrayList<String[]> getPercent(List<String> f) {
        ArrayList<String[]> percent = new ArrayList<>();
        for (String s : f) {
            if (!s.startsWith("#")) {
                String[] demonPercs = s.split(", ");
                String[] playerPercs = new String[demonPercs.length];
                for (int i = 0; i < demonPercs.length; i++) {
                    String[] demonPerc = demonPercs[i].split(" ");
                    playerPercs[i] = demonPerc[1].replace("%", "");
                }
                percent.add(playerPercs);
            }
        }
        return percent;
    }
}
