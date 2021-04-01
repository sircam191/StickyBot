import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class ShardCommands extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        String prefix = "?";

        if(event.getAuthor().isBot()) {
            return;
        }

        if(Main.mapPrefix.containsKey(event.getGuild().getId())) {
            prefix = Main.mapPrefix.get(event.getGuild().getId());
        }

        if (args[0].equalsIgnoreCase(prefix + "shard") || args[0].equalsIgnoreCase(prefix + "shards")) {

            EmbedBuilder emb = new EmbedBuilder();
            emb.setColor(Color.ORANGE);
            emb.setTitle("-Shard Info-");
            emb.addField("Shards: ", "Total Shards: " + (Main.jda.getShardsTotal()) +
                    "\nThis Guilds Shard: " + ((event.getGuild().getIdLong() >>> 22) % Main.jda.getShardsTotal()), false);

            event.getChannel().sendMessage(emb.build()).queue();

        } else if (args[0].equalsIgnoreCase(prefix + "shardping")) {
            String pings = "__**Shard Pings:**__\n";

            pings += "`Average: " + Main.jda.getAverageGatewayPing() + "`\n";

            for (JDA shard : Main.jda.getShards()) {
                pings += "**Shard:** " + shard.getShardInfo().getShardString() + " **Ping:** " + shard.getGatewayPing() + "ms. **Status:** " + shard.getStatus() + "\n";
            }
            event.getChannel().sendMessage(pings).queue();
        }

    }

}
