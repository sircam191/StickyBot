import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class Commands extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

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
        } else

        //HELP or COMMANDS
        if (args[0].equalsIgnoreCase(Main.prefix + "help") || args[0].equalsIgnoreCase(Main.prefix + "commands")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("**Commands**");
            embed.setColor(Color.YELLOW);
            embed.addField("Sticky Commands:", "``?stick <message>`` - Sticks message to the channel.\n" +
                                                "``?stickstop`` - Cancels stickied message.\n (*Member must have Manage Messages permissions to use sticky commands.*).", false);
            embed.addField("Other Commands:", "``?about`` - Support Server and other useful info.\n" +
                                                             "``?poll <question>`` - Create a poll for people to vote.\n"+
                                                            "``?userinfo <@user>`` - Get info on a member.\n" +
                                                            "``?roll`` - Role two dice.\n" +
                                                            "``?faq`` - Get the FAQ for StickyBot.\n" +
                                                            "``?invite`` - Invite link for StickyBot.\n"

                                                             , false);
            embed.setFooter("For support please join the Support Server. Use ``?about`` for the invite.", "https://cdn.discordapp.com/attachments/641158383138897943/663491981111984129/SupportServer1.png");
            event.getChannel().sendMessage(embed.build()).queue();
        } else

        //ABOUT
        if (args[0].equalsIgnoreCase(Main.prefix + "about")) {

            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.yellow);
            eb.setTitle("__**StickyBot Information:**__");
            eb.addField("Developed By:", "P_O_G#2222", false);
            eb.addField("Ping:", Long.toString(Main.jda.getPing()) + "ms", false);
            eb.addField("Uptime:", "``" + numberOfHours + " Hours, " + numberOfMinutes + " Min, " + numberOfSeconds + " Seconds``", true);
            eb.addField("Guilds:", "StickyBot is in **" + String.valueOf(event.getJDA().getGuilds().size()) + "** Guilds", false);
            eb.addField("Members:", "StickyBot is Serving **" + event.getJDA().getUserCache().size() + "** Members", false);
            eb.addField("Support Server:", "[discord/stickySupport](https://discord.gg/SvNQTtf)", false);
            eb.addField("Vote for StickyBot:", "[discord.gg/stickybot](https://top.gg/bot/628400349979344919)", false);
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

        } else

        //SHUTDOWN
        if (args[0].equalsIgnoreCase(Main.prefix + "shutdown")) {
            Member pog = event.getGuild().getMemberById("182729649703485440");
            if (event.getMember() == pog) {
                event.getChannel().sendMessage("```Shutting Down Bot```").queue();
                Main.jda.shutdown();
            } else {
                event.getChannel().sendMessage("Only ``P_O_G#2222`` can use this command.").queue();
            }
        } else

        //UPTIME
        if (args[0].equalsIgnoreCase(Main.prefix + "uptime")) {
            event.getChannel().sendMessage("Uptime: ``" + numberOfHours + " Hours, " + numberOfMinutes + " Min, " + numberOfSeconds + " Seconds``").queue();
        } else

        //INVITE
        if (args[0].equalsIgnoreCase(Main.prefix + "invite")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.ORANGE);
            embed.addField("Invite StickyBot to your server:", "[top.gg/StickyBot](https://top.gg/bot/628400349979344919)", false);
            event.getChannel().sendMessage(embed.build()).queue();

        } else if (args[0].equalsIgnoreCase(Main.prefix + "faq")) {
            //FAQ
            EmbedBuilder emb = new EmbedBuilder();
            emb.setTitle("StickyBot FAQ");
            emb.addField("When I use commands the bot does nothing?", "Make sure the bot can read messages in that channel as well as send then. (If the bot is offline check the #bot-status channel to see why)", false);
            emb.addField("How many stickied messages can I have at once?", "You can have as many stickied messages in your server as you would like.", false);
            emb.addField("Why does it stop the sticky sometimes even when I don't use the ``?stickstop`` command?", "Anytime the the bot is restarted you will need to redo the ``?stick`` command. (The bot is restarted a average of once every 1 or 2 months)", false);
            emb.addField("When I use the ``?stick`` command it reacts with a X?", "This either means you don not have permissions to use that command OR the bot does not have permissions to send messages in that channel. (To use the ?stick commands you need to have the manage messages permission in the server.)" , false);
            emb.setFooter("StickyBot FAQ", "https://images.discordapp.net/avatars/628400349979344919/aa45048719fee7f9d79534e0601ec8e6.png?size=512");
            emb.setColor(Color.YELLOW);
            event.getChannel().sendMessage(emb.build()).queue();
            } else  //POLL
                if (args[0].equalsIgnoreCase(Main.prefix + "poll")) {
                    String pollQ;
                    try {
                        if (!args[1].isEmpty()) {
                            pollQ = String.join(" ", args).substring(5);

                            event.getMessage().delete().queue();

                            EmbedBuilder emb = new EmbedBuilder();
                            emb.setColor(Color.BLACK);
                            emb.setFooter("Poll by: " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());

                            emb.setTitle("**" + pollQ + "**");

                            //adds reactions to poll message
                            event.getChannel().sendMessage(emb.build()).queue(m -> {
                                m.addReaction("\ud83d\udc4d").queue();
                                m.addReaction("\uD83D\uDC4E").queue();
                            });
                        }
                    } catch (Exception e) {
                        event.getChannel().sendMessage("Please use this format ``?poll <your question>``.\nExample: ``?poll Is this a cool command?``").queue();
                    }
                } else //DICE ROLL
                    if (args[0].equalsIgnoreCase(Main.prefix + "dice") || args[0].equalsIgnoreCase(Main.prefix + "roll")) {
                        int dice1 = (int) (Math.random() * 6 + 1);
                        int dice2 = (int) (Math.random() * 6 + 1);
                        EmbedBuilder emb = new EmbedBuilder();

                        emb.addField("-Rolling Dice-", "Dice 1: **" + Integer.toString(dice1) + "**", false);
                        emb.addField("", "Dice 2: **" + Integer.toString(dice2) + "**", false);
                        emb.addField("", "**TOTAL: **   **" + Integer.toString(dice1 + dice2) + "**", false);
                        emb.setColor(Color.WHITE);
                        emb.setThumbnail("https://media.giphy.com/media/5nxHFn5888nrq/giphy.gif");
                        event.getChannel().sendMessage(emb.build()).queue();
                    } else if (args[0].equalsIgnoreCase(Main.prefix + "userinfo")) {
                       //USERINFO
                        try {
                            User tagUser = event.getMessage().getMentionedUsers().get(0);
                            Member taggedMember = event.getMessage().getMentionedMembers().get(0);
                            EmbedBuilder emb = new EmbedBuilder();
                            String joinDateClean = String.valueOf(taggedMember.getJoinDate().getMonth() + " " + String.valueOf(taggedMember.getJoinDate().getDayOfMonth()) + ", " + String.valueOf(taggedMember.getJoinDate().getYear()));

                            //Adds user roles as mentions into String
                            int i = taggedMember.getRoles().size();
                            String rolesTagged = "";
                            while( i > 0) {
                                rolesTagged += taggedMember.getRoles().get(i -1).getAsMention();
                                rolesTagged += " ";
                                i--;
                            }
                            //////

                            emb.setThumbnail(tagUser.getAvatarUrl());
                            emb.setTitle("**-User Info-**");
                            emb.addField("Info for " + taggedMember.getEffectiveName() + "#" + tagUser.getDiscriminator(),
                                    "**User ID:** " + tagUser.getId() + "\n" +
                                            "**Nickname:** " + tagUser.getName() + "\n" +
                                            "**Join Date:** " + joinDateClean + "\n" +
                                            "**Status:** " + taggedMember.getOnlineStatus().toString() + "\n" +
                                            "**Tag: ** " + taggedMember.getAsMention() + "\n" +
                                            "**Number of Roles:** " + taggedMember.getRoles().size() + "\n" +
                                            "**Roles:** " + rolesTagged
                                    , false);

                            emb.setColor(taggedMember.getColor());


                            if(taggedMember.hasPermission(Permission.ADMINISTRATOR)) {
                                emb.setFooter(taggedMember.getEffectiveName() + " is a Admin", tagUser.getAvatarUrl());
                            }
                            else {
                                emb.setFooter(taggedMember.getEffectiveName() + " is NOT a Admin", tagUser.getAvatarUrl());
                            }
                            event.getChannel().sendMessage(emb.build()).queue();

                        } catch (Exception e) {
                            event.getChannel().sendMessage("Please tag a member in the server.").queue();
                        }

                    }
    }
}


