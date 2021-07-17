import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
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

            int shardId = (int) ((event.getGuild().getIdLong() >>> 22) % Main.jda.getShardsTotal());

            event.getMessage().reply(">>> **Pong!**" + "\nThis Shard: (`" + shardId + "`) Ping: `" + Main.jda.getShardById(shardId).getGatewayPing() + "`ms."
                    + "\nAll Shards Average Ping: `" + Main.jda.getAverageGatewayPing() + "`ms.").queue();
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
                eb.setColor(Color.orange);
                eb.setTitle("**-StickyBot Information-**");
                eb.addField("Developed By:", "P_O_G#2222\n(`182729649703485440`)", false);
                eb.addField("Website:", "[www.stickybot.info](https://www.stickybot.info/)", false);
                eb.addField("Ping:", Main.jda.getAverageGatewayPing() + "ms", false);
                eb.addField("Uptime:", "``" + numberOfHours + " Hours, " + numberOfMinutes + " Min, " + numberOfSeconds + " Seconds``", true);
                eb.addField("Shards: ", "Shard " + "**" + ((event.getGuild().getIdLong() >>> 22) % Main.jda.getShardsTotal()) + " of " + (Main.jda.getShardsTotal()) + "**", false);
                eb.addField("Guilds:", "StickyBot is in **" + NumberFormat.getInstance().format(Main.jda.getGuildCache().size()) + "** Guilds", false);
                eb.addField("Support Server:", "[StickyBot Support](https://discord.gg/SvNQTtf)", false);
                eb.addField("Vote for StickyBot:", "[top.gg/stickybot](https://top.gg/bot/628400349979344919)", false);
                eb.addField("Premium: ", "[www.stickybot.info/premium](https://www.stickybot.info/premium)", false);
                eb.addField("**Commands:** ", "Do ``?commands`` or ``?help``", false);
                eb.addField("Donate:", "[paypal.me/sircam19](https://www.paypal.me/sircam19)", false);
                eb.setFooter("StickyBot is Made with Java & JDA", Main.jda.getShards().get(0).getSelfUser().getAvatarUrl());

                event.getMessage().reply(eb.build()).setActionRow(Button.link("https://www.stickybot.info", "Website").withEmoji(Emoji.fromMarkdown("<:StickyBotCircle:693004145065590856>")),
                        Button.link("https://discord.com/invite/SvNQTtf", "Support Server").withEmoji(Emoji.fromMarkdown("<:discordEmote:853160010305765376>")),
                        Button.link("https://www.stickybot.info/premium", "Premium").withEmoji(Emoji.fromMarkdown("\uD83E\uDDE1")),
                        Button.link("https://docs.stickybot.info", "Docs").withEmoji(Emoji.fromMarkdown("<:iBlue:860060995979706389>"))).queue();
           }

                //DONATE
           else if (args[0].equalsIgnoreCase(prefix + "donate") || args[0].equalsIgnoreCase(prefix + "dono")) {
               EmbedBuilder embed = new EmbedBuilder();
               embed.setColor(Color.ORANGE);
               embed.setTitle("Help Keep StickyBot up and running smoothly!");
               embed.setDescription("[**paypal.me/sircam19**](https://www.paypal.me/sircam19)");
               embed.setFooter("Include your Discord name or ID in the donation to receive the @donator tag in the support server.");
               event.getMessage().addReaction("\u2764").queue();
               event.getMessage().reply(embed.build()).setActionRow(Button.link("https://www.paypal.me/sircam19", "PayPal").withEmoji(Emoji.fromMarkdown("<:paypal:853161440902250496>"))).queue();

           }


                //SERVER INFO
            else if (args[0].equalsIgnoreCase(prefix + "serverinfo")) {
                if (Main.mapDisable.containsKey(event.getGuild().getId())) {
                    return;
                }
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


                emb.setColor(Color.orange);
                emb.setFooter(event.getGuild().getName(), event.getGuild().getIconUrl());

                event.getMessage().reply(emb.build()).queue();
            }

               //UPTIME
           else if (args[0].equalsIgnoreCase(prefix + "uptime")) {
                if (Main.mapDisable.containsKey(event.getGuild().getId())) {
                    return;
                }
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
                event.getMessage().replyEmbeds(embed.build()).queue();

           }
                //POLL
           else if (args[0].equalsIgnoreCase(prefix + "poll")) {
                if (Main.mapDisable.containsKey(event.getGuild().getId())) {
                    return;
                }
               String pollQ;

                try {
                   if (!args[1].isEmpty()) {
                       pollQ = String.join(" ", args).substring(5);

                       EmbedBuilder emb = new EmbedBuilder();
                       emb.setColor(event.getGuild().getMemberById(Main.botId).getColor());
                       emb.setFooter("Poll by: " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());

                       emb.setDescription(pollQ.trim());

                       //adds reactions to poll message
                       event.getChannel().sendMessage(emb.build()).queue(m -> {
                           m.addReaction("\ud83d\udc4d").queue();
                           m.addReaction("\uD83D\uDC4E").queue();
                           m.addReaction("\uD83E\uDD37").queue();
                       });
                       event.getMessage().delete().queue();
                   }
               } catch (Exception e) {
                   event.getMessage().reply("Please use this format ``?poll <your question>``.\nExample: ``?poll Is this a cool command?``").queue();
               }

           }

              //USERINFO
           else if (args[0].equalsIgnoreCase(prefix + "userinfo")) {
                if (Main.mapDisable.containsKey(event.getGuild().getId())) {
                    return;
                }
               try {
                   User tagUser;
                   Member taggedMember;

                   if (event.getMessage().getContentRaw().contains("@")) {
                       tagUser = event.getMessage().getMentionedUsers().get(0);
                       taggedMember = event.getMessage().getMentionedMembers().get(0);
                   } else if (args.length == 2 && args[1].length() == 18 && !args[0].contains("[a-zA-Z]+")) {
                        try {
                            taggedMember = event.getGuild().retrieveMemberById(args[1]).complete();
                            tagUser = Main.jda.retrieveUserById(args[1]).complete();
                        } catch (Exception e) {
                            e.printStackTrace();
                            event.getChannel().sendMessage(event.getMember().getAsMention() + " this user is not in this server.").queue();
                            return;
                       }
                   } else {
                       tagUser = event.getAuthor();
                       taggedMember = event.getMember();
                   }

                   EmbedBuilder emb = new EmbedBuilder();
                   String joinDateClean = taggedMember.getTimeJoined().getMonth() + " " + taggedMember.getTimeJoined().getDayOfMonth() + ", " + taggedMember.getTimeJoined().getYear();
                   String creationDateClean = taggedMember.getTimeCreated().getMonth() + " " + taggedMember.getTimeCreated().getDayOfMonth() + ", " + taggedMember.getTimeCreated().getYear();

                   String daysJoined = numberOfDaysJoined(taggedMember);

                   String badges = tagUser.getFlags().toString();
                   String badgesEmotes = "";

                   if(badges.contains("BUG_HUNTER")) {
                        badgesEmotes += "<:bughunter:865689196570869770> ";
                   }if(badges.contains("MODERATOR")) {
                       badgesEmotes += "<:mod:865689196529188884> ";
                   }if(badges.contains("EARLY_SUPPORTER")) {
                       badgesEmotes += "<:supporter:865689196589219880> ";
                   }if(badges.contains("BALANCE")) {
                       badgesEmotes += "<:balance:865689196466143232> ";
                   }if(badges.contains("BRAVERY")) {
                       badgesEmotes += "<:bravery:865689196597084200> ";
                   }if(badges.contains("BRILLIANCE")) {
                       badgesEmotes += "<:brilliance:865692499648446505> ";
                   }if(badges.contains("PARTNER")) {
                       badgesEmotes += "<:partner:865689196668911656> ";
                   }if(badges.contains("STAFF")) {
                       badgesEmotes += "<:staff:865689196568248351> ";
                   }if(badges.contains("DEVELOPER")) {
                       badgesEmotes += "<:DiscordBotDev:730213279707693167> ";
                   }if(!boostCheck(taggedMember).equals("Member not boosting.")) {
                       badgesEmotes += "<:boost:865689196449234985> ";
                   }if(tagUser.getEffectiveAvatarUrl().endsWith(".gif")) {
                       badgesEmotes += "<:nitro:865689196249612299> ";
                   }

                   else {
                       badgesEmotes = "None";
                   }

                   emb.setThumbnail(tagUser.getEffectiveAvatarUrl());
                   emb.setTitle("**-User Info-**");
                   emb.addField("Info for " + tagUser.getName() + "#" + tagUser.getDiscriminator(),
                           "<:settings:865689196567724052> **User ID:** ``" + tagUser.getId() + "``\n" +
                                   "<:add:865689196492750848> **Nickname:** " + taggedMember.getEffectiveName() + "\n" +
                                   "<:rules:865689196332056607> **Join Date:** " + joinDateClean + " *(" + daysJoined + " days ago)*" + "\n" +
                                   //"**Join Position:** " + joinSpot + "\n" +
                                   "<:rules:865689196332056607> **Creation Date:** " + creationDateClean + " *(" + numberOfDaysCreated(taggedMember) + " days ago)*" + "\n" +
                                   //"**Status:** " + taggedMember.getOnlineStatus().getKey() + "\n" +
                                   "<:info:865689196521193482> **Badges: **" + badgesEmotes + "\n" +
                                   "<:__:865689196446089256> **Tag: ** " + taggedMember.getAsMention() + "\n" +
                                   "<:boost:865689196449234985> **Nitro Boosting: ** " + boostCheck(taggedMember) + "\n" +
                                   "<:roles:865690170229391420> **Number of Roles:** " + taggedMember.getRoles().size()
                           , false);

                   emb.addField("<:roles:865690170229391420> **Roles: **", getRoles(taggedMember), false );


                   emb.setColor(taggedMember.getColor());

               if (taggedMember.hasPermission(Permission.ADMINISTRATOR)) {
                   emb.setFooter(tagUser.getName() + " is a Admin", tagUser.getEffectiveAvatarUrl());
               } else {
                   emb.setFooter(tagUser.getName(), tagUser.getEffectiveAvatarUrl());
               }

               event.getMessage().reply(emb.build()).setActionRow(Button.link(event.getMember().getUser().getEffectiveAvatarUrl(), "Avatar")).queue(m -> {
                   if (Integer.valueOf(daysJoined) == 365 || Integer.valueOf(daysJoined) == 730 || Integer.valueOf(daysJoined) == 1095 || Integer.valueOf(daysJoined) == 1460 || Integer.valueOf(daysJoined) == 1825 || Integer.valueOf(daysJoined) == 2190) {
                       m.addReaction("\uD83C\uDF89").queue();
                   }
               });

                 } catch (Exception e) {
                    event.getMessage().reply("Please tag a member in the server, provide a user ID, or leave blank to get your own user info.").queue();
                    e.printStackTrace();
                  }

           }


            //COIN FLIP
           else if (args[0].equalsIgnoreCase(prefix + "coinflip") || args[0].equalsIgnoreCase(prefix + "flip") || args[0].equalsIgnoreCase(prefix + "coin") || args[0].equalsIgnoreCase(prefix + "flipcoin")) {
                if (Main.mapDisable.containsKey(event.getGuild().getId())) {
                    return;
                }
                int random = (int) Math.round(Math.random());
                EmbedBuilder emb = new EmbedBuilder();
                emb.setTitle("-Coin Flip-");
                emb.setColor(Color.orange);

                if (random == 0) {
                    emb.setDescription("Coin Landed on **HEADS**!");
                } else {
                    emb.setDescription("Coin Landed on **TAILS**!");
                }
                emb.setFooter("Flipped by: " + event.getMember().getEffectiveName(), event.getMember().getUser().getAvatarUrl());
                event.getMessage().reply(emb.build()).queue();
           }

                //WEBSITE
           else if (args[0].equalsIgnoreCase(prefix + "website") || args[0].equalsIgnoreCase(prefix + "site")) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.ORANGE);
                embed.setTitle("-StickyBot Website-");
                embed.setDescription("[www.stickybot.info](https://www.stickybot.info/)");
                event.getMessage().reply(embed.build()).setActionRow(Button.link("https://www.stickybot.info", "Website").withEmoji(Emoji.fromMarkdown("<:StickyBotCircle:693004145065590856>"))).queue();

           }

                //SUPPORT
           else if (args[0].equalsIgnoreCase(prefix + "support") || args[0].equalsIgnoreCase(prefix + "supportserver")) {
               EmbedBuilder embed = new EmbedBuilder();
               embed.setColor(Color.ORANGE);
               embed.setTitle("-Join the Official StickyBot Support Server-");
               embed.setDescription("[StickyBot Support](https://discord.gg/SvNQTtf)");
                event.getMessage().replyEmbeds(embed.build()).setActionRow(
                       Button.link("https://discord.com/invite/SvNQTtf", "Support Server").withEmoji(Emoji.fromMarkdown("<:discordEmote:853160010305765376>")),
                       Button.link("https://www.stickybot.info", "Website").withEmoji(Emoji.fromMarkdown("<:StickyBotCircle:693004145065590856>")),
                        Button.link("https://docs.stickybot.info", "Docs").withEmoji(Emoji.fromMarkdown("<:iBlue:860060995979706389>"))).queue();
           }

            //PREMIUM
            else if (args[0].equalsIgnoreCase(prefix + "premium") || args[0].equalsIgnoreCase(prefix + "stickybotpremium")) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.ORANGE);
                embed.setTitle("-StickyBot Premium-");
                embed.addField("Features:", "-Unlimited stickied messages." +
                        "\n-Use Custom Embeds as stickies." +
                        "\n-Create slower posting stickies." +
                        "\n-Custom Prefix." +
                        "\n-Removes \"Stickied Message:\" header." +
                        "\n-Premium support." +
                        "\n-More to come!", false);
                embed.addField("Learn More:", "[`www.stickybot.info/premium`](https://www.stickybot.info/premium)", false);
                event.getMessage().replyEmbeds(embed.build()).setActionRow(Button.link("https://www.stickybot.info/premium", "Premium").withEmoji(Emoji.fromMarkdown("\uD83E\uDDE1")),
                        Button.link("https://www.stickybot.info/manage-subscription", "Manage Subscription").withEmoji(Emoji.fromMarkdown("<:StickyBotCircle:693004145065590856>"))).queue();

            //EMBED
            } else if (args[0].equalsIgnoreCase(prefix + "embed")) {
                if (Main.mapDisable.containsKey(event.getGuild().getId())) {
                    return;
                }
                if (event.getMessage().getContentRaw().length() > 1000) {
                    event.getMessage().reply("Error: text is too long, text must be under 1000 characters.").queue();
                    return;
                }
               try {
                   EmbedBuilder emb = new EmbedBuilder();
                    emb.setDescription(event.getMessage().getContentRaw().replace(prefix + "embed", ""));
                    emb.setColor(event.getGuild().getMemberById(Main.botId).getColor());
                    emb.setFooter("Embed By: " + event.getMember().getUser().getName());
                    event.getChannel().sendMessage(emb.build()).queue();
               } catch (Exception e) {
                    event.getChannel().sendMessage("Please use the format `?embed <message>`.").queue();
               }
                event.getMessage().delete().queue();
            }

            //PERM CHECK
            else if (args[0].equalsIgnoreCase(prefix + "permcheck")) {

                String mH;
                String mM;
                String eL;
                String aMR;
                String uEE;


            if (event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_HISTORY)) {mH = "java";} else {mH = "c";}
            if (event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_MANAGE)) {mM = "java";} else {mM = "c";}
            if (event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_EMBED_LINKS)) {eL = "java";} else {eL = "c";}
            if (event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_ADD_REACTION)) {aMR = "java";} else {aMR = "c";}
            if (event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_EXT_EMOJI)) {uEE = "java";} else {uEE = "c";}


            String result = "**StickyBot has the following permissions in this CHANNEL:**\n\n";
                result +=
                        "```" + mH + "\nMessage History: " + event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_HISTORY) + "```" +
                                   "```" + mM + "\nManage Messages: " + event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_MANAGE) + "```" +
                                   "```" + eL + "\nEmbed Links: " + event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_EMBED_LINKS) + "```" +
                                   "```" + aMR + "\nAdd Message Reactions: " + event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_ADD_REACTION) + "```" +
                                    "```" + uEE + "\nUse External Emojis: " + event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_EXT_EMOJI) + "```";

                if(mH.equals("c") || mM.equals("c") || eL.equals("c") || aMR.equals("c") || uEE.equals("c")) {
                    result += "\n__Make sure all these are set to true in you channel/server settings for StickyBot for function properly.__\n";
                }

                result += "\nCommand used: ``" + event.getMessage().getTimeCreated().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)) + "`` by: " + event.getMember().getAsMention();

                event.getChannel().sendMessage(result).queue();
            }
            //IF BOT IS MENTIONED
            else if (!event.getMessage().getMentionedMembers().isEmpty() && event.getMessage().getMentionedMembers().get(0).getUser().getId().equals(Main.botId)) {
                event.getMessage().reply("**Hey!**\uD83D\uDC4B\nMy prefix in this server is: `" + prefix + "`.\nUse the `" + prefix + "help` command to get a list of commands.").queue();

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


