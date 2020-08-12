package cz.sspbrno.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import javax.security.auth.login.LoginException;
import cz.sspbrno.Config;

public class DiscordConnect {
    private JDA bot;

    public DiscordConnect() throws LoginException {
        this.bot = new JDABuilder(Config.discord_token)
                .setActivity(Activity.playing(Config.discord_activity))
                .addEventListeners(new DiscordListener())
                .build();
    }
}
