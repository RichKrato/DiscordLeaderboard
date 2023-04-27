package cz.sspbrno.discord;

import cz.sspbrno.Config;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DiscordListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "add":
                Arrays.toString(Objects.requireNonNull(event.getMember()).
                        getRoles().toArray()).toLowerCase().contains("ceo of gmd");
                break;
        }
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        String arg = event.getMessage().getContentRaw().toString();
        String[] args = event.getMessage().getContentRaw().split(" ");
        boolean help = false;
        if (args.length > 1) help = args[1].equals("-h") || args[1].equals("--help");
        boolean botChannel = event.getChannel().toString().contains("bot");
        long id = event.getAuthor().getIdLong();

        try {
            if (Arrays.toString(Objects.requireNonNull(event.getMember()).
                    getRoles().toArray()).toLowerCase().contains("ceo of gmd")) {
                switch (args[0]) {
                    case DiscordCommands.prefix + "createlist" -> {
                        /**
                         * create list
                         */
                        System.out.println("create list");
                        if (!botChannel) {
                            clearMessage(event.getGuildChannel());
                        }
                        if (help) event.getChannel().sendMessage(DiscordCommands.createlistHelp).queue();
                        else try {
                            clearMessage(event.getGuildChannel());
                            if (Config.readIV("listCreated").equals("1")) {
                                event.getChannel().sendMessage("list exists, dumbass").queue();
                            }
                            DiscordCommands.createList();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case DiscordCommands.prefix + "updatelist" -> {
                        /**
                         * update list
                         */
                        if (!event.getChannel().toString().contains("sin-slavy")) {
                            clearMessage(event.getGuildChannel());
                        }
                        if (help) event.getChannel().sendMessage(DiscordCommands.updatelistHelp).queue();
                        else try {
                            clearMessages(event.getGuildChannel());
                            String[] list = DiscordCommands.updateList();
                            for (String s : list) event.getChannel().sendMessage(s).submit();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case DiscordCommands.prefix + "add" -> {
                        /**
                         * add
                         */
                        if (!botChannel) {
                            clearMessage(event.getGuildChannel());
                        }
                        if (help) event.getChannel().sendMessage(DiscordCommands.addHelp).queue();
                        else if (args[1].equals("-csv") || args[1].equals("--through-csv"))
                            try {
                                clearMessage(event.getGuildChannel());
                                ArrayList<String> csv;
                                if (new File("form_message.txt").isFile()) csv = readCSV("form_message.txt");
                                else csv = readCSV();
                                for (String s : csv)
                                    event.getChannel().sendMessage(DiscordCommands.prefix + "add " + s).queue();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        else try {
                                clearMessage(event.getGuildChannel());
                                DiscordCommands.add(args);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }
                    case DiscordCommands.prefix + "rename" -> {
                        /**
                         * rename
                         */
                        if (!botChannel) {
                            clearMessage(event.getGuildChannel());
                        }
                        if (help) event.getChannel().sendMessage(DiscordCommands.renameHelp).queue();
                        else try {
                            clearMessage(event.getGuildChannel());
                            DiscordCommands.rename(args);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case DiscordCommands.prefix + "update" -> {
                        /**
                         * update
                         */
                        if (!botChannel) {
                            clearMessage(event.getGuildChannel());
                        }
                        if (help) event.getChannel().sendMessage(DiscordCommands.updateHelp).queue();
                        else try {
                            clearMessage(event.getGuildChannel());
                            DiscordCommands.updateRecord(args);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case DiscordCommands.prefix + "remove" -> {
                        /**
                         * remove
                         */
                        if (!botChannel) {
                            clearMessage(event.getGuildChannel());
                        }
                        if (help) event.getChannel().sendMessage(DiscordCommands.removeHelp).queue();
                        else try {
                            clearMessage(event.getGuildChannel());
                            DiscordCommands.removeRecord(args);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (NullPointerException npe) { npe.getMessage(); }
        switch (args[0]) {
            /**
             * listofrecords
             */
            case DiscordCommands.prefix + "listofrecords" -> {
                if (!botChannel) {
                    clearMessage(event.getGuildChannel());
                    break;
                }
                if (help) event.getChannel().sendMessage(DiscordCommands.listofrecordsHelp).queue();
                else try {
                    event.getChannel().sendMessage(String.format("<@%s>\n%s", id, DiscordCommands.listOfRecords(args))).queue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            /**
             * listofplayers
             */
            case DiscordCommands.prefix + "listofplayers" -> {
                if (!botChannel) {
                    clearMessage(event.getGuildChannel());
                    break;
                }
                if (help) event.getChannel().sendMessage(DiscordCommands.listofplayersHelp).queue();
                else try {
                    event.getChannel().sendMessage(String.format("<@%s>\n%s", id, DiscordCommands.listOfPlayers())).queue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            /**
             * getposition
             */
            case DiscordCommands.prefix + "getposition" -> {
                if (!botChannel) {
                    clearMessage(event.getGuildChannel());
                    break;
                }
                if (help) event.getChannel().sendMessage(DiscordCommands.getpositionHelp).queue();
                else try {
                    event.getChannel().sendMessage(String.format("<@%s>\n%s", id, DiscordCommands.getPosition(args))).queue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            /**
             * estimatepos
             */
            case DiscordCommands.prefix + "estimatepos" -> {
                if (!botChannel) {
                    clearMessage(event.getGuildChannel());
                    break;
                }
                if (help) event.getChannel().sendMessage(DiscordCommands.estimateposHelp).queue();
                else try {
                    event.getChannel().sendMessage(String.format("<@%s>\n%s", id, DiscordCommands.estimatePos(args))).queue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            /**
             * commands
             */
            case DiscordCommands.prefix + "commands" -> {
                if (!botChannel) {
                    clearMessage(event.getGuildChannel());
                    break;
                }
                event.getChannel().sendMessage(String.format("<@%s>\n%s", id, DiscordCommands.commands())).queue();
            }
            /**
             * help
             */
            case DiscordCommands.prefix + "help" -> {
                if (!botChannel) {
                    clearMessage(event.getGuildChannel());
                    break;
                }
                try {
                    event.getChannel().sendMessage(Config.readINI().get(3)).queue();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void clearMessages(GuildMessageChannelUnion channel) {
        MessageHistory mh = channel.getHistory();
        while (true) {
            List<Message> list = mh.retrievePast(1).complete();
            String[] ID = list.toString().replace("(id=", "sgrsgrhst").replace(",", "sgrsgrhst").split("sgrsgrhst");
            if (list.isEmpty()) break;
            System.out.println(Arrays.toString(ID));
            channel.deleteMessageById(ID[1]).submit();
        }
    }

    private void clearMessage(GuildMessageChannelUnion channel) {
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
