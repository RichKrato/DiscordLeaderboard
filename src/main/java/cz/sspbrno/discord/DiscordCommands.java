package cz.sspbrno.discord;

import cz.sspbrno.leaderboards.Leaderboard;
import cz.sspbrno.leaderboards.Ranking;
import cz.sspbrno.sql.SQLConnect;
import cz.sspbrno.sql.SQLInit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DiscordCommands {
    public static final String prefix = "-/";
    private static final String[] commandsListAdmins = {"createlist", "updatelist", "add", "rename", "updaterecord", "removerecord"};
    private static final String[] commandsList = {"listofrecords", "listofplayers", "getposition", "estimatepos", "commands"};
    public static final String createlistHelp = "*"+prefix+"createlist*\n" +
            "creates the user, db and tables\n" +
            "usable only once";
    public static final String updatelistHelp = "*"+prefix+"updatelist*\n" +
            "updates the list\n" +
            "use in appropriate channel, as it deletes all messages";
    public static final String addHelp = "*"+prefix+"add Pakki 240hz sonic-wave 60/40-100/100 www.youtube.com/watch?v=RvjfmZNgnpI*\n" +
            "***Pakki*** - refers to a player name\n" +
            "***240hz*** - refers to a device level was played on\n" +
            "***sonic-wave*** - name of a demon on the list, all spaces must be replaced with a dash(-)\n" +
            "***60/40-100/100*** - refers to a progress done on the level (atleast 60%)\n" +
            "***www.youtube.com/watch?v=RvjfmZNgnpI*** - optional variable, link to video proof";
    public static final String renameHelp = "*"+prefix+"rename Bogen boge*\n" +
            "***Bogen*** - refers to current name on the leaderboard\n" +
            "***boge*** - refers to a new name to be updated on the leaderboard";
    public static final String updaterecordHelp = "*"+prefix+"updaterecord klouad infernal-abyss 100 www.youtube.com/watch?v=RvjfmZNgnpI*\n" +
            "***klouad*** - refers to a name the record belongs to\n" +
            "***infernal-abyss*** - refers to a level that it's record is getting updated, all spaces must be replaced with a dash(-)\n" +
            "***100*** - refers to a new record\n" +
            "***www.youtube.com/watch?v=RvjfmZNgnpI*** - optional variable, adds proof to a new record";
    public static final String removerecordHelp = "*"+prefix+"removerecord Trewis carnage-mode*\n" +
            "***Trewis*** - refers to a name of a player\n" +
            "***carnage-mode*** - refers to a level the record is getting deleted, all spaces must be replaced with a dash(-)\n";
    public static final String listofrecordsHelp = "*"+prefix+"listofrecords suni*\n" +
            "***suni*** - refers to the player's name\n" +
            "records are sorted by their list position\n" +
            "writes a list of records, sorted by the positions on the list";
    public static final String listofplayersHelp = "*"+prefix+"listofplayers*\n" +
            "displays a list of players in the list, sorted by the amount of records";
    public static final String getpositionHelp = "*"+prefix+"getposition Nidi*\n" +
            "***Nidi*** - name of the player\n" +
            "displays a position of player on the leaderboards";
    public static final String estimateposHelp = "*"+prefix+"estimatepos Lopaha 144hz betrayal-of-fate 100*\n" +
            "***Lopaha*** - name of the player\n" +
            "***144hz*** - device of the player\n" +
            "***betrayal-of-fate*** - name of the level\n" +
            "***100*** - percentage on the level\n" +
            "estimates the position on the leaderboards";
    public static void createList() throws IOException, SQLException {
        SQLInit con = new SQLInit();
    }

    public static String[] updateList() throws SQLException, IOException {
        SQLConnect con = new SQLConnect();
        con.update();
        con.close();
        DiscordOutput output = new DiscordOutput();
        int number = output.prepareAndSend().size();
        ArrayList<String> preList = output.prepareAndSend();
        String[] list = new String[number];
        for (int i = 0; i < number; i++) list[i] = preList.get(i);
        return list;
    }

    public static void add(String[] args) throws SQLException {
        SQLConnect con = new SQLConnect();
        con.insertIntoCompletionist(args[1], args[2], args[3], args[4]);
        con.close();
    }

    public static void rename(String[] args) throws SQLException {
        SQLConnect con = new SQLConnect();
        con.updatePlayerName(args[1], args[2]);
        con.close();
    }

    public static void updateRecord(String[] args) throws SQLException {
        SQLConnect con = new SQLConnect();
        con.updateRecord(args[1], args[2], args[3]);
        con.close();
    }

    public static void removeRecord(String[] args) throws SQLException {
        SQLConnect con = new SQLConnect();
        con.removeRecord(args[1], args[2]);
        con.close();
    }

    public static String listOfRecords(String[] args) throws SQLException, IOException {
        SQLConnect con = new SQLConnect();
        String message = con.listOfRecords(args[1]);
        con.close();
        return message;
    }

    public static String listOfPlayers() throws SQLException {
        SQLConnect con = new SQLConnect();
        String message = con.listOfPlayers();
        con.close();
        return message;
    }

    public static String getPosition(String[] args) throws SQLException, IOException {
        Leaderboard lead = new Leaderboard();
        ArrayList<ArrayList<String>> list = lead.sortByRank(lead.shrink(Ranking.playerRanking(0)));
        int pos = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get(1).equalsIgnoreCase(args[1])) pos = i+1;
        }
        if (pos == 0) return "Player doesn't exist on the list";
        return String.format("Position of the player **%s** on the list is: **%s**", args[1], pos);
    }

    public static String estimatePos(String[] args) throws IOException, SQLException {
        String[] toF = {args[1], args[2], args[3], args[4]};
        File f = new File("estimate.txt");
        Files.write(f.toPath(), Collections.singleton(String.join(" ", toF)));
        String[] estimation = getPosition(args).split("\\*\\*");
        Files.delete(f.toPath());
        return String.format("New possible position for the player **%s** on the list is: **%s**", estimation[1], estimation[3]);
    }

    public static String commands() {
        return String.format("**List of commands:**\n" +
                        "for admins -> ( *%s* )\n" +
                        "for everyone -> ( *%s* )\n" +
                        "use -h or --help after a command for help",
                String.join(", ", commandsListAdmins), String.join(", ", commandsList));
    }
}