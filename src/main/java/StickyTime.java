import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.*;
import java.text.NumberFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
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

        if (args[0].equalsIgnoreCase(prefix + "stick") && (permCheck(event.getMember())) && !event.getAuthor().isBot()) {
            if (guildHasSticky(event.getGuild().getId()) && !Main.premiumGuilds.containsValue(event.getGuild().getId())) {
               EmbedBuilder em = new EmbedBuilder();
                       em.setTitle("**There is already an active sticky message in this server!** ")
                               .setDescription("Stop the sticky in " + event.getGuild().getTextChannelById(getActiveStickyChannelId(event.getGuild().getId())).getAsMention() + " using `" + prefix + "stickstop` first.")
                               .addField("__StickyBot Premium__ allows for stickies in as many channels as you like plus other awesome features!", "Try it for free: [www.stickybot.com](https://www.stickybot.info)", false);
                       event.getChannel().sendMessage(em.setColor(Color.ORANGE).build()).queue();

            } else {
                    try {
                    //remove last sticky message if there is one (user used sticky command while already having one)
                    if(Main.mapDeleteId.get(channelId) != null) {
                        event.getChannel().deleteMessageById(Main.mapDeleteId.get(channelId)).queue(null, (error) -> {});
                    }

                    //premium sticky
                    if (Main.premiumGuilds.containsValue(event.getGuild().getId())) {

                        for (Emote emote : event.getMessage().getEmotes()) {
                            event.getGuild().retrieveEmoteById(emote.getId()).queue(success -> {}, failure -> {
                                event.getChannel().sendMessage("Error: Please only use emotes that are from this server.").queue();
                                Main.mapMessage.remove(event.getChannel().getId());
                                removeDB(channelId);
                            });
                        }

                        String input = event.getMessage().getContentRaw();

                        if (event.getMessage().getContentRaw().contains(prefix + "stick \n")) {
                            event.getChannel().sendMessage("Error: Please provide text after `" + prefix + "stick` before using a new line.").queue();
                            return;
                        }


                        String [] arr = input.split(" ", 2);
                        Main.mapMessage.put(event.getChannel().getId(), arr[1]);
                        removeDB(channelId);
                        addDB(channelId,(arr[1]));
                    } else

                        //classic sticky
                        if (!Main.premiumGuilds.containsValue(event.getGuild().getId())) {

                            if (event.getMessage().getContentRaw().contains(prefix + "stick \n")) {
                                event.getChannel().sendMessage("Error: Please provide text after `" + prefix + "stick` before using a new line.").queue();
                                return;
                            }

                            for (Emote emote : event.getMessage().getEmotes()) {
                                event.getGuild().retrieveEmoteById(emote.getId()).queue(success -> {}, failure -> {
                                    event.getChannel().sendMessage("Error: Please only use emotes that are from this server.").queue();
                                    Main.mapMessage.remove(event.getChannel().getId());
                                    removeDB(channelId);
                                });
                            }






                            String o = event.getMessage().getContentRaw();
                            String [] arr = o.split(" ", 2);

                            Main.mapMessage.put(event.getChannel().getId(), "__**Stickied Message:**__\n\n" + arr[1]);
                            removeDB(channelId);
                            addDB(channelId,("__**Stickied Message:**__\n\n" + arr[1]));
                        }
                        
                    event.getChannel().sendMessage(Main.mapMessage.get(channelId)).queue(m -> Main.mapDeleteId.put(event.getChannel().getId(), m.getId()));
                    event.getMessage().addReaction("\u2705").queue();
                } catch (Exception e) {
                    event.getChannel().sendMessage("Please use this format: `?stick <message>`.").queue();
                }
            }



        } else if (args[0].equalsIgnoreCase(prefix + "stick") && (!permCheck(event.getMember() ))) {
            //Adds X emote
            event.getMessage().addReaction("\u274C").queue();
            event.getChannel().sendMessage("You need the global `Manage Messages` permission to use this command!").queue();
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
            event.getChannel().sendMessage("You need the global `Manage Messages` permission to use this command!").queue();
        }

        if (Main.mapMessage.get(channelId) != null) {

            List<Message> history = event.getChannel().getHistory().retrievePast(8).complete();

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


            //gets set to true if one of last five messages contains sticky message.
            Boolean check = false;

            for(Message m : history.subList(0, 5)) {
                if(m.getContentRaw().equals(Main.mapMessage.get(channelId))) {
                    check = true;
                }
            }
            if(!check) {
                if(Main.mapDeleteId.get(channelId) != null) {
                    event.getChannel().deleteMessageById(Main.mapDeleteId.get(channelId)).queue(null, (error) -> {});
                }
                event.getChannel().sendMessage(Main.mapMessage.get(channelId)).queue();
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

        for (String id : channelIds) {
            if (Main.mapMessage.containsKey(id)) {
                return true;
            }
        }
        return false;
    }

    public String getActiveStickyChannelId(String guildId) {
        List<String> channelIds = Main.jda.getGuildById(guildId).getTextChannels().stream().map(textChannel -> textChannel.getId()).collect(Collectors.toList());

        for (String id : channelIds) {
            if (Main.mapMessage.containsKey(id)) {
                return id;
            }
        }
        return "null";
    }
}
