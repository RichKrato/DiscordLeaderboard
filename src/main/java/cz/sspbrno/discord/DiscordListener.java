package cz.sspbrno.discord;

import cz.sspbrno.sql.SQLConnect;
import cz.sspbrno.sql.SQLInit;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DiscordListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String prefix = "-/";
        String[] args = event.getMessage().getContentRaw().split(" ");
        String[] commandsListAdmins = {"createlist", "updatelist", "add", "rename", "updaterecord", "removerecord", "addproof"};
        String[] commandsList = {"getproof", "listofrecords", "listofplayers"};

        if (args[0].equals(prefix + "getproof")) {
            try {
                if (args[1].equals("-h") || args[1].equals("--help")) {
                    event.getChannel().sendMessage("*-/getproof Pakki sonic-wave*\n" +
                            "***Pakki*** - refers to the player's name\n" +
                            "***sonic-wave*** - refers to the player's accomplishment, all spaces must be replaced with a dash(-)\n" +
                            "links proof to the completion, if available").queue();
                } else {
                    try {
                        SQLConnect con = new SQLConnect();
                        try {
                            event.getChannel().sendMessage(con.getProof(args[1], args[2])).queue();
                        } catch (IllegalArgumentException e) { event.getChannel().sendMessage("neexistuje :slight_smile: ").queue(); }
                        con.close();
                    } catch (Exception e) { event.getChannel().sendMessage("noo").queue(); }
                }
            } catch (ArrayIndexOutOfBoundsException aioobe) { event.getChannel().sendMessage("noo").queue(); }
        } else if (args[0].equals(prefix + "listofrecords")) {
            try {
                if (args[1].equals("-h") || args[1].equals("--help")) {
                    event.getChannel().sendMessage("*-/listofrecords suni*\n" +
                            "***suni*** - refers to the player's name\n" +
                            "records are sorted by their list position\n" +
                            "writes a list of records, sorted by the positions on the list").queue();
                } else {
                    try {
                        SQLConnect con = new SQLConnect();
                        event.getChannel().sendMessage(con.listOfRecords(args[1])).queue();
                        con.close();
                    } catch (Exception e) { event.getChannel().sendMessage("noo").queue(); }
                }
            } catch (ArrayIndexOutOfBoundsException aioobe) { event.getChannel().sendMessage("noo").queue(); }
        } else if (args[0].equals(prefix + "listofplayers")) {
            try {
                if (args[1].equals("-h") || args[1].equals("--help")) {
                    event.getChannel().sendMessage("*-/listofplayers*\n" +
                            "displays a list of players in the list, sorted by the amount of records").queue();
                }
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                try {
                    SQLConnect con = new SQLConnect();
                    event.getChannel().sendMessage(con.listOfPlayers()).queue();
                    con.close();
                } catch (Exception e) { event.getChannel().sendMessage("noo").queue(); }
            }
        } else if (args[0].equals(prefix + "commands")) {
            String message = String.format("**List of commands:**\n" +
                            "for admins -> ( *%s* )\n" +
                            "for everyone -> ( *%s* )\n" +
                            "use -h or --help after a command for help",
                    String.join(", ", commandsListAdmins),
                    String.join(", ", commandsList));
            event.getChannel().sendMessage(message).queue();
        } else if (Arrays.toString(Arrays.toString(Objects.requireNonNull(event.getMember()).getRoles().toArray())
                .replace("(", ":").split(":")).toLowerCase().contains("list admin")) {
            if (args[0].equals(prefix + "createlist")) {
                try {
                    if (args[1].equals("-h") || args[1].equals("--help")) {
                        event.getChannel().sendMessage("*-/createlist*\n" +
                                "creates the user, db and tables\n" +
                                "usable only once").queue();
                    }
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    try {
                        clearMessage(event.getChannel());
                        SQLInit con = new SQLInit();
                    } catch (Exception e) { event.getChannel().sendMessage("noo").queue(); }
                }
            } else if (args[0].equals(prefix + "updatelist")) {
                try {
                    if (args[1].equals("-h") || args[1].equals("--help")) {
                        event.getChannel().sendMessage("*-/updatelist*\n" +
                                "updates the list\n" +
                                "use in appropriate channel, as it deletes all messages").queue();
                    }
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    try {
                        clearMessages(event.getChannel());
                        SQLConnect con = new SQLConnect();
                        DiscordOutput output = new DiscordOutput();
                        con.update();
                        for (int i = 0; i < output.prepareAndSend().size(); i++)
                            event.getChannel().sendMessage(output.prepareAndSend().get(i)).queue();
                        con.close();
                    } catch (Exception e) { event.getChannel().sendMessage("noo").queue(); }
                }
            } else if (args[0].equals(prefix + "add")) {
                try {
                    if (args[1].equals("-h") || args[1].equals("--help")) {
                        event.getChannel().sendMessage("*-/add Pakki 240hz sonic-wave 60/40-100/100 www.youtube.com/watch?v=RvjfmZNgnpI\"*\n" +
                                "***Pakki*** - refers to a player name\n" +
                                "***240hz*** - refers to a device level was played on\n" +
                                "***sonic-wave*** - name of a demon on the list, all spaces must be replaced with a dash(-)\n" +
                                "***60/40-100/100*** - refers to a progress done on the level (atleast 60%)\n" +
                                "***www.youtube.com/watch?v=RvjfmZNgnpI*** - optional variable, link to video proof").queue();
                    } else if (args[1].equals("-csv") || args[1].equals("--through-csv")) {
                        for (int i = 0; i < readCSV().size(); i++) {
                            event.getChannel().sendMessage(prefix + "add " + readCSV().get(i)).queue();
                        }
                    } else if (args.length == 5) {
                        try {
                            clearMessage(event.getChannel());
                            SQLConnect con = new SQLConnect();
                            con.insertIntoCompletionist(args[1], args[2], args[3], args[4]);
                            con.close();
                        } catch (Exception e) { event.getChannel().sendMessage("noo").queue(); }
                    } else if (args.length == 6) {
                        try {
                            clearMessage(event.getChannel());
                            SQLConnect con = new SQLConnect();
                            con.insertIntoCompletionist(args[1], args[2], args[3], args[4], args[5]);
                            con.close();
                        } catch (Exception e) { event.getChannel().sendMessage("noo").queue(); }
                    }
                } catch (ArrayIndexOutOfBoundsException | IOException aioob_ioe) { event.getChannel().sendMessage("noo").queue(); }
            } else if (args[0].equals(prefix + "rename")) {
                try {
                    if (args[1].equals("-h") || args[1].equals("--help")) {
                        event.getChannel().sendMessage("*-/rename Bogen boge*\n" +
                                "***Bogen*** - refers to current name on the leaderboard\n" +
                                "***boge*** - refers to a new name to be updated on the leaderboard").queue();
                    } else {
                        try {
                            clearMessage(event.getChannel());
                            SQLConnect con = new SQLConnect();
                            con.updatePlayerName(args[1], args[2]);
                            con.close();
                        } catch (Exception e) { event.getChannel().sendMessage("noo").queue(); }
                    }
                } catch (ArrayIndexOutOfBoundsException aioobe) { event.getChannel().sendMessage("noo").queue(); }
            } else if (args[0].equals(prefix + "updaterecord")) {
                try {
                    if (args[1].equals("-h") || args[1].equals("--help")) {
                        event.getChannel().sendMessage("*-/updaterecord klouad infernal-abyss 100 www.youtube.com/watch?v=RvjfmZNgnpI*\n" +
                                "***klouad*** - refers to a name the record belongs to\n" +
                                "***infernal-abyss*** - refers to a level that it's record is getting updated, all spaces must be replaced with a dash(-)\n" +
                                "***100*** - refers to a new record\n" +
                                "***www.youtube.com/watch?v=RvjfmZNgnpI*** - optional variable, adds proof to a new record").queue();
                    } else if (args.length == 4) {
                        clearMessage(event.getChannel());
                        try {
                            clearMessage(event.getChannel());
                            SQLConnect con = new SQLConnect();
                            con.updateRecord(args[1], args[2], args[3]);
                            con.close();
                        } catch (Exception e) { event.getChannel().sendMessage("noo").queue(); }
                    } else {
                        clearMessage(event.getChannel());
                        try {
                            clearMessage(event.getChannel());
                            SQLConnect con = new SQLConnect();
                            con.updateRecord(args[1], args[2], args[3], args[4]);
                            con.close();
                        } catch (Exception e) { event.getChannel().sendMessage("noo").queue(); }
                    }
                } catch (ArrayIndexOutOfBoundsException aioobe) { event.getChannel().sendMessage("noo").queue(); }

            } else if (args[0].equals(prefix + "addproof")) {
                try {
                    if (args[1].equals("-h") || args[1].equals("--help")) {
                        event.getChannel().sendMessage("*-/addproof klouad ithacropolis www.youtube.com/watch?v=RvjfmZNgnpI*\n" +
                                "***klouad*** - refers to a name proof's being given to\n" +
                                "***ithacropolis*** - refers to a level the proof is on, all spaces must be replaced with a dash(-)\n" +
                                "***www.youtube.com/watch?v=RvjfmZNgnpI*** - the actual proof").queue();
                    } else {
                        try {
                            clearMessage(event.getChannel());
                            SQLConnect con = new SQLConnect();
                            con.addProof(args[1], args[2], args[3]);
                            con.close();
                        } catch (Exception e) { event.getChannel().sendMessage("noo").queue(); }
                    }
                } catch (ArrayIndexOutOfBoundsException aioobe) { event.getChannel().sendMessage("noo").queue(); }
            } else if (args[0].equals(prefix + "removerecord")) {
                try {
                    if (args[1].equals("-h") || args[1].equals("--help")) {
                        event.getChannel().sendMessage("*-/removerecord Trewis carnage-mode*\n" +
                                "***Trewis*** - refers to a name of a player\n" +
                                "***carnage-mode*** - refers to a level the record is getting deleted, all spaces must be replaced with a dash(-)\n").queue();
                    } else {
                        try {
                            clearMessage(event.getChannel());
                            SQLConnect con = new SQLConnect();
                            con.removeRecord(args[1], args[2]);
                            con.close();
                        } catch (Exception e) { event.getChannel().sendMessage("noo").queue(); }
                    }
                } catch (ArrayIndexOutOfBoundsException aioobe) { event.getChannel().sendMessage("noo").queue(); }
            }
        }
    }

    private void clearMessages(TextChannel channel) {
        MessageHistory mh = channel.getHistory();
        while (true) {
            List<Message> list = mh.retrievePast(1).complete();
            String[] ID = list.toString().replace("(", "sgrsgrhst")
                    .replace(")", "sgrsgrhst").split("sgrsgrhst");
            if (list.isEmpty()) break;
            channel.deleteMessageById(ID[ID.length-2]).queue();
        }
    }

    private void clearMessage(TextChannel channel) {
        channel.deleteMessageById(channel.getLatestMessageIdLong()).queue();
    }

    private ArrayList<String> readCSV() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("../../Documents/list.csv"));
        ArrayList<String> al = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            al.add(line);
            line = br.readLine();
        }
        return al;
    }
}
