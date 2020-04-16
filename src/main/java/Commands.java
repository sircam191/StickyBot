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
import java.security.spec.ECField;
import java.text.NumberFormat;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


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
                                                            "``?joinlist <count>`` - Get the first X amount of members who have joined the guild.\n" +
                                                            "``?roll`` - Role two dice.\n" +
                                                             "``?coinflip`` - Flips a coin.\n" +
                                                            "``?invite`` - Invite link for StickyBot.\n" +
                                                            "``?virus`` - Get latest stats from COVID-19.\n"

                                                             , false);
            embed.setFooter("For support please join the Support Server. Use ?about for the invite.", Main.jda.getSelfUser().getAvatarUrl());
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
            eb.addField("Guilds:", "StickyBot is in **" + NumberFormat.getInstance().format(event.getJDA().getGuilds().size()) + "** Guilds", false);
            eb.addField("Members:", "StickyBot is Serving **" + NumberFormat.getInstance().format(event.getJDA().getUserCache().size()) + "** Members", false);
            eb.addField("Support Server:", "[discord/stickySupport](https://discord.gg/SvNQTtf)", false);
            eb.addField("Vote for StickyBot:", "[top.gg/stickybot](https://top.gg/bot/628400349979344919)", false);
            eb.addField("Donate:", "[paypal.me/sircam19](https://www.paypal.me/sircam19)", false);
            eb.addField("**Commands:** ", "Do ``?commands`` or ``?help``", false);
            eb.setFooter("Made with \uD83D\uDC96 using JDA", Main.jda.getSelfUser().getAvatarUrl());

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
            if (event.getMember().getIdLong() == 182729649703485440L) {
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
                                "**Bots:** " + BotCount(event.getGuild()) + "\n" +
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

            } else  //POLL
                if (args[0].equalsIgnoreCase(Main.prefix + "poll")) {
                    String pollQ;
                    try {
                        if (!args[1].isEmpty()) {
                            pollQ = String.join(" ", args).substring(5);

                            EmbedBuilder emb = new EmbedBuilder();
                            emb.setColor(event.getGuild().getMemberById(Main.botId).getColor());
                            emb.setFooter("Poll by: " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());

                            emb.setTitle("**" + pollQ.trim() + "**");

                            //adds reactions to poll message
                            event.getChannel().sendMessage(emb.build()).queue(m -> {
                                m.addReaction("\ud83d\udc4d").queue();
                                m.addReaction("\uD83D\uDC4E").queue();
                                m.addReaction("\uD83E\uDD37").queue();
                            });
                        }
                    } catch (Exception e) {
                        event.getChannel().sendMessage("Please use this format ``?poll <your question>``.\nExample: ``?poll Is this a cool command?``").queue();
                    }
                    event.getMessage().delete().queue();
                } else //DICE ROLL
                    if (args[0].equalsIgnoreCase(Main.prefix + "dice") || args[0].equalsIgnoreCase(Main.prefix + "roll")) {
                        int dice1 = (int) (Math.random() * 6 + 1);
                        int dice2 = (int) (Math.random() * 6 + 1);
                        EmbedBuilder emb = new EmbedBuilder();

                        emb.setTitle("-Rolling Dice-");
                        emb.setDescription("Dice 1: **" + (dice1) + "**" + "\nDice 2: **" + dice2 + "**" +
                                "\n\n**TOTAL: " + (dice1 + dice2) + "**");

                        emb.setColor(Color.WHITE);
                        emb.setThumbnail("https://studio.code.org/v3/assets/GBhvGLEcbJGFHdJfHkChqw/8TEb9oxGc.gif");
                        emb.setFooter("Rolled by: " + event.getMember().getEffectiveName(), event.getMember().getUser().getAvatarUrl());
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
                                joinSpot = event.getGuild().getMembers().stream().sorted(Comparator.comparing(Member::getTimeJoined)).takeWhile(it -> !it.equals(taggedMember)).count() + 1;
                            } else {
                                joinSpot = event.getGuild().getMembers().stream().sorted(Comparator.comparing(Member::getTimeJoined)).takeWhile(it -> !it.equals(event.getMember())).count() + 1;
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
                                            "**Status:** " + taggedMember.getOnlineStatus().getKey() + "\n" +
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
                    //JOIN LIST
                    else if (args[0].equalsIgnoreCase(Main.prefix + "joinl") || args[0].equalsIgnoreCase(Main.prefix + "joinlist")) {
                        int requestedCount = 10;
                        try {
                            if (!args[1].isEmpty()) {
                                try {
                                    requestedCount = Integer.valueOf(args[1]);
                                    if(requestedCount < 2) {
                                        requestedCount = 2;
                                    } else if (requestedCount > 25) {
                                        requestedCount = 25;
                                    }
                                } catch (Exception e) {
                                    requestedCount = 10;
                                }
                            }
                        } catch (Exception e) {
                            requestedCount = 10;
                        }

                        List<Member> firstMemberList = event.getGuild().getMembers().stream().sorted(Comparator.comparing(Member::getTimeJoined)).limit(requestedCount).collect(Collectors.toList());

                        EmbedBuilder emb = new EmbedBuilder();
                        emb.setTitle("First " + requestedCount + " people to join __" + event.getGuild().getName() + "__" + ":");
                        for(int i = 0; i <= firstMemberList.size() -1; i++) {

                           emb.addField("**" + String.valueOf(i + 1) + ".**", firstMemberList.get(i).getAsMention() + "\n" + firstMemberList.get(i).getUser().getName() + "#" + firstMemberList.get(i).getUser().getDiscriminator(), false);
                        }
                        emb.setColor(event.getMember().getColor());
                        emb.setFooter("Guild Created: " + numberOfDaysCreatedGuild(event.getGuild()) + " days ago");
                        event.getChannel().sendMessage(emb.build()).queue();
                    }


        if (args[0].equalsIgnoreCase(Main.prefix + "coinflip") || args[0].equalsIgnoreCase(Main.prefix + "flip") || args[0].equalsIgnoreCase(Main.prefix + "coin") || args[0].equalsIgnoreCase(Main.prefix + "flipcoin")) {

            int random = (int) Math.round(Math.random());
            EmbedBuilder emb = new EmbedBuilder();
            emb.setTitle("-Coin Flip-");
            emb.setColor(Color.GRAY);

            if(random == 0) {
                emb.setDescription("Coin Landed on **HEADS**!");
            } else {
                emb.setDescription("Coin Landed on **TAILS**!");
            }
            emb.setFooter("Flipped by: " + event.getMember().getEffectiveName(), event.getMember().getUser().getAvatarUrl());
            event.getChannel().sendMessage(emb.build()).queue();

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
            String tier = guild.getBoostTier().name();
            String boosters = String.valueOf(guild.getBoostCount());
            return tier + ", " + boosters +  " Boosts.";
        } else {
            return "Tier 0";
        }
    }

    public static String BotCount(Guild guild) {
        int counter = 0;
        for(Member member : guild.getMembers()) {
            if(member.getUser().isBot()){
                counter++;
            }
        }
       return String.valueOf(counter);
    }


}


