import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class Commands extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Member pog = event.getGuild().getMemberById("182729649703485440");
        //Uptime time stuff
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        long uptime = runtimeMXBean.getUptime();
        long uptimeInSeconds = uptime / 1000;
        long numberOfHours = uptimeInSeconds / (60 * 60);
        long numberOfMinutes = (uptimeInSeconds / 60) - (numberOfHours * 60);
        long numberOfSeconds = uptimeInSeconds % 60;

        //PING
        if (args[0].equalsIgnoreCase(Main.prefix + "ping")) {
            event.getChannel().sendMessage("Pong!" + "\n> WebSocket Latency: " + Long.toString(Main.jda.getPing()) + "ms").queue();
        }

        //HELP or COMMANDS
        if (args[0].equalsIgnoreCase(Main.prefix + "help") || args[0].equalsIgnoreCase(Main.prefix + "commands")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("**Commands**");
            embed.setColor(Color.YELLOW);
            embed.addField("Sticky Commands:", "``?stick <message>`` - Sticks message to the channel.\n" +
                                                "``?stickstop`` - Cancels stickied message.\n (*Member must have Manage Messages permissions to use sticky commands. You can only have one stickied message on your server at a time*).", false);
            embed.addField("Other Commands:", "``?about`` - Support Server and other useful info.\n``?invite`` - Invite link for StickyBot.", false);
            event.getChannel().sendMessage(embed.build()).queue();
        }

        //ABOUT
        if (args[0].equalsIgnoreCase(Main.prefix + "about")) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.yellow);
            eb.setTitle("__**StickyBot Information:**__");
            eb.addField("Developed By:", "P_O_G#2222", false);
            eb.addField("Ping:", Long.toString(Main.jda.getPing()) + "ms", false);
            eb.addField("Uptime:", "``" + numberOfHours + " Hours, " + numberOfMinutes + " Min, " + numberOfSeconds + " Seconds``", true);
            eb.addField("Guilds:", "StickyBot is in **" + String.valueOf(event.getJDA().getGuilds().size()) + "** Guilds", false);
            eb.addField("Support Server:", "[discord/stickySupport](https://discord.gg/SvNQTtf)", false);
            eb.addField("Donate:", "[paypal.me/sircam19](https://www.paypal.me/sircam19)", false);
            eb.addField("**Commands:** ", "Do ``?commands`` or ``?help``", false);
            event.getChannel().sendMessage(eb.build()).queue();
        }

        //DONATE
        if (args[0].equalsIgnoreCase(Main.prefix + "donate") || args[0].equalsIgnoreCase(Main.prefix + "dono")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.ORANGE);
            embed.addField("Buy me a beer!", "[``paypal.me/sircam19``](https://www.paypal.me/sircam19)", false);
            event.getChannel().sendMessage(embed.build()).queue();

        }

        //SHUTDOWN
        if (args[0].equalsIgnoreCase(Main.prefix + "shutdown")) {
            if (event.getMember() == pog) {
                event.getChannel().sendMessage("```Shutting Down Bot```").queue();
                Main.jda.shutdown();
            } else {
                event.getChannel().sendMessage("Only ``P_O_G#2222`` can use this command.").queue();
            }
        }

        //UPTIME
        if (args[0].equalsIgnoreCase(Main.prefix + "uptime")) {
            event.getChannel().sendMessage("Uptime: ``" + numberOfHours + " Hours, " + numberOfMinutes + " Min, " + numberOfSeconds + " Seconds``").queue();
        }

        //INVITE
        if (args[0].equalsIgnoreCase(Main.prefix + "invite")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.ORANGE);
            embed.addField("Invite StickyBot to your server:", "[top.gg/StickyBot](https://discordapp.com/oauth2/authorize?client_id=628400349979344919&scope=bot&permissions=68672)", false);
            event.getChannel().sendMessage(embed.build()).queue();
        }

    }}


