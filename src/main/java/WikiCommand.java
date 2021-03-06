import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import net.explodingbush.ksoftapi.entities.Wikihow;

import java.awt.*;

public class WikiCommand extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        if (event.getAuthor().isBot()) {
            return;
        }

        if (Main.mapDisable.containsKey(event.getGuild().getId())) {
            return;
        }

        String[] args = event.getMessage().getContentRaw().split("\\s+");
        String prefix = "?";


        if (Main.mapPrefix.containsKey(event.getGuild().getId())) {
            prefix = Main.mapPrefix.get(event.getGuild().getId());
        }

        if (args[0].equalsIgnoreCase(prefix + "wikihow")) {

          Wikihow wiki = Main.kSoftApi.getRandomWikihow().execute();

            EmbedBuilder em = new EmbedBuilder();

            em.setTitle("Random WikiHow Article", wiki.getArticleUrl())
            .setDescription(wiki.getTitle())
            .setThumbnail(wiki.getImage())
            .setColor(Color.yellow)
            .setFooter("Random Wiki requested by " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator() + " Source: KSoft.Si API");
            event.getMessage().reply(em.build()).setActionRow(Button.link(wiki.getArticleUrl(), "Full Article")).queue();
        }
    }
}
