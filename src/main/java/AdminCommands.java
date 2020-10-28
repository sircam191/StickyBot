import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class AdminCommands extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (event.getAuthor().isBot()) {
            return;
        }

        if (args[0].equalsIgnoreCase(Main.prefix + "adminstats")) {
            if (event.getMember().getIdLong() == 182729649703485440L) {

                //Uptime stuff
                RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
                long uptime = runtimeMXBean.getUptime();
                long uptimeInSeconds = uptime / 1000;
                long numberOfHours = uptimeInSeconds / (60 * 60);
                long numberOfMinutes = (uptimeInSeconds / 60) - (numberOfHours * 60);
                long numberOfSeconds = uptimeInSeconds % 60;

                EmbedBuilder emb = new EmbedBuilder();

                emb.setTitle("StickyBot VPS Admin Stats");
                emb.setColor(Color.ORANGE);
                emb.setDescription("StickyBot is created by P_O_G#2222 (182729649703485440). \n[GitHub](https://github.com/sircam191/StickyBot)");

                /* Total number of processors or cores available to the JVM */
                emb.addField("Available processors:", Integer.toString(Runtime.getRuntime().availableProcessors()) , false);

                /* Total amount of free memory available to the JVM */
                emb.addField("Free memory: ", FileUtils.byteCountToDisplaySize(Runtime.getRuntime().freeMemory()), true);

                /* This will return Long.MAX_VALUE if there is no preset limit */
                long maxMemory = Runtime.getRuntime().maxMemory();
                /* Maximum amount of memory the JVM will attempt to use */
                String output = "empty";

                if (maxMemory == Long.MAX_VALUE) {
                    output = "no limit";
                } else {
                    output = FileUtils.byteCountToDisplaySize(maxMemory);
                }

                emb.addField("Maximum memory: ", output, true);

                /* Total memory currently available to the JVM */
                emb.addField("Total memory available to JVM:", FileUtils.byteCountToDisplaySize(Runtime.getRuntime().totalMemory()), true);

                /* Bots uptime */
                emb.addField("StickyBot session uptime: ", "Uptime: ``" + numberOfHours + " Hours, " + numberOfMinutes + " Min, " + numberOfSeconds + " Seconds``", false);

                /* Get a list of all filesystem roots on this system */
                File[] roots = File.listRoots();

                /* For each filesystem root, print some info */
                for (File root : roots) {

                    StringBuilder info = new StringBuilder();
                    info.append("Total space: " + FileUtils.byteCountToDisplaySize(root.getTotalSpace()));
                    info.append("\nFree space: " + FileUtils.byteCountToDisplaySize(root.getFreeSpace()));
                    info.append("\nUsable space: " + FileUtils.byteCountToDisplaySize(root.getUsableSpace()));

                    emb.addField("File system root: " + root.getAbsolutePath(), info.toString(), true);
                }

                String pings = "Shard Pings:\n";
                for (JDA shard : Main.jda.getShards()) {
                    pings += shard.getShardInfo().getShardString() + ": " + shard.getGatewayPing() + "ms\n";
                }
                emb.addField("Shard Pings", pings, false);

                emb.setFooter(event.getMessage().getTimeCreated().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
                event.getChannel().sendMessage(emb.build()).queue();

            } else {
                event.getMessage().addReaction("\u274C").queue();
            }
            }

    }
}
