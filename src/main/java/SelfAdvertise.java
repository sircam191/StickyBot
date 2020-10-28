import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.discordbots.api.client.DiscordBotListAPI;

public class SelfAdvertise extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        if(event.getAuthor().isBot()) {
            return;
        }

        if (event.getChannel().getId().equals("771095262126669854")) {
            DiscordBotListAPI api = new DiscordBotListAPI.Builder()
                    .token(Main.topggAPIToken)
                    .botId(Main.botId)
                    .build();

            String userId = event.getAuthor().getId();
            api.hasVoted(userId).whenComplete((hasVoted, e) -> {
                if(hasVoted) {
                   event.getMessage().addReaction("StickyBotSpinning2:736470214090424351").queue();
                } else {
                    event.getMessage().delete().queue();
                }
            });
        }
    }
}
