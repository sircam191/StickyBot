import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

public class ImageRecon extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        String prefix = "?";

        if (event.getAuthor().isBot()) {
            return;
        }

        if (Main.mapDisable.containsKey(event.getGuild().getId()) && !event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            return;
        }

        if (Main.mapPrefix.containsKey(event.getGuild().getId())) {
            prefix = Main.mapPrefix.get(event.getGuild().getId());
        }

        if (args[0].equalsIgnoreCase(prefix + "image")) {

            //if args not given let user know
            if (event.getMessage().getContentRaw().equalsIgnoreCase(prefix + "image")){
                event.getMessage().reply(event.getMember().getAsMention() + " You need to provide a image link.").queue();
                return;
            }

            String imageLink = event.getMessage().getContentRaw().replace(prefix + "image", "");



            OkHttpClient client = new OkHttpClient();

            try {
                Main.jda.getTextChannelById("863173304735367178").sendMessage("**IMAGE RECOGNITION COMMAND:**\nLink: `" + imageLink.trim() + "`\nSERVER: `" + event.getGuild().getId() + "`").queue();
                System.out.println("OLD:" + imageLink);
                String imageLink2 = imageLink.replace(":", "%3A").replace("/", "%2F").trim();
                System.out.println("NEW:" + imageLink2);

                Request request = new Request.Builder()
                        .url("https://everypixel-api.p.rapidapi.com/keywords?url=" + imageLink2)
                        .get()
                        .addHeader("x-rapidapi-host", "everypixel-api.p.rapidapi.com")
                        .addHeader("x-rapidapi-key", Main.rapidAPIToken)
                        .build();

                Response response = client.newCall(request).execute();

                String data = response.body().string();
                System.out.println("Image Recognition lookup: " + data);

                JsonObject jsonObject = new JsonParser().parse(data.trim()).getAsJsonObject();

                //x = x.substring(0, 4) + "." + x.substring(4, x.length());
                String percent1 = jsonObject.get("keywords").getAsJsonArray().get(0).getAsJsonObject().get("score").getAsString();
                int resultCount = jsonObject.get("keywords").getAsJsonArray().size();

                EmbedBuilder em = new EmbedBuilder();


                if (resultCount > 3) {
                    em.addField(jsonObject.get("keywords").getAsJsonArray().get(0).getAsJsonObject().get("keyword").getAsString().toUpperCase(),
                            "Confidence: " + Math.round(((jsonObject.get("keywords").getAsJsonArray().get(0).getAsJsonObject().get("score").getAsDouble() * 100) * 100.0)) /100.0 + "%", false);
                    em.addField(jsonObject.get("keywords").getAsJsonArray().get(1).getAsJsonObject().get("keyword").getAsString().toUpperCase(),
                            "Confidence: " + Math.round(((jsonObject.get("keywords").getAsJsonArray().get(1).getAsJsonObject().get("score").getAsDouble() * 100) * 100.0)) /100.0 + "%", false);
                    em.addField(jsonObject.get("keywords").getAsJsonArray().get(2).getAsJsonObject().get("keyword").getAsString().toUpperCase(),
                            "Confidence: " + Math.round(((jsonObject.get("keywords").getAsJsonArray().get(2).getAsJsonObject().get("score").getAsDouble() * 100) * 100.0)) /100.0 + "%", false);
                    em.addField(jsonObject.get("keywords").getAsJsonArray().get(3).getAsJsonObject().get("keyword").getAsString().toUpperCase(),
                            "Confidence: " + Math.round(((jsonObject.get("keywords").getAsJsonArray().get(3).getAsJsonObject().get("score").getAsDouble() * 100) * 100.0)) /100.0 + "%", false);
                } else if (resultCount == 3) {
                    em.addField(jsonObject.get("keywords").getAsJsonArray().get(0).getAsJsonObject().get("keyword").getAsString().toUpperCase(),
                            "Confidence: " + Math.round(((jsonObject.get("keywords").getAsJsonArray().get(0).getAsJsonObject().get("score").getAsDouble() * 100) * 100.0)) /100.0 + "%", false);
                    em.addField(jsonObject.get("keywords").getAsJsonArray().get(1).getAsJsonObject().get("keyword").getAsString().toUpperCase(),
                            "Confidence: " + Math.round(((jsonObject.get("keywords").getAsJsonArray().get(1).getAsJsonObject().get("score").getAsDouble() * 100) * 100.0)) /100.0 + "%", false);
                    em.addField(jsonObject.get("keywords").getAsJsonArray().get(2).getAsJsonObject().get("keyword").getAsString().toUpperCase(),
                            "Confidence: " + Math.round(((jsonObject.get("keywords").getAsJsonArray().get(2).getAsJsonObject().get("score").getAsDouble() * 100) * 100.0)) /100.0 + "%", false);
                } else if (resultCount == 2) {
                    em.addField(jsonObject.get("keywords").getAsJsonArray().get(0).getAsJsonObject().get("keyword").getAsString().toUpperCase(),
                            "Confidence: " + Math.round(((jsonObject.get("keywords").getAsJsonArray().get(0).getAsJsonObject().get("score").getAsDouble() * 100) * 100.0)) /100.0 + "%", false);
                    em.addField(jsonObject.get("keywords").getAsJsonArray().get(1).getAsJsonObject().get("keyword").getAsString().toUpperCase(),
                            "Confidence: " + Math.round(((jsonObject.get("keywords").getAsJsonArray().get(1).getAsJsonObject().get("score").getAsDouble() * 100) * 100.0)) /100.0 + "%", false);
                }else if (resultCount == 1) {
                    em.addField(jsonObject.get("keywords").getAsJsonArray().get(0).getAsJsonObject().get("keyword").getAsString().toUpperCase(),
                            "Confidence: " + Math.round(((jsonObject.get("keywords").getAsJsonArray().get(0).getAsJsonObject().get("score").getAsDouble() * 100) * 100.0)) /100.0 + "%", false);
                } else {
                    event.getMessage().reply(event.getMember().getAsMention() + " Darn! The AI was not able to get any keywords for this image.\nFeel free to try a different image.").queue();
                            return;
                }

                        em.setColor(Color.ORANGE)
                        .setThumbnail(imageLink)
                        .setFooter("Source: EveryPixel API", "https://www.programmableweb.com/sites/default/files/Everypixel%20Oauth%20API%20Image.jpg")
                        .setTitle("-Image Recognition-");


                event.getMessage().replyEmbeds(em.build()).queue();

                response.close();
            } catch (Exception e) {
                event.getMessage().reply(event.getMember().getAsMention() + "Whoops! Something went wrong.\nPlease make sure you provide a image link or try a different link.\n*(You may also be sending requests too quickly)*").queue();
                e.printStackTrace();
            }
        }
    }
}
