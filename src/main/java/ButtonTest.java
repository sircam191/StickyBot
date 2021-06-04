import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.concurrent.TimeUnit;

public class ButtonTest extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        //split each word of text into array by spaces
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        //if bot sent the message return and do nothing
        if (event.getAuthor().isBot()) {
            return;
        }

        //sets prefix (? default, can be different for premium members)
        String prefix = "?";
        if (Main.mapPrefix.containsKey(event.getGuild().getId())) {
            prefix = Main.mapPrefix.get(event.getGuild().getId());
        }

        if (args[0].equalsIgnoreCase(prefix + "b") && event.getAuthor().getId().equals("182729649703485440")) {

            event.getChannel().sendMessage("Click a Button:")
                    .setActionRow(Button.primary("blue", "Blue"),
                            Button.success("green", "Green"),
                            Button.danger("red", "Red"),
                            Button.secondary("gray", "Gray")).queue();
        }
    }

    public void onButtonClick(ButtonClickEvent event) {
        if (event.getButton().getId().equals("blue")) {
            event.reply(event.getMember().getAsMention() + " Clicked The **Blue** Button!").queue(m -> m.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
        } else if (event.getButton().getId().equals("green")) {
            event.reply(event.getMember().getAsMention() + " Clicked The **Green** Button!").queue(m -> m.deleteOriginal().queueAfter(8, TimeUnit.SECONDS));
        } else if (event.getButton().getId().equals("red")) {
            event.reply(event.getMember().getAsMention() + " Clicked The **Red** Button!").queue(m -> m.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
        } else if (event.getButton().getId().equals("gray")) {
            event.reply(event.getMember().getAsMention() + " Clicked The **Gray** Button!").queue(m -> m.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
        }
    }
}
