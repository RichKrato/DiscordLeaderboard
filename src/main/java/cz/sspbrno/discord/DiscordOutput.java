package cz.sspbrno.discord;

import cz.sspbrno.Config;
import cz.sspbrno.leaderboards.Leaderboard;
import cz.sspbrno.sql.SQLConnect;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiscordOutput {
    private SQLConnect con;

    public DiscordOutput() throws SQLException {
        this.con = new SQLConnect();
    }

    public ArrayList<String> prepareAndSend() throws SQLException, IOException {
        Leaderboard lead = new Leaderboard();
        ArrayList<String[]> db_demon = new ArrayList<>(con.select("demon"));
        ArrayList<String[]> db_completionist = new ArrayList<>(con.select("completionist"));
        String[][] unfilteredList = new String[db_completionist.size()][3];
        ArrayList<String[][]> mid = new ArrayList<>();
        for (int i = 0; i < db_completionist.size(); i++) {
            for (String[] strings : db_demon) {
                if ((db_completionist.get(i)[3].replace("-", " ").toLowerCase()).equals(strings[0].toLowerCase())) {
                    String[] a = {db_completionist.get(i)[0], strings[2], strings[0]};
                    unfilteredList[i] = a;
                    if (unfilteredList[i][1].equals(strings[2]) && db_completionist.get(i)[0].equals(unfilteredList[i][0])) {
                        String[][] messageList = new String[2][3];
                        messageList[0] = String.format("%s %s - %s**",
                                strings[2], strings[0], strings[1]).split(" ");
                        messageList[1] = String.format("%s %s%% (%s),",
                                db_completionist.get(i)[1], db_completionist.get(i)[4], db_completionist.get(i)[2]).split(" ");
                        mid.add(messageList);
                    }
                }
            }
        }

        sortMultiple(mid);

        ArrayList<String> lb = new ArrayList<>();
        lb.add("**Dokoncene Top 150 (+ legacy) demony z CZ/SK (+ progressy nad 60%)**\n\n");
        for (int i = 0; i < mid.size(); i++) {
            for (int j = 0; j < mid.get(i).length; j++) {
                String[] a = new String[2];
                a[0] = String.join(" ", mid.get(i)[0]);
                a[1] = String.join(" ", mid.get(i)[1]);
                if (j%2==0) a[0] = "**#"+a[0];
                lb.add(a[j]);
            }
        }

        ArrayList<String> listFinal = new ArrayList<>();
        for (int i = 0; i < lb.size(); i++) {
            if (i != 0) {
                if (!(lb.get(i).startsWith("*") && listFinal.contains(lb.get(i)))) {
                    if (lb.get(i).endsWith(",") && listFinal.get(listFinal.size()-1).endsWith(",")) {
                        listFinal.set(listFinal.size()-1, listFinal.get(listFinal.size()-1)+ " " +lb.get(i));
                    } else listFinal.add(lb.get(i));
                }
            } else listFinal.add(lb.get(i));
        }
        listFinal.addAll(lead.top20());
        listFinal.addAll(creators());
        listFinal.addAll(lead.top60hz());
        listFinal.addAll(lead.topMobile());
        return trimSize(listFinal);
    }

    public ArrayList<String> trimSize(ArrayList<String> ar) {
        ArrayList<String> list = new ArrayList<>();
        int count = 0;
        if (ar.size()%10 == 0) {
            for (int i = 1; i <= ar.size()/10; i++) {
                String[] lol = new String[10];
                for (int j = 0; j < 10; j++) {
                    lol[j] = ar.get(i*10+j-10);
                    count++;
                }
                list.add(String.join("\n", lol));
            }
        } else {
            for (int i = 1; i <= ar.size()/10+1; i++) {
                String[] lol = new String[10];
                for (int j = 0; j < 10; j++) {
                    try {
                        lol[j] = ar.get(i*10+j-10);
                    } catch (IndexOutOfBoundsException ioobe) { lol[j] = ""; }
                }
                list.add(String.join("\n", lol));
            }
        }
        return list;
    }

    private void sortMultiple(ArrayList<String[][]> mid) {
        mid.sort((o1, o2) -> {
            String a1 = o1[1][2].split("[(),]")[1].replace("hz", "0").replace("fps", "1");
            String a2 = o2[1][2].split("[(),]")[1].replace("hz", "0").replace("fps", "1");
            if (a1.equals("mobile") && a2.equals("mobile")) return 0;
            else if (a1.equals("mobile")) return -1;
            else if (a2.equals("mobile")) return 1;
            return Integer.compare(Integer.parseInt(a1), Integer.parseInt(a2));
        });
        mid.sort((o1, o2) -> {
            String[] a1 = o1[1][1].split("[%-]");
            String[] a2 = o2[1][1].split("[%-]");
            if (a1.length == 1 || a2.length == 1) {
                if (a1.length == 1 && a2.length == 2) return Integer.compare(Integer.parseInt(a2[1]) - Integer.parseInt(a2[0]), Integer.parseInt(a1[0]));
                else if (a2.length == 1 && a1.length == 2) return Integer.compare(Integer.parseInt(a2[0]), Integer.parseInt(a1[1]) - Integer.parseInt(a1[0]));
                else return Integer.compare(Integer.parseInt(a2[0]), Integer.parseInt(a1[0]));
            }
            else return Integer.compare(Integer.parseInt(a2[1]) - Integer.parseInt(a2[0]), Integer.parseInt(a1[1]) - Integer.parseInt(a1[0]));
        });
        mid.sort((o1, o2) -> {
            if (Integer.parseInt(o1[0][0]) < Integer.parseInt(o2[0][0])) return -1;
            else if (Integer.parseInt(o1[0][0]) > Integer.parseInt(o2[0][0])) return 1;
            return 0;
        });
    }

    private static final String creatorsPath = "creators.txt";

    private ArrayList<String> creators() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        List<String> creators = Config.readFile(creatorsPath);
        list.add("====================\n**Top " + creators.size() + " CZ/SK Creatoru (Podle CP)**\n");
        for (String s : creators) {
            String[] creator = s.split(" ");
            list.add(String.format("**%s %s %s** %s %s %s", creator[0], creator[1], creator[2], creator[3], creator[4], creator[5]));
        }
        return list;
    }
}
