package cz.sspbrno.discord;

import cz.sspbrno.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class DiscordConnect {

	public static void connect() throws LoginException {
		JDA jda = JDABuilder.createLight(Config.discord_token)
				.enableIntents(GatewayIntent.MESSAGE_CONTENT)
				.addEventListeners(new DiscordListener())
				.setActivity(Activity.competing("in biggest dumpy competition"))
				.build();
		jda.updateCommands().addCommands(
				Commands.slash("createlist","creates the user, db and tables")
						.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
						.setGuildOnly(true),
				Commands.slash("updatelist","updates the list")
						.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
						.setGuildOnly(true),
				Commands.slash("add","add Pakki 240hz sonic-wave 60/40-100/100 www.youtube.com/watch?v=RvjfmZNgnpI")
						.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
						.setGuildOnly(true)
						.addOption(OptionType.STRING, "player", "xddholder", true)
						.addOption(OptionType.STRING, "refresh-rate", "hz or fps", true)
						.addOption(OptionType.STRING, "level", "lowercase, dash sign for space", true)
						.addOption(OptionType.STRING, "progress", "intervals without percent", true)
						.addOption(OptionType.STRING, "proof", "video"),
				Commands.slash("rename","rename Bogen boge")
						.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
						.setGuildOnly(true)
						.addOption(OptionType.STRING, "old-name", "self explanatory", true)
						.addOption(OptionType.STRING, "new-name", "yes", true),
				Commands.slash("update","update klouad infernal-abyss 100 www.youtube.com/watch?v=RvjfmZNgnpI")
						.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
						.setGuildOnly(true)
						.addOption(OptionType.STRING, "player", "xddholder", true)
						.addOption(OptionType.STRING, "refresh-rate", "hz or fps", true)
						.addOption(OptionType.STRING, "level", "lowercase, dash sign for space", true)
						.addOption(OptionType.STRING, "progress", "intervals without percent", true)
						.addOption(OptionType.STRING, "proof", "video"),
				Commands.slash("remove","remove Trewis carnage-mode")
						.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
						.setGuildOnly(true)
						.addOption(OptionType.STRING, "player", "xddholder", true)
						.addOption(OptionType.STRING, "level", "lowercase, dash sign for space", true),
				Commands.slash("listofrecords","listofrecords suni")
						.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_SEND))
						.setGuildOnly(true)
						.addOption(OptionType.STRING, "player", "xddholder", true),
				Commands.slash("listofplayers","listofplayers")
						.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_SEND))
						.setGuildOnly(true),
				Commands.slash("getpos","getposition Nidi")
						.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_SEND))
						.setGuildOnly(true)
						.addOption(OptionType.STRING, "player", "xddholder", true),
				Commands.slash("estimatepos","estimatepos Lopaha 144hz betrayal-of-fate 100")
						.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_SEND))
						.setGuildOnly(true)
						.addOption(OptionType.STRING, "player", "xddholder", true)
						.addOption(OptionType.STRING, "refresh-rate", "hz or fps", true)
						.addOption(OptionType.STRING, "level", "lowercase, dash sign for space", true)
						.addOption(OptionType.STRING, "progress", "intervals without percent", true)
		).queue();
	}
}