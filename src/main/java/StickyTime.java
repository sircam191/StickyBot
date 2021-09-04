import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StickyTime extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String[] args = event.getMessage().getContentRaw().split("\\s+");
        //Member stickyBot = event.getGuild().getMemberById(Main.botId);
        String channelId = event.getChannel().getId();

        String prefix = "?";
        if (Main.mapPrefix.containsKey(event.getGuild().getId())) {
            prefix = Main.mapPrefix.get(event.getGuild().getId());
        }


        if (event.getMessage().getContentRaw().startsWith(prefix + "stick ") && event.getMessage().getContentRaw().matches("[\\S]+\\s{2,}.*") && (permCheck(event.getMember())) && !event.getAuthor().isBot()) {
            event.getMessage().reply(event.getMember().getAsMention() + " please only use one space after the `?stick` command!").queue();
            return;
        }

        if (args[0].equalsIgnoreCase(prefix + "stick") && (permCheck(event.getMember())) && !event.getAuthor().isBot()) {
            if (guildHasSticky(event.getGuild().getId()) && !Main.premiumGuilds.containsValue(event.getGuild().getId())) {
               EmbedBuilder em = new EmbedBuilder();

               String channelNames = "";
               if (getActiveStickyChannelId(event.getGuild().getId()).size() == 1) {
                   channelNames = event.getGuild().getTextChannelById(getActiveStickyChannelId(event.getGuild().getId()).get(0)).getAsMention();
               } else {
                   channelNames = event.getGuild().getTextChannelById(getActiveStickyChannelId(event.getGuild().getId()).get(0)).getAsMention() + " or " + event.getGuild().getTextChannelById(getActiveStickyChannelId(event.getGuild().getId()).get(1)).getAsMention();;
               }

               em.setTitle("**There is already 2 active sticky messages in this server!** ")
                               .setDescription("Stop a sticky in " + channelNames + " using `" + prefix + "stickstop` first.")
                               .addField("__StickyBot Premium__ lets you create sticky embeds with a custom bot profile picture and name! Plus stickies in as many channels as you like with awesome customization!", "Learn more: [www.stickybot.com](https://www.stickybot.info)", false);
                event.getMessage().replyEmbeds(em.setColor(Color.ORANGE).build()).queue();

            } else {

                event.getChannel().getHistory().retrievePast(8).queue(count -> {

                    String prefix2 = "?";
                    if (Main.mapPrefix.containsKey(event.getGuild().getId())) {
                        prefix2 = Main.mapPrefix.get(event.getGuild().getId());
                    }

                    if(count.size() < 8) {
                        event.getMessage().reply(event.getMember().getAsMention() + " There needs to be at least 8 messages in this channel for stickies to work!").queue();

                    } else {


                        if(Main.mapMessageSlow.containsKey(event.getChannel().getId())) {
                            Main.mapMessageSlow.remove(channelId);

                            if(Main.mapDeleteId2.get(channelId) != null) {
                                event.getChannel().deleteMessageById(Main.mapDeleteId2.get(channelId)).queue(null, (error) -> {});
                            }
                        }

                        try {
                            //remove last sticky message if there is one (user used sticky command while already having one)
                            if(Main.mapDeleteId.get(channelId) != null) {
                                event.getChannel().deleteMessageById(Main.mapDeleteId.get(channelId)).queue(null, (error) -> {});
                            }

                            //premium sticky
                            if (Main.premiumGuilds.containsValue(event.getGuild().getId())) {

                                for (Emote emote : event.getMessage().getEmotes()) {
                                    event.getGuild().retrieveEmoteById(emote.getId()).queue(success -> {}, failure -> {
                                        event.getMessage().reply(event.getMember().getAsMention() + " Error: Please only use emotes that are from this server.").queue();
                                        Main.mapMessage.remove(event.getChannel().getId());
                                        removeDB(channelId);
                                    });
                                }

                                String input = event.getMessage().getContentRaw();

                                if (event.getMessage().getContentRaw().contains(prefix2 + "stick \n")) {
                                    event.getMessage().reply(event.getMember().getAsMention() + " Error: Please provide text after `" + prefix2 + "stick` before using a new line.").queue();
                                    return;
                                }


                                String [] arr = input.split(" ", 2);
                                Main.mapMessage.put(event.getChannel().getId(), arr[1].trim());
                                removeDB(channelId);
                                addDB(channelId,(arr[1]).trim());
                            } else

                                //classic sticky
                                if (!Main.premiumGuilds.containsValue(event.getGuild().getId())) {

                                    if (event.getMessage().getContentRaw().contains(prefix2 + "stick \n")) {
                                        event.getMessage().reply(event.getMember().getAsMention() + " Error: Please provide text after `" + prefix2 + "stick` before using a new line.").queue();
                                        return;
                                    }

                                    for (Emote emote : event.getMessage().getEmotes()) {
                                        event.getGuild().retrieveEmoteById(emote.getId()).queue(success -> {}, failure -> {
                                            event.getMessage().reply(event.getMember().getAsMention() + " Error: Please only use emotes that are from this server.").queue();
                                            Main.mapMessage.remove(event.getChannel().getId());
                                            removeDB(channelId);
                                        });
                                    }


                                    String o = event.getMessage().getContentRaw();
                                    String [] arr = o.split(" ", 2);

                                    Main.mapMessage.put(event.getChannel().getId(), "__**Stickied Message:**__\n\n" + arr[1].trim());
                                    removeDB(channelId);
                                    addDB(channelId,("__**Stickied Message:**__\n\n" + arr[1]).trim());
                                }

                            event.getChannel().sendMessage(Main.mapMessage.get(channelId)).queue(m -> Main.mapDeleteId.put(event.getChannel().getId(), m.getId()));
                            event.getMessage().addReaction("\u2705").queue();
                        } catch (Exception e) {
                            event.getChannel().sendMessage(event.getMember().getAsMention() + " please use this format: `?stick <message>`.").queue();
                            e.printStackTrace();
                        }
                    }
                });
            }
        } else if (args[0].equalsIgnoreCase(prefix + "stick") && (!permCheck(event.getMember() ))) {
            //Adds X emote
            event.getMessage().addReaction("\u274C").queue();
            event.getMessage().reply(event.getMember().getAsMention() + " you need the global `Manage Messages` permission to use this command!").queue();
        }

        else if ( (args[0].equalsIgnoreCase(prefix + "stickstop") || args[0].equalsIgnoreCase(prefix + "unstick")) && (permCheck(event.getMember() ))) {
            Main.mapMessage.remove(channelId);

            if(Main.mapDeleteId.get(channelId) != null) {
                event.getChannel().deleteMessageById(Main.mapDeleteId.get(channelId)).queue(null, (error) -> {});
            }

            removeDB(channelId);
            event.getMessage().addReaction("\u2705").queue();
        } else if ( (args[0].equalsIgnoreCase(Main.prefix + "stickstop") || args[0].equalsIgnoreCase(Main.prefix + "unstick")) && (!permCheck(event.getMember() ))) {
            //Adds X mark
            event.getMessage().addReaction("\u274C").queue();
            event.getMessage().reply(event.getMember().getAsMention() + " you need the global `Manage Messages` permission to use this command!").queue();
        }

        if (Main.mapMessage.get(channelId) != null) {

            event.getChannel().getHistory().retrievePast(8).queue(history -> {

                try {
                    for(Message m : history.subList(0, 5)) {
                        //if message is sticky message
                        if(m.getContentRaw().equals(Main.mapMessage.get(channelId))) {
                            //if message is older then 30 sec
                            if(m.getTimeCreated().compareTo(OffsetDateTime.now().minusSeconds(15)) < 0) {
                                m.delete().queue(null, (error) -> {});
                                event.getChannel().sendMessage(Main.mapMessage.get(channelId)).queue(mes -> Main.mapDeleteId.put(channelId, mes.getId()));
                            }
                            break;
                        }
                    }
                } catch (Exception e) {
                    //do nothing
                }

                //gets set to true if one of last five messages contains sticky message.
                Boolean check = false;

                try {
                    for(Message m : history.subList(0, 5)) {
                        if(m.getContentRaw().equals(Main.mapMessage.get(channelId))) {
                            check = true;
                        }
                    }
                } catch (Exception e) {
                    //do nothing
                }

                if(!check) {
                    if(Main.mapDeleteId.get(channelId) != null) {
                        event.getChannel().deleteMessageById(Main.mapDeleteId.get(channelId)).queue(null, (error) -> {});
                    }

                    //If message send fails, stickstop
                    if (event.getChannel().canTalk()) {
                        event.getChannel().sendMessage(Main.mapMessage.get(channelId)).queue();
                    } else {
                        Main.mapMessage.remove(channelId);
                        removeDB(channelId);
                        System.out.println("StickStop Override due to missing write permission");
                    }
                }
                //Added to make sure it does not bug and send two stickies (next 5 lines)
                List<Message> indexes = new ArrayList<>();
                for (Message mes : history) {
                    if (mes.getContentRaw().equals(Main.mapMessage.get(channelId))) {
                        indexes.add(mes);
                    }
                }
                if (!indexes.isEmpty() && indexes.size() > 1) {
                    indexes.remove(0);
                    for (Message mess : indexes) {
                        mess.delete().queue(null, (error) -> {});
                    }
                }
            });
        }
    }

    public boolean permCheck(Member member) {
        if (member.hasPermission(Permission.MESSAGE_MANAGE)) {
            return true;
        } else {
            return false;
        }
    }

    public void addDB(String channelId, String message) {
        try {
            Connection dbConn = DriverManager.getConnection(Main.dbUrl,Main.dbUser,Main.dbPassword);
            String sql = "INSERT INTO newMessages (channelId, message) VALUES ( ?, ?)";
            PreparedStatement myStmt = dbConn.prepareStatement(sql);
            myStmt.setString(1, channelId);
            myStmt.setString(2, message);
            myStmt.execute();
            myStmt.close();

        } catch ( SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeDB(String channelId) {
        try {
            Connection dbConn = DriverManager.getConnection(Main.dbUrl,Main.dbUser,Main.dbPassword);
            Statement myStmt = dbConn.createStatement();
            String sql = "DELETE FROM newMessages WHERE channelId='" + channelId + "';";
            myStmt.execute(sql);
            myStmt.close();
        } catch ( SQLException e) {
            e.printStackTrace();
        }
    }

    //returns true if no active sticky is in channel, otherwise returns false.
    public boolean guildHasSticky(String guildId) {
        List<String> channelIds = Main.jda.getGuildById(guildId).getTextChannels().stream().map(textChannel -> textChannel.getId()).collect(Collectors.toList());
        int numStickies = 0;

        for (String id : channelIds) {
            if (Main.mapMessage.containsKey(id)) {
                numStickies += 1;
            }
        }
        if (numStickies >= 2) {
            return true;
        } else {
            return false;
        }
    }

    public List<String> getActiveStickyChannelId(String guildId) {
        List<String> channelIds = Main.jda.getGuildById(guildId).getTextChannels().stream().map(textChannel -> textChannel.getId()).collect(Collectors.toList());
        List<String> stickyChannelIDs = new ArrayList<>();

        for (String id : channelIds) {
            if (Main.mapMessage.containsKey(id)) {
                stickyChannelIDs.add(id);
            }
        }

        return stickyChannelIDs;
    }
}
