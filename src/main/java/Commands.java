import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.OffsetDateTime;
import java.util.Comparator;


import static java.time.temporal.ChronoUnit.DAYS;

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
            event.getChannel().sendMessage("Pong!" + "\n> WebSocket Latency: " + Long.toString(Main.jda.getGatewayPing()) + "ms").queue();
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
                                                            "``?serverinfo`` - Get info on the server.\n" +
                                                            "``?roll`` - Role two dice.\n" +
                                                            "``?faq`` - Get the FAQ for StickyBot.\n" +
                                                            "``?invite`` - Invite link for StickyBot.\n"

                                                             , false);
            embed.setFooter("For support please join the Support Server. Use ?about for the invite.", "https://cdn.discordapp.com/attachments/641158383138897943/663491981111984129/SupportServer1.png");
            event.getChannel().sendMessage(embed.build()).queue();
        } else

        //ABOUT
        if (args[0].equalsIgnoreCase(Main.prefix + "about")) {

            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.yellow);
            eb.setTitle("__**StickyBot Information:**__");
            eb.addField("Developed By:", "P_O_G#2222", false);
            eb.addField("Ping:", Long.toString(Main.jda.getGatewayPing()) + "ms", false);
            eb.addField("Uptime:", "``" + numberOfHours + " Hours, " + numberOfMinutes + " Min, " + numberOfSeconds + " Seconds``", true);
            eb.addField("Guilds:", "StickyBot is in **" + String.valueOf(event.getJDA().getGuilds().size()) + "** Guilds", false);
            eb.addField("Members:", "StickyBot is Serving **" + event.getJDA().getUserCache().size() + "** Members", false);
            eb.addField("Support Server:", "[discord/stickySupport](https://discord.gg/SvNQTtf)", false);
            eb.addField("Vote for StickyBot:", "[discord.gg/stickybot](https://top.gg/bot/628400349979344919)", false);
            eb.addField("Donate:", "[paypal.me/sircam19](https://www.paypal.me/sircam19)", false);
            eb.addField("**Commands:** ", "Do ``?commands`` or ``?help``", false);

            event.getChannel().sendMessage(eb.build()).queue();

        } else

        //DONATE
        if (args[0].equalsIgnoreCase(Main.prefix + "donate") || args[0].equalsIgnoreCase(Main.prefix + "dono")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.ORANGE);
            embed.addField("Buy me a beer!", "[``paypal.me/sircam19``](https://www.paypal.me/sircam19)", false);
            event.getMessage().addReaction("\u2764").queue();
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

            //SERVER INFO
            if (args[0].equalsIgnoreCase(Main.prefix + "serverinfo")) {


                String creationDateClean = String.valueOf(event.getGuild().getTimeCreated().getMonth() + " " + String.valueOf(event.getGuild().getTimeCreated().getDayOfMonth()) + ", " + String.valueOf(event.getGuild().getTimeCreated().getYear()));


                EmbedBuilder emb = new EmbedBuilder();

                emb.setThumbnail(event.getGuild().getIconUrl());
                emb.setTitle("**-Server Info-**");
                emb.addField("Info for " + event.getGuild().getName(),
                        "**Server ID:** ``" + event.getGuild().getId() + "``\n" +
                                "**Creation Date:** " + creationDateClean + " *(" + numberOfDaysCreatedGuild(event.getGuild()) + " days ago)*" + "\n" +
                                "**Members:** " + event.getGuild().getMemberCount() + "\n" +
                                "**Owner:** " + event.getGuild().getOwner().getAsMention() + "\n" +
                                "**Region: ** " + event.getGuild().getRegion().getName() + " " + event.getGuild().getRegion().getEmoji() + "\n" +
                                "**Nitro Boosting: ** " + GuildBoost(event.getGuild()) + "\n" +
                                "**Number of Roles:** " + event.getGuild().getRoles().size() + "\n" +
                                "**Text Channels:** " + event.getGuild().getTextChannels().size() + "\n" +
                                "**Voice Channels:** " + event.getGuild().getVoiceChannels().size() + "\n" +
                                "**Custom Emotes:** " + event.getGuild().getEmotes().size()
                        , false);


                emb.setColor(event.getGuild().getOwner().getColor());
                emb.setFooter(event.getGuild().getName(), event.getGuild().getIconUrl());

                event.getChannel().sendMessage(emb.build()).queue();
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

                        emb.addField("-Rolling Dice-", "Dice 1: **" + (dice1) + "**", false);
                        emb.addField("", "Dice 2: **" + (dice2) + "**", false);
                        emb.addField("", "**TOTAL: **   **" + (dice1 + dice2) + "**", false);
                        emb.setColor(Color.WHITE);
                        emb.setThumbnail("https://media.giphy.com/media/5nxHFn5888nrq/giphy.gif");
                        event.getChannel().sendMessage(emb.build()).queue();
                    } else if (args[0].equalsIgnoreCase(Main.prefix + "userinfo")) {
                       //USERINFO
                        try {
                            long joinSpot;
                            User tagUser;
                            Member taggedMember;

                            if(event.getMessage().toString().contains("@")) {
                                tagUser = event.getMessage().getMentionedUsers().get(0);
                                taggedMember = event.getMessage().getMentionedMembers().get(0);
                                joinSpot = event.getGuild().getMembers().stream().sorted(Comparator.comparing(Member::getTimeJoined)).takeWhile(it -> !it.equals(taggedMember)).count();
                            } else {
                                joinSpot = event.getGuild().getMembers().stream().sorted(Comparator.comparing(Member::getTimeJoined)).takeWhile(it -> !it.equals(event.getMember())).count();
                                    tagUser = event.getAuthor();
                                    taggedMember = event.getMember();
                            }


                            EmbedBuilder emb = new EmbedBuilder();
                            String joinDateClean = String.valueOf(taggedMember.getTimeJoined().getMonth() + " " + String.valueOf(taggedMember.getTimeJoined().getDayOfMonth()) + ", " + String.valueOf(taggedMember.getTimeJoined().getYear()));
                            String creationDateClean = String.valueOf(taggedMember.getTimeCreated().getMonth() + " " + String.valueOf(taggedMember.getTimeCreated().getDayOfMonth()) + ", " + String.valueOf(taggedMember.getTimeCreated().getYear()));

                            emb.setThumbnail(tagUser.getAvatarUrl());
                            emb.setTitle("**-User Info-**");
                            emb.addField("Info for " + tagUser.getName() + "#" + tagUser.getDiscriminator(),
                                    "**User ID:** ``" + tagUser.getId() + "``\n" +
                                            "**Nickname:** " + taggedMember.getEffectiveName() + "\n" +
                                            "**Join Date:** " + joinDateClean + " *(" + numberOfDaysJoined(taggedMember) + " days ago)*" + "\n" +
                                            "**Join Position:** " + joinSpot + "\n" +
                                            "**Creation Date:** " + creationDateClean + " *(" + numberOfDaysCreated(taggedMember) + " days ago)*" + "\n" +
                                            "**Status:** " + taggedMember.getOnlineStatus().toString() + "\n" +
                                            "**Tag: ** " + taggedMember.getAsMention() + "\n" +
                                            "**Nitro Boosting: ** " + boostCheck(taggedMember) + "\n" +
                                            "**Number of Roles:** " + taggedMember.getRoles().size() + "\n" +
                                            "**Roles:** " + getRoles(taggedMember)
                                    , false);


                            emb.setColor(taggedMember.getColor());

                            if(taggedMember.hasPermission(Permission.ADMINISTRATOR)) {
                                emb.setFooter(tagUser.getName() + " is a admin", tagUser.getAvatarUrl());
                            }
                            else {
                                emb.setFooter(tagUser.getName() + " is not a admin", tagUser.getAvatarUrl());
                            }
                            event.getChannel().sendMessage(emb.build()).queue();

                        } catch (Exception e) {
                            event.getChannel().sendMessage("Please tag a member in the server.").queue();
                        }
                    }

    }

    public static String boostCheck(Member member) {

        if(member.getTimeBoosted() == null){
            return "Member is not boosting.";
        } else {
            return ("Boosting since: " + member.getTimeBoosted().getMonth() + " " + String.valueOf(member.getTimeBoosted().getDayOfMonth()) + ", " + String.valueOf(member.getTimeBoosted().getYear())) + " *(" + numberOfDaysBoosted(member) + " days ago)*";
        }
    }

    public static String numberOfDaysJoined(Member member) {
        long daysBetween = DAYS.between(member.getTimeJoined(), OffsetDateTime.now());
        return String.valueOf(daysBetween);
    }

    public static String numberOfDaysCreatedGuild(Guild guild) {
        long daysBetween = DAYS.between(guild.getTimeCreated(), OffsetDateTime.now());
        return String.valueOf(daysBetween);
    }


    public static String numberOfDaysCreated(Member member) {
        long daysBetween = DAYS.between(member.getTimeCreated(), OffsetDateTime.now());
        return String.valueOf(daysBetween);
    }

    public static String numberOfDaysBoosted(Member member) {
        long daysBetween = DAYS.between(member.getTimeBoosted(), OffsetDateTime.now());
        return String.valueOf(daysBetween);
    }

    public static String getRoles(Member taggedMember) {
        int i = taggedMember.getRoles().size();
        String rolesTagged = "";
        while (i > 0) {
            rolesTagged += taggedMember.getRoles().get(i - 1).getAsMention();
            rolesTagged += " ";
            i--;
        }
        if (!rolesTagged.isEmpty()) {
            return rolesTagged;
        } else {
            return "None";
        }
    }
        public static String GuildBoost(Guild guild) {
            if (guild.getBoostCount() > 0) {
            String tier = guild.getBoostTier().toString();
            String boosters = String.valueOf(guild.getBoostCount());
            return tier + ". # of Boosts: " + boosters;
            } else {
                return "Tier 0";
        }

    }
}


