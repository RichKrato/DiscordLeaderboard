package cz.sspbrno.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import javax.security.auth.login.LoginException;
import cz.sspbrno.Config;

public class DiscordConnect {

    public DiscordConnect() throws LoginException {
        JDA bot = JDABuilder.createDefault(Config.discord_token)
                .setActivity(Activity.playing(Config.discord_activity))
                .addEventListeners(new DiscordListener())
                .build();
    }
}
