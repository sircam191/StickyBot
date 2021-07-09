import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.IOException;

public class LoveCalc extends ListenerAdapter
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

        if (args[0].equalsIgnoreCase(prefix + "love")) {

            //if args not given let user know
            if (event.getMessage().getContentRaw().equalsIgnoreCase(prefix + "love")){
                event.getMessage().reply("You need to provide two names!\nExample: `" + prefix + "love Alice, Sam`").queue();
                return;
            }



            String rawInput = event.getMessage().getContentRaw().replace(prefix + "love", "");

            String[] names = rawInput.split("\\s*,\\s*");


            OkHttpClient client = new OkHttpClient();



            try {
                Request request = new Request.Builder()
                        .url("https://love-calculator.p.rapidapi.com/getPercentage?fname=" + names[0].trim() + "&sname=" + names[1])
                        .get()
                        .addHeader("x-rapidapi-key", "**************")
                        .addHeader("x-rapidapi-host", "love-calculator.p.rapidapi.com")
                        .build();

                Response response = client.newCall(request).execute();

                String data = response.body().string();
                System.out.println(data);

                JsonObject jsonObject = new JsonParser().parse(data.trim()).getAsJsonObject();

                EmbedBuilder emb = new EmbedBuilder();
                emb.setColor(Color.ORANGE)
                        .setTitle("-Compatibility Check-")
                        .setFooter("Used by " + event.getMember().getUser().getAsTag(), event.getMember().getUser().getEffectiveAvatarUrl())
                        .setDescription(StringUtils.capitalize(names[0].trim()) + " & " + StringUtils.capitalize(names[1]))
                        .addField("Compatibility Percentage:", "`" + jsonObject.get("percentage").getAsInt() + "%`",false)
                        .addField("Message:", jsonObject.get("result").getAsString(),false);

                event.getMessage().reply(emb.build()).queue();
                response.close();
            } catch (Exception e) {
                event.getMessage().reply("Whoops! Something went wrong. Make sure you provide two names.\nExample: `" + prefix + "love Alice, Sam`").queue();
                e.printStackTrace();
            }
        }
    }
}
