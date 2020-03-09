import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import zone.nora.coronavirus.Coronavirus;
import zone.nora.coronavirus.data.latest.LatestData;

import java.awt.*;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class VirusCommand extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Map<String, String> mapDeleteId = new HashMap<>();

        if (args[0].equalsIgnoreCase(Main.prefix + "virus") || args[0].equalsIgnoreCase(Main.prefix + "corona") || args[0].equalsIgnoreCase(Main.prefix + "covid-19") || args[0].equalsIgnoreCase(Main.prefix + "coronavirus")) {

            EmbedBuilder emb = new EmbedBuilder();
            event.getChannel().sendMessage(emb.setTitle("Loading...").build()).queue(m ->  mapDeleteId.put("id", m.getId()));

            //Corona Virus API
            Coronavirus coronavirus = new Coronavirus();
            try {
                LatestData latest = coronavirus.getLatestData();

                emb.setTitle("COVID-19 Latest Stats");
                emb.addField("Confirmed Cases", NumberFormat.getInstance().format(latest.getConfirmed()), false);
                emb.addField("Recovered Cases", NumberFormat.getInstance().format(latest.getRecovered()), false);
                emb.addField("Deaths", NumberFormat.getInstance().format(latest.getDeaths()), false);
                emb.setColor(Color.RED);
                emb.setFooter("Statistics Source: Johns Hopkins CSSE");
                event.getChannel().deleteMessageById(mapDeleteId.get("id")).queue();
                event.getChannel().sendMessage(emb.build()).queue();


            } catch (IOException e) {
                e.printStackTrace();
            }

            //



        }
  }

}
