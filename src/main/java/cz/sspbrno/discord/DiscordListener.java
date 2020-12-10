package cz.sspbrno.discord;

import cz.sspbrno.Config;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DiscordListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        boolean help = false;
        if (args.length > 1) help = args[1].equals("-h") || args[1].equals("--help");
        boolean botChannel = event.getChannel().toString().contains("bot");
        long id = event.getAuthor().getIdLong();

        try {
            if (Arrays.toString(Arrays.toString(Objects.requireNonNull(event.getMember()).getRoles().toArray())
                    .replace("(", ":").split(":")).toLowerCase().contains("ceo of gmd"))
                switch (args[0]) {
                    /**
                     * createlist
                     */
                    case DiscordCommands.prefix + "createlist":
                        if (!botChannel) {
                            clearMessage(event.getChannel());
                            break;
                        }
                        if (help) event.getChannel().sendMessage(DiscordCommands.createlistHelp).queue();
                        else try {
                            clearMessage(event.getChannel());
                            if (Config.readIV("listCreated").equals("1")) {
                                event.getChannel().sendMessage("list exists, dumbass").queue();
                                break;
                            }
                            DiscordCommands.createList();
                        } catch (Exception e) { e.printStackTrace(); }
                        break;
                    /**
                     * updatelist
                     */
                    case DiscordCommands.prefix + "updatelist":
                        if (!event.getChannel().toString().contains("sin-slavy")) {
                            clearMessage(event.getChannel());
                            break;
                        }
                        if (help) event.getChannel().sendMessage(DiscordCommands.updatelistHelp).queue();
                        else try {
                            clearMessages(event.getChannel());
                            String[] list = DiscordCommands.updateList();
                            for (String s : list) event.getChannel().sendMessage(s).submit();
                        } catch (Exception e) { e.printStackTrace(); }
                        break;
                    /**
                     * add
                     */
                    case DiscordCommands.prefix + "add":
                        if (!botChannel) {
                            clearMessage(event.getChannel());
                            break;
                        }
                        if (help) event.getChannel().sendMessage(DiscordCommands.addHelp).queue();
                        else if (args[1].equals("-csv") || args[1].equals("--through-csv"))
                            try {
                                clearMessage(event.getChannel());
                                ArrayList<String> csv;
                                if (new File("form_message.txt").isFile()) csv = readCSV("form_message.txt");
                                else csv = readCSV();
                                for (String s : csv) event.getChannel().sendMessage(DiscordCommands.prefix + "add " + s).queue();
                            } catch (Exception e) { e.printStackTrace(); }
                        else try {
                                clearMessage(event.getChannel());
                                DiscordCommands.add(args);
                            } catch (Exception e) { e.printStackTrace(); }
                        break;
                    /**
                     * rename
                     */
                    case DiscordCommands.prefix + "rename":
                        if (!botChannel) {
                            clearMessage(event.getChannel());
                            break;
                        }
                        if (help) event.getChannel().sendMessage(DiscordCommands.renameHelp).queue();
                        else try {
                            clearMessage(event.getChannel());
                            DiscordCommands.rename(args);
                        } catch (Exception e) { e.printStackTrace(); }
                        break;
                    /**
                     * updaterecord
                     */
                    case DiscordCommands.prefix + "updaterecord":
                        if (!botChannel) {
                            clearMessage(event.getChannel());
                            break;
                        }
                        if (help) event.getChannel().sendMessage(DiscordCommands.updaterecordHelp).queue();
                        else try {
                            clearMessage(event.getChannel());
                            DiscordCommands.updateRecord(args);
                        } catch (Exception e) { e.printStackTrace(); }
                        break;
                    /**
                     * removerecord
                     */
                    case DiscordCommands.prefix + "removerecord":
                        if (!botChannel) {
                            clearMessage(event.getChannel());
                            break;
                        }
                        if (help) event.getChannel().sendMessage(DiscordCommands.removerecordHelp).queue();
                        else try {
                            clearMessage(event.getChannel());
                            DiscordCommands.removeRecord(args);
                        } catch (Exception e) { e.printStackTrace(); }
                        break;
                }
        } catch (NullPointerException npe) { npe.getMessage(); }
        switch (args[0]) {
            /**
             * listofrecords
             */
            case DiscordCommands.prefix + "listofrecords":
                if (!botChannel) {
                    clearMessage(event.getChannel());
                    break;
                }
                if (help) event.getChannel().sendMessage(DiscordCommands.listofrecordsHelp).queue();
                else try {
                    event.getChannel().sendMessage(String.format("<@%s>\n%s", id, DiscordCommands.listOfRecords(args))).queue();
                } catch (Exception e) { e.printStackTrace(); }
                break;
            /**
             * listofplayers
             */
            case DiscordCommands.prefix + "listofplayers":
                if (!botChannel) {
                    clearMessage(event.getChannel());
                    break;
                }
                if (help) event.getChannel().sendMessage(DiscordCommands.listofplayersHelp).queue();
                else try {
                    event.getChannel().sendMessage(String.format("<@%s>\n%s", id, DiscordCommands.listOfPlayers())).queue();
                } catch (Exception e) { e.printStackTrace(); }
                break;
            /**
             * getposition
             */
            case DiscordCommands.prefix + "getposition":
                if (!botChannel) {
                    clearMessage(event.getChannel());
                    break;
                }
                if (help) event.getChannel().sendMessage(DiscordCommands.getpositionHelp).queue();
                else try {
                    event.getChannel().sendMessage(String.format("<@%s>\n%s", id, DiscordCommands.getPosition(args))).queue();
                } catch (Exception e) { e.printStackTrace(); }
                break;
            /**
             * estimatepos
             */
            case DiscordCommands.prefix + "estimatepos":
                if (!botChannel) {
                    clearMessage(event.getChannel());
                    break;
                }
                if (help) event.getChannel().sendMessage(DiscordCommands.estimateposHelp).queue();
                else try {
                    event.getChannel().sendMessage(String.format("<@%s>\n%s", id, DiscordCommands.estimatePos(args))).queue();
                } catch (Exception e) { e.printStackTrace(); }
                break;
            /**
             * commands
             */
            case DiscordCommands.prefix + "commands":
                if (!botChannel) {
                    clearMessage(event.getChannel());
                    break;
                }
                event.getChannel().sendMessage(String.format("<@%s>\n%s", id, DiscordCommands.commands())).queue();
                break;
            /**
             * help
             */
            case DiscordCommands.prefix + "help":
                if (!botChannel) {
                    clearMessage(event.getChannel());
                    break;
                }
                try {
                    event.getChannel().sendMessage(Config.readINI().get(3)).queue();
                } catch (IOException e) { e.printStackTrace(); }
                break;
        }
    }

    private void clearMessages(TextChannel channel) {
        MessageHistory mh = channel.getHistory();
        while (true) {
            List<Message> list = mh.retrievePast(1).complete();
            String[] ID = list.toString().replace("(", "sgrsgrhst").replace(")", "sgrsgrhst").split("sgrsgrhst");
            if (list.isEmpty()) break;
            channel.deleteMessageById(ID[ID.length-2]).submit();
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

    private ArrayList<String> readCSV(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        ArrayList<String> al = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            al.add(line);
            line = br.readLine();
        }
        return al;
    }
}
