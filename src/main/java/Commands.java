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
import java.sql.*;
import java.text.NumberFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static java.time.temporal.ChronoUnit.DAYS;

public class Commands extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        String prefix = "?";

        if(event.getAuthor().isBot()) {
            return;
        }

        if(Main.mapPrefix.containsKey(event.getGuild().getId())) {
            prefix = Main.mapPrefix.get(event.getGuild().getId());
        }



        //PING
        if (args[0].equalsIgnoreCase(prefix + "ping")) {


            event.getChannel().sendMessage("Pong!" + "\n> WebSocket Latency: " + Main.jda.getAverageGatewayPing() + "ms").queue();
        }

         //HELP or COMMANDS
        else if (args[0].equalsIgnoreCase(prefix + "help") || args[0].equalsIgnoreCase(prefix + "commands") || args[0].equalsIgnoreCase(Main.prefix + "help")) {
             EmbedBuilder embed = new EmbedBuilder();
             embed.setTitle("**-Commands-**");
             embed.setColor(Color.YELLOW);
             embed.setDescription("[www.stickybot.info](https://www.stickybot.info/)");
             embed.addField("Sticky Commands:", "``" + prefix + "stick <message>`` - Sticks message to the channel.\n" +
                     "``" + prefix + "stickstop`` - Cancels stickied message.\n (*Member must have Manage Messages permissions to use sticky commands.*).", false);
             embed.addField("Other Commands:", "``?about`` - Support Server and other useful info.\n" +
                             "``" + prefix + "poll <question>`` - Create a poll for people to vote.\n" +
                             "``" + prefix + "userinfo <@user>`` - Get info on a member.\n" +
                             "``" + prefix + "serverinfo`` - Get info on the server.\n" +
                             "``" + prefix + "embed <message>`` - Turns your message into a embed.\n" +
                             "``" + prefix + "roll`` - Roll two dice.\n" +
                             "``" + prefix + "weather <city>`` - Get the current weather in a city.\n" +
                             "``" + prefix + "randomwiki`` - Get a random WikiHow article.\n" +
                             "``" + prefix + "coinflip`` - Flips a coin.\n" +
                             "``" + prefix + "invite`` - Invite link for StickyBot.\n" +
                             "``" + prefix + "support`` - Invite to the StickyBot Support Server.\n" +
                             "``" + prefix + "donate`` - Help keep StickyBot running smoothly.\n" +
                             "``" + prefix + "permcheck`` - Check if StickyBot has all the needed permissions.\n" +
                             "``" + prefix + "premium`` - Purchase StickyBot Premium.\n"
                     , false);
             embed.addField("Premium Commands:", "``" + prefix + "prefix <prefix>`` - Sets StickyBots prefix.\n" +
                     "``?resetprefix`` - Resets prefix to `?`.\n" +
                     "``" + prefix + "stickembed <message>`` - Creates a sticky with a **embed**.\n" +
                    "(*Member must have Manage Server permissions to use prefix commands.*).", false);

             embed.addField("Prefix:", "This guilds prefix: `" + prefix + "`", false);
             embed.setFooter("For support please join the Support Server. Use " + prefix + "support for the invite.", Main.jda.getShards().get(0).getSelfUser().getAvatarUrl());
             event.getChannel().sendMessage(embed.build()).queue();
            }

            //ABOUT
           else if (args[0].equalsIgnoreCase(prefix + "about")) {

            //Uptime stuff
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            long uptime = runtimeMXBean.getUptime();
            long uptimeInSeconds = uptime / 1000;
            long numberOfHours = uptimeInSeconds / (60 * 60);
            long numberOfMinutes = (uptimeInSeconds / 60) - (numberOfHours * 60);
            long numberOfSeconds = uptimeInSeconds % 60;

                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(Color.yellow);
                eb.setTitle("**-StickyBot Information-**");
                eb.addField("Developed By:", "P_O_G#2222", false);
                eb.addField("Website:", "[www.stickybot.info](https://www.stickybot.info/)", false);
                eb.addField("Ping:", Main.jda.getAverageGatewayPing() + "ms", false);
                eb.addField("Uptime:", "``" + numberOfHours + " Hours, " + numberOfMinutes + " Min, " + numberOfSeconds + " Seconds``", true);
                eb.addField("Shards: ", "Shard " + "**" + ((event.getGuild().getIdLong() >>> 22) % Main.jda.getShardsTotal()) + " of " + (Main.jda.getShardsTotal()) + "**", false);
                eb.addField("Guilds:", "StickyBot is in **" + NumberFormat.getInstance().format(Main.jda.getGuildCache().size()) + "** Guilds", false);
                eb.addField("Support Server:", "[StickyBot Support](https://discord.gg/SvNQTtf)", false);
                eb.addField("Vote for StickyBot:", "[top.gg/stickybot](https://top.gg/bot/628400349979344919)", false);
                eb.addField("Donate:", "[paypal.me/sircam19](https://www.paypal.me/sircam19)", false);
                eb.addField("Premium: ", "[www.stickybot.info/premium](https://www.stickybot.info/premium)", false);
                eb.addField("**Commands:** ", "Do ``?commands`` or ``?help``", false);
                eb.setFooter("Made with \uD83D\uDC96 using JDA", Main.jda.getShards().get(0).getSelfUser().getAvatarUrl());

                event.getChannel().sendMessage(eb.build()).queue();
           }

                //DONATE
           else if (args[0].equalsIgnoreCase(prefix + "donate") || args[0].equalsIgnoreCase(prefix + "dono")) {
               EmbedBuilder embed = new EmbedBuilder();
               embed.setColor(Color.ORANGE);
               embed.setTitle("Help Keep StickyBot up and running smoothly!");
               embed.setDescription("[**paypal.me/sircam19**](https://www.paypal.me/sircam19)");
               embed.setFooter("Include your Discord name or ID in the donation to receive the @donator tag in the support server.");
               event.getMessage().addReaction("\u2764").queue();
               event.getChannel().sendMessage(embed.build()).queue();

           }

                    //SHUTDOWN
           else if (args[0].equalsIgnoreCase(prefix + "shutdown")) {
            if (event.getMember().getIdLong() == 182729649703485440L) {
                event.getChannel().sendMessage("```Shutting Down Bot```").queue();
                Main.jda.shutdown();
            } else {
                event.getChannel().sendMessage("Only ``P_O_G#2222`` can use this command.").queue();
            }
        }

            //RESTART
            if (args[0].equalsIgnoreCase(prefix + "restart")) {
                if (event.getMember().getIdLong() == 182729649703485440L) {
                    event.getChannel().sendMessage("```Restarting Bot```").queue();
                    Main.jda.restart();
                } else {
                    event.getChannel().sendMessage("Only ``P_O_G#2222`` can use this command.").queue();
                }
            }
                //SERVER INFO
            else if (args[0].equalsIgnoreCase(prefix + "serverinfo")) {
                String creationDateClean = String.valueOf(event.getGuild().getTimeCreated().getMonth() + " " + String.valueOf(event.getGuild().getTimeCreated().getDayOfMonth()) + ", " + String.valueOf(event.getGuild().getTimeCreated().getYear()));

                EmbedBuilder emb = new EmbedBuilder();

                emb.setThumbnail(event.getGuild().getIconUrl());
                emb.setTitle("**-Server Info-**");
                emb.addField("Info for " + event.getGuild().getName(),
                        "**Server ID:** ``" + event.getGuild().getId() + "``\n" +
                                "**Creation Date:** " + creationDateClean + " *(" + numberOfDaysCreatedGuild(event.getGuild()) + " days ago)*" + "\n" +
                                "**Members:** " + NumberFormat.getInstance().format(event.getGuild().retrieveMetaData().complete().getApproximateMembers()) + "\n" +
                                //"**Bots:** " + BotCount(event.getGuild()) + "\n" +
                                "**Owner:** " + event.getGuild().retrieveOwner().complete().getAsMention() + "\n" +
                                "**Region: ** " + event.getGuild().getRegion().getName() + " " + event.getGuild().getRegion().getEmoji() + "\n" +
                                "**Nitro Boosting: ** " + GuildBoost(event.getGuild()) + "\n" +
                                "**Number of Roles:** " + event.getGuild().getRoles().size() + "\n" +
                                "**Text Channels:** " + event.getGuild().getTextChannels().size() + "\n" +
                                "**Voice Channels:** " + event.getGuild().getVoiceChannels().size() + "\n"
                        , false);


                emb.setColor(Color.BLACK);
                emb.setFooter(event.getGuild().getName(), event.getGuild().getIconUrl());

                event.getChannel().sendMessage(emb.build()).queue();
            }

               //UPTIME
           else if (args[0].equalsIgnoreCase(prefix + "uptime")) {
                //Uptime stuff
                RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
                long uptime = runtimeMXBean.getUptime();
                long uptimeInSeconds = uptime / 1000;
                long numberOfHours = uptimeInSeconds / (60 * 60);
                long numberOfMinutes = (uptimeInSeconds / 60) - (numberOfHours * 60);
                long numberOfSeconds = uptimeInSeconds % 60;

               event.getChannel().sendMessage("Uptime: ``" + numberOfHours + " Hours, " + numberOfMinutes + " Min, " + numberOfSeconds + " Seconds``").queue();
           }

            //INVITE
           else if (args[0].equalsIgnoreCase(prefix + "invite")) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.ORANGE);
                embed.addField("Invite StickyBot to your server:", "[top.gg/StickyBot](https://top.gg/bot/628400349979344919)", false);
                event.getChannel().sendMessage(embed.build()).queue();

           }
                //POLL
           else if (args[0].equalsIgnoreCase(prefix + "poll")) {
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
           }
               //DICE ROLL
           else if (args[0].equalsIgnoreCase(prefix + "dice") || args[0].equalsIgnoreCase(prefix + "roll")) {
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
           }

           //USERINFO
           else if (args[0].equalsIgnoreCase(prefix + "userinfo")) {

               try {
                   User tagUser;
                   Member taggedMember;

                   if (event.getMessage().toString().contains("@")) {
                       tagUser = event.getMessage().getMentionedUsers().get(0);
                       taggedMember = event.getMessage().getMentionedMembers().get(0);
                   } else {
                       tagUser = event.getAuthor();
                       taggedMember = event.getMember();
                   }

                   EmbedBuilder emb = new EmbedBuilder();
                   String joinDateClean = taggedMember.getTimeJoined().getMonth() + " " + taggedMember.getTimeJoined().getDayOfMonth() + ", " + taggedMember.getTimeJoined().getYear();
                   String creationDateClean = taggedMember.getTimeCreated().getMonth() + " " + taggedMember.getTimeCreated().getDayOfMonth() + ", " + taggedMember.getTimeCreated().getYear();

                   String daysJoined = numberOfDaysJoined(taggedMember);

                   emb.setThumbnail(tagUser.getAvatarUrl());
                   emb.setTitle("**-User Info-**");
                   emb.addField("Info for " + tagUser.getName() + "#" + tagUser.getDiscriminator(),
                           "**User ID:** ``" + tagUser.getId() + "``\n" +
                                   "**Nickname:** " + taggedMember.getEffectiveName() + "\n" +
                                   "**Join Date:** " + joinDateClean + " *(" + daysJoined + " days ago)*" + "\n" +
                                   //"**Join Position:** " + joinSpot + "\n" +
                                   "**Creation Date:** " + creationDateClean + " *(" + numberOfDaysCreated(taggedMember) + " days ago)*" + "\n" +
                                   //"**Status:** " + taggedMember.getOnlineStatus().getKey() + "\n" +
                                   "**Tag: ** " + taggedMember.getAsMention() + "\n" +
                                   "**Nitro Boosting: ** " + boostCheck(taggedMember) + "\n" +
                                   "**Number of Roles:** " + taggedMember.getRoles().size()
                           , false);

                   emb.addField("**Roles: **", getRoles(taggedMember), false );


                   emb.setColor(taggedMember.getColor());

               if (taggedMember.hasPermission(Permission.ADMINISTRATOR)) {
                   emb.setFooter(tagUser.getName() + " is a Admin", tagUser.getAvatarUrl());
               } else {
                   emb.setFooter(tagUser.getName(), tagUser.getAvatarUrl());
               }
               event.getChannel().sendMessage(emb.build()).queue(m -> {
                   if (Integer.valueOf(daysJoined) == 365 || Integer.valueOf(daysJoined) == 730 || Integer.valueOf(daysJoined) == 1095 || Integer.valueOf(daysJoined) == 1460) {
                       m.addReaction("\uD83C\uDF89").queue();
                   }
               });

                 } catch (Exception e) {
                    event.getChannel().sendMessage("Please tag a member in the server.").queue();
                  }

           }


            //COIN FLIP
           else if (args[0].equalsIgnoreCase(prefix + "coinflip") || args[0].equalsIgnoreCase(prefix + "flip") || args[0].equalsIgnoreCase(prefix + "coin") || args[0].equalsIgnoreCase(prefix + "flipcoin")) {
                int random = (int) Math.round(Math.random());
                EmbedBuilder emb = new EmbedBuilder();
                emb.setTitle("-Coin Flip-");
                emb.setColor(Color.GRAY);

                if (random == 0) {
                    emb.setDescription("Coin Landed on **HEADS**!");
                } else {
                    emb.setDescription("Coin Landed on **TAILS**!");
                }
                emb.setFooter("Flipped by: " + event.getMember().getEffectiveName(), event.getMember().getUser().getAvatarUrl());
                event.getChannel().sendMessage(emb.build()).queue();
           }

                //WEBSITE
           else if (args[0].equalsIgnoreCase(prefix + "website") || args[0].equalsIgnoreCase(prefix + "site")) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.ORANGE);
                embed.setTitle("-StickyBot Website-");
                embed.setDescription("[www.stickybot.info](https://www.stickybot.info/)");
                event.getChannel().sendMessage(embed.build()).queue();

           }

                //SUPPORT
           else if (args[0].equalsIgnoreCase(prefix + "support") || args[0].equalsIgnoreCase(prefix + "supportserver")) {
               EmbedBuilder embed = new EmbedBuilder();
               embed.setColor(Color.ORANGE);
               embed.setTitle("-Join the Official StickyBot Support Server-");
               embed.setDescription("[StickyBot Support](https://discord.gg/SvNQTtf)");
               event.getChannel().sendMessage(embed.build()).queue();
           }

            //PREMIUM
            else if (args[0].equalsIgnoreCase(prefix + "premium") || args[0].equalsIgnoreCase(prefix + "stickybotpremium")) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.ORANGE);
                embed.setTitle("-Unlock StickyBot Premium for unlimited stickies, embed stickies, a custom prefix, and more!-");
                embed.setDescription("[StickyBot Premium](https://www.stickybot.info/premium)");
                event.getChannel().sendMessage(embed.build()).queue();

            //EMBED
            } else if (args[0].equalsIgnoreCase(prefix + "embed")) {

               try {
                    EmbedBuilder emb = new EmbedBuilder();
                    emb.setDescription(event.getMessage().getContentRaw().substring(7));
                    emb.setColor(event.getGuild().getMemberById(Main.botId).getColor());
                    event.getChannel().sendMessage(emb.build()).queue();
               } catch (Exception e) {
                    event.getChannel().sendMessage("Please use the format `?embed <message>`.").queue();
               }
                event.getMessage().delete().queue();
            }

            //PERM CHECK
            else if (args[0].equalsIgnoreCase(prefix + "permcheck")) {
                EmbedBuilder emb = new EmbedBuilder();
                emb.setTitle("**-StickyBot Server Permission Check-**");
                emb.setDescription("__StickyBot's Permissions in this server:__\n" +
                        "\nMessage History: `" + event.getMember().hasPermission(Permission.MESSAGE_HISTORY) +
                                   "`\nManage Messages: `" + event.getMember().hasPermission(Permission.MESSAGE_MANAGE) +
                                   "`\nEmbed Links: `" + event.getMember().hasPermission(Permission.MESSAGE_EMBED_LINKS) +
                                   "`\nAdd Message Reactions: `" + event.getMember().hasPermission(Permission.MESSAGE_ADD_REACTION) + "`");

                emb.setFooter(event.getMessage().getTimeCreated().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));

            event.getChannel().sendMessage(emb.setColor(Color.yellow).build()).queue();
            }
        }

    public static String boostCheck(Member member) {

        if(member.getTimeBoosted() == null){
            return "Member not boosting.";
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


