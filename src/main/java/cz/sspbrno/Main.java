package cz.sspbrno;

import cz.sspbrno.discord.DiscordConnect;
import cz.sspbrno.discord.DiscordOutput;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws LoginException, SQLException, IOException {
    	DiscordConnect.connect();
    }
}