package cz.sspbrno.discord;

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
        String[] args = event.getMessage().getContentRaw().split(" ");
        boolean help = false;
        if (args.length > 1) help = args[1].equals("-h") || args[1].equals("--help");
        boolean botChannel = event.getChannel().toString().contains("bot");
        long id = event.getAuthor().getIdLong();

        int number = strToInt(args[0]);
        if (Arrays.toString(Arrays.toString(Objects.requireNonNull(event.getMember()).getRoles().toArray())
                .replace("(", ":").split(":")).toLowerCase().contains("list admin"))
            switch (number) {
                /**
                 * createlist
                 */
                case 1164:
                    if (!botChannel) {
                        clearMessage(event.getChannel());
                        break;
                    }
                    if (help) event.getChannel().sendMessage(DiscordCommands.createlistHelp).queue();
                    else try {
                        clearMessage(event.getChannel());
                        DiscordCommands.createList();
                    } catch (Exception e) { e.printStackTrace(); }
                    break;
                /**
                 * updatelist
                 */
                case 1179:
                    if (!event.getChannel().toString().contains("sin-slavy")) {
                        clearMessage(event.getChannel());
                        break;
                    }
                    if (help) event.getChannel().sendMessage(DiscordCommands.updatelistHelp).queue();
                    else try {
                        clearMessages(event.getChannel());
                        String[] list = DiscordCommands.updateList();
                        for (String s : list) event.getChannel().sendMessage(s).queue();
                    } catch (Exception e) { e.printStackTrace(); }
                    break;
                /**
                 * add
                 */
                case 389:
                    if (!botChannel) {
                        clearMessage(event.getChannel());
                        break;
                    }
                    if (help) event.getChannel().sendMessage(DiscordCommands.addHelp).queue();
                    else if (args[1].equals("-csv") || args[1].equals("--through-csv"))
                        try {
                            clearMessage(event.getChannel());
                            ArrayList<String> csv = readCSV();
                            for (String s : csv) event.getChannel().sendMessage(DiscordCommands.prefix + "add " + s).queue();
                        } catch (Exception e) { e.printStackTrace(); }
                    else try {
                        clearMessage(event.getChannel());
                        DiscordCommands.add(args, args.length);
                    } catch (Exception e) { e.printStackTrace(); }
                    break;
                /**
                 * rename
                 */
                case 724:
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
                case 1374:
                    if (!botChannel) {
                        clearMessage(event.getChannel());
                        break;
                    }
                    if (help) event.getChannel().sendMessage(DiscordCommands.updaterecordHelp).queue();
                    else try {
                        clearMessage(event.getChannel());
                        DiscordCommands.updateRecord(args, args.length);
                    } catch (Exception e) { e.printStackTrace(); }
                    break;
                /**
                 * removerecord
                 */
                case 1385:
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
                /**
                 * addproof
                 */
                case 939:
                    if (!botChannel) {
                        clearMessage(event.getChannel());
                        break;
                    }
                    if (help) event.getChannel().sendMessage(DiscordCommands.addproofHelp).queue();
                    else try {
                        clearMessage(event.getChannel());
                        DiscordCommands.addProof(args);
                    } catch (Exception e) { e.printStackTrace(); }
                    break;
            }
        switch (number) {
            /**
             * getproof
             */
            case 962:
                if (!botChannel) {
                    clearMessage(event.getChannel());
                    break;
                }
                if (help) event.getChannel().sendMessage(DiscordCommands.getproofHelp).queue();
                else try {
                    event.getChannel().sendMessage(String.format("<@%s> %s", id, DiscordCommands.getProof(args))).queue();
                } catch (Exception e) { e.printStackTrace(); }
                break;
            /**
             * listofrecords
             */
            case 1503:
                if (!botChannel) {
                    clearMessage(event.getChannel());
                    break;
                }
                if (help) event.getChannel().sendMessage(DiscordCommands.listofrecordsHelp).queue();
                else try {
                    event.getChannel().sendMessage(String.format("<@%s> %s", id, DiscordCommands.listOfRecords(args))).queue();
                } catch (Exception e) { e.printStackTrace(); }
                break;
            /**
             * listofplayers
             */
            case 1517:
                if (!botChannel) {
                    clearMessage(event.getChannel());
                    break;
                }
                if (help) event.getChannel().sendMessage(DiscordCommands.listofplayersHelp).queue();
                else try {
                    event.getChannel().sendMessage(String.format("<@%s> %s", id, DiscordCommands.listOfPlayers())).queue();
                } catch (Exception e) { e.printStackTrace(); }
                break;
            /**
             * commands
             */
            case 942:
                if (!botChannel) {
                    clearMessage(event.getChannel());
                    break;
                }
                event.getChannel().sendMessage(String.format("<@%s> %s", id, DiscordCommands.commands())).queue();
                break;
        }
    }

    private void clearMessages(TextChannel channel) {
        MessageHistory mh = channel.getHistory();
        while (true) {
            List<Message> list = mh.retrievePast(1).complete();
            String[] ID = list.toString().replace("(", "sgrsgrhst").replace(")", "sgrsgrhst").split("sgrsgrhst");
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

    private int strToInt(String message) {
        int number = 0;
        byte[] messageByte = message.getBytes();
        for (byte b : messageByte) number += b;
        return number;
    }
}
