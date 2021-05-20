import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.eclipse.jetty.websocket.common.io.http.HttpResponseHeaderParser;
import org.fastily.jwiki.core.NS;
import org.fastily.jwiki.core.Wiki;

import java.awt.*;
import java.io.IOException;

public class WikiCommands extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        //split each word of text into array by spaces
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        //if bot sent the message return and do nothing
        if(event.getAuthor().isBot()) {
            return;
        }

        //sets prefix (? default, can be different for premium members)
        String prefix = "?";
        if(Main.mapPrefix.containsKey(event.getGuild().getId())) {
            prefix = Main.mapPrefix.get(event.getGuild().getId());
        }


        if (args[0].equalsIgnoreCase(prefix + "wiki")) {

            if (args.length == 1) {
                event.getChannel().sendMessage("Please provide a article to search!").queue();
                return;
            }

            String article = getArticleNameFromUser(args);

            Wiki wiki = Main.wiki;
            EmbedBuilder em = new EmbedBuilder();

            em.setAuthor("Wiki Search By: " + event.getAuthor().getName(), event.getMessage().getJumpUrl(), event.getMember().getUser().getAvatarUrl());

            if (article.equals("random")) {
                em.setAuthor("Random Wiki Search By: " + event.getAuthor().getName(), event.getMessage().getJumpUrl(), event.getMember().getUser().getAvatarUrl());
                article = wiki.getRandomPages(1, NS.MAIN).get(0);
            }

            if (!wiki.exists(article)) {
                event.getChannel().sendMessage("Hmm, " + event.getAuthor().getAsMention() + " there does not seem to be a Wikipedia page on this.\nTry searching something different or check for a typo.").queue();
                return;
            }

            em.setColor(Color.ORANGE);

            String wikiLink = "https://en.wikipedia.org/wiki/" + article.replaceAll(" ", "_");

            em.setTitle("Wikipedia: " + wiki.nss(article.substring(0, 1).toUpperCase() + article.substring(1)), wikiLink);

            String wikiText = wiki.getTextExtract(article);

            if (wikiText.startsWith(article.substring(0, 1).toUpperCase() + article.substring(1) + " may refer to:")) {
                em.setDescription("There are multiple results for **" + article + "**!\nTo see all of the results [Click Here](" + wikiLink + ").");
                em.setFooter("Source: www.wikipedia.org", "https://upload.wikimedia.org/wikipedia/en/thumb/8/80/Wikipedia-logo-v2.svg/1200px-Wikipedia-logo-v2.svg.png");
                em.setAuthor("Wiki Search By: " + event.getAuthor().getName(), event.getMessage().getJumpUrl(), event.getMember().getUser().getAvatarUrl());
                event.getChannel().sendMessage(em.build()).queue();
                return;
            }

            if(wikiText.split("\n", 2)[0].length() > 2048){
                em.setDescription(wikiText.substring(0, 1950) + "[**[...]**]( " + wikiLink + ")");
            } else {
                em.setDescription(wikiText.split("\n", 2)[0]);
            }

            em.setImage(getImageLink(article));

            em.setFooter("Source: www.wikipedia.org", "https://upload.wikimedia.org/wikipedia/en/thumb/8/80/Wikipedia-logo-v2.svg/1200px-Wikipedia-logo-v2.svg.png");
            event.getChannel().sendMessage(em.build()).queue();
        }
    }


    public String getArticleNameFromUser (String[] input) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 1; i < input.length; i++) {
            strBuilder.append(" " + input[i]);
        }
        String newString = strBuilder.toString().substring(1);

        return newString;
    }


    public String getImageLink (String article) {

        article = article.replaceAll(" ", "_");

        final String BASE_URL="https://en.wikipedia.org/api/rest_v1/page/summary/";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL+article)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            String data = response.body().string();

            JsonObject newdata = new JsonParser().parse(data).getAsJsonObject().getAsJsonObject("thumbnail");
            try {
                newdata.get("source");
            } catch (Exception e) {
                return "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";
            }
            String imageLink = newdata.get("source").toString().substring(1, newdata.get("source").toString().length() - 1);
            return imageLink;
        }
        catch (IOException | HttpResponseHeaderParser.ParseException e) {
            e.printStackTrace();
        }
        return "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";
    }
}


