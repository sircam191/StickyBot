import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

public class UrbanDict extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        String prefix = "?";

        if (event.getAuthor().isBot()) {
            return;
        }

        if (Main.mapPrefix.containsKey(event.getGuild().getId())) {
            prefix = Main.mapPrefix.get(event.getGuild().getId());
        }

        if (args[0].equalsIgnoreCase(prefix + "urban")) {


            if (!event.getChannel().isNSFW()) {
                event.getMessage().reply("Sorry! This command can only be used in NSFW channels.").queue();
                return;
            }

            //if args not given let user know
            if (event.getMessage().getContentRaw().equalsIgnoreCase(prefix + "urban")){
                event.getMessage().reply("You need to provide something to lookup!\nExample: `" + prefix + "urban Banana`").queue();
                return;
            }

            String lookup = event.getMessage().getContentRaw().replace(prefix + "urban", "");

            try {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://mashape-community-urban-dictionary.p.rapidapi.com/define?term=" + lookup)
                        .get()
                        .addHeader("x-rapidapi-key", "**********************")
                        .addHeader("x-rapidapi-host", "mashape-community-urban-dictionary.p.rapidapi.com")
                        .build();

                Response response = client.newCall(request).execute();

                String data = response.body().string();
                System.out.println(data);

                JsonObject jsonObject = new JsonParser().parse(data.trim()).getAsJsonObject().get("list").getAsJsonArray().get(0).getAsJsonObject();


                EmbedBuilder emb = new EmbedBuilder();
                emb.setColor(Color.ORANGE)
                        .setTitle("-Urban Dictionary Lookup-")
                        .setFooter("Used by " + event.getMember().getUser().getAsTag(), event.getMember().getUser().getEffectiveAvatarUrl())
                        .setDescription("Result for: **" + StringUtils.capitalize(lookup.trim()) + "**")
                        .addField("Definition:", StringUtils.capitalize(jsonObject.get("definition").getAsString()),false)
                        .addField("Example:", StringUtils.capitalize(jsonObject.get("example").getAsString()), false)
                        .addField("Likes:", "\uD83D\uDC4D " + jsonObject.get("thumbs_up").getAsString(), true)
                        .addField("Dislikes:", "\uD83D\uDC4E " + jsonObject.get("thumbs_down").getAsString(), true)
                        .addField("Author:", jsonObject.get("author").getAsString(), false);

                event.getMessage().replyEmbeds(emb.build()).setActionRow(Button.link(jsonObject.get("permalink").getAsString(), "Full Page").withEmoji(Emoji.fromMarkdown("<:ud:862918399675727893>"))).queue();
                response.close();
            } catch (Exception e) {
                event.getMessage().reply("Whoops! Something went wrong. Make sure you provide provide something to lookup!\nExample: `" + prefix + "urban Banana`").queue();
                e.printStackTrace();
            }
        }
    }
}
