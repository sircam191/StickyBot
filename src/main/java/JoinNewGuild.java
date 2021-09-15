import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import org.discordbots.api.client.DiscordBotListAPI;

import java.awt.*;
import java.text.NumberFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class JoinNewGuild extends ListenerAdapter {
    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        Member stickyBot = event.getGuild().getMemberById(Main.botId);

        EmbedBuilder em = new EmbedBuilder();

        em.setTitle("StickyBot Joined a New Server!");
        em.addField("Server Name: ", event.getGuild().getName(), false);
        em.addField("Server ID", event.getGuild().getId(), false);
        em.addField("Guild Members: ", NumberFormat.getInstance().format(event.getGuild().retrieveMetaData().complete().getApproximateMembers()), false);
        em.addField("Guild Region: ",  event.getGuild().getRegion().getEmoji() + " " + event.getGuild().getRegion().getName(), false);
        em.addField("Guild Owner Tag", event.getGuild().retrieveOwner().complete().getAsMention(), false);
        em.addField("Guild Owner Raw", event.getGuild().retrieveOwner().complete().getEffectiveName() + "#" + event.getGuild().retrieveOwner().complete().getUser().getDiscriminator(), false);
        em.addField("Guild Owner ID", event.getGuild().retrieveOwner().complete().getId(), false);
        em.addField("Time", OffsetDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)) + " PST", false);
        em.setFooter("StickyBot is now in " + NumberFormat.getInstance().format(Main.jda.getGuildCache().size()) + " guilds", stickyBot.getUser().getEffectiveAvatarUrl());
        em.setThumbnail(event.getGuild().getIconUrl());
        em.setColor(Color.GREEN);

        Main.jda.getTextChannelById("643974985446326272").sendMessage(em.build()).queue();

        //DM server owner info
        event.getGuild().retrieveOwner().queue((u) -> {
            u.getUser().openPrivateChannel().queue((channel) ->
            {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(Color.yellow);
                eb.setTitle("**Thank You For Adding StickyBot To Your Server!**");
                eb.setDescription("Here are the basics to get you started:");
                eb.addField("Note:", "The stickied message is sent every 5 messages or 15 seconds to comply with discord TOS.", false);
                eb.addField("**Commands:** ", "Do ``?commands`` or ``?help``", false);
                eb.addField("Support Server:", "*Bot support & giveaways!*\n[StickyBot Support](https://discord.gg/SvNQTtf)", false);
                eb.addField("Vote/Review StickyBot:", "[top.gg/StickyBot](https://top.gg/bot/628400349979344919)", false);
                eb.addField("Issues?", "Make sure the bot has perms to send and delete messages and is immune to Slow Mode in the channel.\nJoin the support server with any other issues you may encounter.", false);
                eb.addField("Website:","[www.stickybot.info](https://www.stickybot.info/)", false);
                eb.addField("Premium: ", "-Unlimited Stickied Messages."  +
                        "\n-Use Custom Embeds as Stickies." +
                        "\n-Create a sticky embed with a custom name and profile pic." +
                        "\n-Custom Prefix." +
                        "\n-Removes \"Stickied Message:\" header." +
                        "\n-Slower Posting Stickies." +
                        "\n-Premium support." +
                        "\n-More to come!" +
                        "\n**Learn More** [**Here**](https://www.stickybot.info/premium)", false);
                eb.setFooter("StickyBot", Main.jda.getShards().get(0).getSelfUser().getAvatarUrl());
                channel.sendMessageEmbeds(eb.build()).setActionRows(
                        ActionRow.of(Button.link("https://www.stickybot.info", "Website").withEmoji(Emoji.fromMarkdown("<:StickyBotCircle:693004145065590856>")),
                                Button.link("https://discord.com/invite/SvNQTtf", "Support Server").withEmoji(Emoji.fromMarkdown("<:discordEmote:853160010305765376>"))),
                        ActionRow.of(Button.link("https://www.stickybot.info/premium", "Premium").withEmoji(Emoji.fromMarkdown("\uD83E\uDDE1")),
                                Button.link("https://docs.stickybot.info", "Docs").withEmoji(Emoji.fromMarkdown("<:iBlue:860060995979706389>")))

                ).queue();
            });
        });

        int serverCount = (int) Main.jda.getGuildCache().size();
        Main.topggAPI.setStats(serverCount);

    }
}
