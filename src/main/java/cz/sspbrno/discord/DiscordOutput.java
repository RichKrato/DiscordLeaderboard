package cz.sspbrno.discord;

import cz.sspbrno.sql.SQLConnect;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class DiscordOutput {
    private SQLConnect con;

    public DiscordOutput() throws SQLException {
        this.con = new SQLConnect();
    }

    public ArrayList<String> prepareAndSend() throws SQLException {
        ArrayList<String[]> db_demon = new ArrayList<>(con.select("demon"));
        ArrayList<String[]> db_completionist = new ArrayList<>(con.select("completionist"));
        ArrayList<String[]> unfilteredList = new ArrayList<>();
        ArrayList<String[][]> mid = new ArrayList<>();
        ArrayList<String> lb = new ArrayList<>();
        lb.add("**Dokoncene Top 150 (+ legacy) demony z CZ/SK (+ progressy nad 60%)**\n\n");
        for (int i = 0; i < db_completionist.size(); i++) {
            for (int j = 0; j < db_demon.size(); j++) {
                if (db_completionist.get(i)[3].replace("-", " ").equalsIgnoreCase(db_demon.get(j)[0].toLowerCase())) {
                    String[] a = new String[3];
                    a[0] = db_completionist.get(i)[0];
                    a[1] = db_demon.get(j)[2];
                    a[2] = db_demon.get(j)[0];
                    unfilteredList.add(a);
                }
            }
        }
        for (int i = 0; i < unfilteredList.size(); i++) {
            for (int j = 0; j < db_demon.size(); j++) {
                if (unfilteredList.get(i)[1].equals(db_demon.get(j)[2]) &&
                        db_completionist.get(i)[0].equals(unfilteredList.get(i)[0])) {
                    String[][] messageList = new String[2][];
                    messageList[0] = String.format("%s %s - %s**",
                            db_demon.get(j)[2],
                            db_demon.get(j)[0],
                            db_demon.get(j)[1]).split(" ");
                    messageList[1] = String.format("%s %s%% (%s),",
                            db_completionist.get(i)[1],
                            db_completionist.get(i)[4],
                            db_completionist.get(i)[2]).split(" ");
                    mid.add(messageList);
                }
            }
        }
        mid.sort((o1, o2) -> {
            if (o1[1][2].split("[(),]")[1].equals("mobile")) return -1;
            else if (o2[1][2].split("[(),]")[1].equals("mobile")) return 1;
            return Integer.compare(
                    Integer.parseInt(o1[1][2].replace("hz", "0").replace("fps", "1").split("[(),]")[1]),
                    Integer.parseInt(o2[1][2].replace("hz", "0").replace("fps", "1").split("[(),]")[1]));
        });
        mid.sort((o1, o2) -> {
            String[] a1 = o1[1][1].split("[%-]");
            String[] a2 = o2[1][1].split("[%-]");
            if (a1.length == 1 || a2.length == 1) {
                if (a1.length == 1 && a2.length == 2) {
                    if (Integer.parseInt(a2[1]) - Integer.parseInt(a2[0]) < Integer.parseInt(a1[0])) return -1;
                    else return 1;
                } else if (a2.length == 1 && a1.length == 2) {
                    if (Integer.parseInt(a2[0]) < Integer.parseInt(a1[1]) - Integer.parseInt(a1[0])) return -1;
                    else return 1;
                } else {
                    if (Integer.parseInt(a2[0]) < Integer.parseInt(a1[0])) return -1;
                    else return 1;
                }
            } else if (Integer.parseInt(a2[1]) - Integer.parseInt(a2[0]) < Integer.parseInt(a1[1]) - Integer.parseInt(a1[0])) return -1;
            else return 1;
        });
        mid.sort((o1, o2) -> {
            if (Integer.parseInt(o1[0][0]) < Integer.parseInt(o2[0][0])) return -1;
            else if (Integer.parseInt(o1[0][0]) > Integer.parseInt(o2[0][0])) return 1;
            return 0;
        });
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
        return listFinal;
    }
}
