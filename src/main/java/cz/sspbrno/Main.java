package cz.sspbrno;

import cz.sspbrno.discord.DiscordCommands;
import cz.sspbrno.discord.DiscordConnect;
import cz.sspbrno.discord.DiscordOutput;
import cz.sspbrno.html.ParseLoader;
import cz.sspbrno.leaderboards.Leaderboard;
import cz.sspbrno.sql.SQLConnect;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws LoginException, IOException, SQLException {
        DiscordConnect discord = new DiscordConnect();
    }
}