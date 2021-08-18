import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.awt.*;

public class RollCommand extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        String prefix = "?";

        if(event.getAuthor().isBot()) {
            return;
        }

        if (Main.mapDisable.containsKey(event.getGuild().getId()) && !event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            return;
        }

        if(Main.mapPrefix.containsKey(event.getGuild().getId())) {
            prefix = Main.mapPrefix.get(event.getGuild().getId());
        }

        if (args[0].equalsIgnoreCase(prefix + "dice") || args[0].equalsIgnoreCase(prefix + "roll")) {
            event.getMessage().reply(RollDice(event.getMember()).build()).setActionRow(Button.primary("rollAgain", "Roll Again!").withEmoji(Emoji.fromMarkdown("<:grey_dice:853159207708917771>"))).queue();
           }
        }


    public void onButtonClick(ButtonClickEvent event) {
        if (event.getButton().getId().equals("rollAgain")) {
            event.replyEmbeds(RollDice(event.getMember()).build()).addActionRow(Button.primary("rollAgain", "Roll Again!").withEmoji(Emoji.fromMarkdown("<:grey_dice:853159207708917771>"))).queue();
        }

    }
        public EmbedBuilder RollDice(Member member) {
            String[] emotes = {"<:d1:831426766782201886>",
                    "<:d2:831426778505281577>",
                    "<:d3:831426786726248449>",
                    "<:d4:831426794551902248>",
                    "<:d5:831426802249105429>",
                    "<:d6:831426811254669322>"};

            int dice1 = (int) (Math.random() * 6 + 1);
            int dice2 = (int) (Math.random() * 6 + 1);
            EmbedBuilder emb = new EmbedBuilder();

            emb.setTitle("-Rolling Dice-");
            emb.setDescription("Dice 1: " + emotes[dice1 - 1] + " **" + (dice1) + "** " +
                    "\nDice 2: " + emotes[dice2 - 1] + " **" + (dice2) + "** " +
                    "\n\n**TOTAL: " + (dice1 + dice2) + "**");

            emb.setColor(Color.orange);
            emb.setThumbnail("https://studio.code.org/v3/assets/GBhvGLEcbJGFHdJfHkChqw/8TEb9oxGc.gif");
            emb.setFooter("Rolled by: " + member.getEffectiveName(), member.getUser().getAvatarUrl());

            return emb;

        }


}
