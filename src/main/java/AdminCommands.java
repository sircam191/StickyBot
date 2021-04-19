import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AdminCommands extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (event.getAuthor().isBot()) {
            return;
        }




        if (args[0].equalsIgnoreCase(Main.prefix + "restartshard")) {
            if (event.getMember().getIdLong() == 182729649703485440L) {

                Main.jda.restart(Integer.parseInt(args[1]));
                event.getChannel().sendMessage("Done Joe").queue();

            }
        }


        if (args[0].equalsIgnoreCase(Main.prefix + "manualstop")) {
            if (event.getMember().getIdLong() == 182729649703485440L) {

                if (Main.mapMessage.containsKey(args[1])) {

                Main.mapMessage.remove(args[1]);

                    try {
                        Connection dbConn = DriverManager.getConnection(Main.dbUrl,Main.dbUser,Main.dbPassword);
                        Statement myStmt = dbConn.createStatement();
                        String sql = "DELETE FROM newMessages WHERE channelId='" + args[1] + "';";
                        myStmt.execute(sql);
                        myStmt.close();
                    } catch ( SQLException e) {
                        e.printStackTrace();
                    }
                } else if (Main.mapMessageEmbed.containsKey(args[1])) {
                    Main.mapMessageEmbed.remove(args[1]);
                    try {
                        Connection dbConn = DriverManager.getConnection(Main.dbUrl,Main.dbUser,Main.dbPassword);
                        Statement myStmt = dbConn.createStatement();
                        String sql = "DELETE FROM messagesEmbed WHERE channelId='" + args[1] + "';";
                        myStmt.execute(sql);
                        myStmt.close();
                    } catch ( SQLException e) {
                        e.printStackTrace();
                    }

                }

                event.getChannel().sendMessage("Done sir").queue();

            }

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

        else if (args[0].equalsIgnoreCase(Main.prefix + "checkpremium")) {
            if (Main.premiumGuilds.containsValue(args[1])) {
                event.getChannel().sendMessage("Server HAS premium").queue();
            } else {
                event.getChannel().sendMessage("Server Does NOT have premium.");
            }

        }
        else if (args[0].equalsIgnoreCase(Main.prefix + "topservers")) {
            if (event.getMember().getIdLong() == 182729649703485440L) {
                var guilds = Main.jda.getGuilds().stream()
                        .sorted(Comparator.comparingInt(Guild::getMemberCount).reversed())
                        .limit(10)
                        .collect(Collectors.toList());

                String info = "";

                for (Guild s : guilds) {
                    info += "**Name:** " + s.getName() + "\n";
                    info += "**Member Count:** " + NumberFormat.getInstance().format(s.retrieveMetaData().complete().getApproximateMembers()) + "\n";
                    info += "**PFP Link:** `" + s.getIconUrl() + "`\n";
                    info += "**ID:** ``" + s.getId() + "``\n\n";
                }

                event.getChannel().sendMessage(info + "\n*Out of* *" + Main.jda.getGuildCache().size() + "* *servers*").queue();
            }

        }

    }
}
