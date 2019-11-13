import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

public class JoinNewGuild extends ListenerAdapter {
    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        Member stickyBot = event.getGuild().getMemberById(Main.botId);
        Guild stickySupportServer = event.getJDA().getGuildById("641158383138897941");

        event.getGuild().getDefaultChannel().sendMessage("Thanks for adding " + stickyBot.getAsMention() + "!\n-Use **?help** for a list of commands.").queue();

        EmbedBuilder em = new EmbedBuilder();

        em.setTitle("StickyBOT Joined a new server!");
        em.addField("Server Name: ", event.getGuild().getName(), false);
        em.addField("Guild Members: ", String.valueOf(event.getGuild().getMembers().size()), false);
        em.addField("Guild Region: ",  event.getGuild().getRegion().getEmoji() + " " + event.getGuild().getRegion().getName(), false);
        em.addField("Guild Owner", event.getGuild().getOwner().getAsMention(), false);
        em.setFooter("StickyBot is now in " + String.valueOf(event.getJDA().getGuilds().size()) + " guilds", stickyBot.getUser().getEffectiveAvatarUrl());
        em.setThumbnail(event.getGuild().getIconUrl());
        em.setColor(Color.GREEN);

        stickySupportServer.getTextChannelById("643974985446326272").sendMessage(em.build()).queue();

    }

}