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

public class SlowSticky extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String[] args = event.getMessage().getContentRaw().split("\\s+");
        //Member stickyBot = event.getGuild().getMemberById(Main.botId);
        String channelId = event.getChannel().getId();

        String prefix = "?";
        if (Main.mapPrefix.containsKey(event.getGuild().getId())) {
            prefix = Main.mapPrefix.get(event.getGuild().getId());
        }




        if (event.getMessage().getContentRaw().startsWith(prefix + "stickslow ") && event.getMessage().getContentRaw().matches("[\\S]+\\s{2,}.*") && (permCheck(event.getMember())) && !event.getAuthor().isBot()) {
            event.getChannel().sendMessage(event.getMember().getAsMention() + " please only use one space after the `?stickslow` command!").queue();
            return;
        }

        if (args[0].equalsIgnoreCase(prefix + "stickslow") && (permCheck(event.getMember())) && !event.getAuthor().isBot()) {
            if (!Main.premiumGuilds.containsValue(event.getGuild().getId())) {
                EmbedBuilder em = new EmbedBuilder();
                em.setTitle("**Whoops! This is a StickyBot Premium Command!** ")
                        .addField("__StickyBot Premium__ allows for slow stickys plus other awesome features!", "Try it for free: [www.stickybot.com](https://www.stickybot.info)", false);
                event.getChannel().sendMessage(em.setColor(Color.ORANGE).build()).queue();
            } else {
                try {
                    //remove last sticky message if there is one (user used sticky command while already having one)

                if(Main.mapMessage.containsKey(event.getChannel().getId())) {
                        Main.mapMessage.remove(channelId);

                        if(Main.mapDeleteId.get(channelId) != null) {
                            event.getChannel().deleteMessageById(Main.mapDeleteId.get(channelId)).queue(null, (error) -> {});
                        }
                    }


                    if(Main.mapDeleteId2.get(channelId) != null) {
                        event.getChannel().deleteMessageById(Main.mapDeleteId2.get(channelId)).queue(null, (error) -> {});
                    }


                        for (Emote emote : event.getMessage().getEmotes()) {
                            event.getGuild().retrieveEmoteById(emote.getId()).queue(success -> {}, failure -> {
                                event.getChannel().sendMessage(event.getMember().getAsMention() + " Error: Please only use emotes that are from this server.").queue();
                                Main.mapMessage.remove(event.getChannel().getId());
                                removeDB(channelId);
                            });
                        }

                        String input = event.getMessage().getContentRaw();

                        if (event.getMessage().getContentRaw().contains(prefix + "stickslow \n")) {
                            event.getChannel().sendMessage(event.getMember().getAsMention() + " Error: Please provide text after `" + prefix + "stickslow` before using a new line.").queue();
                            return;
                        }


                        String [] arr = input.split(" ", 2);
                        Main.mapMessageSlow.put(event.getChannel().getId(), arr[1]);
                        removeDB(channelId);
                        addDB(channelId,(arr[1]));


                    event.getChannel().sendMessage(Main.mapMessageSlow.get(channelId)).queue(m -> Main.mapDeleteId2.put(event.getChannel().getId(), m.getId()));
                    event.getMessage().addReaction("\u2705").queue();
                } catch (Exception e) {
                    event.getChannel().sendMessage(event.getMember().getAsMention() + " please use this format: `?stickslow <message>`.").queue();
                }
            }



        } else if (args[0].equalsIgnoreCase(prefix + "stickslow") && (!permCheck(event.getMember() ))) {
            //Adds X emote
            event.getMessage().addReaction("\u274C").queue();
            //event.getChannel().sendMessage(event.getMember().getAsMention() + " you need the global `Manage Messages` permission to use this command!").queue();
        }

        else if ( (args[0].equalsIgnoreCase(prefix + "stickstop") || args[0].equalsIgnoreCase(prefix + "unstick")) && (permCheck(event.getMember() ))) {
            Main.mapMessageSlow.remove(channelId);

            if(Main.mapDeleteId2.get(channelId) != null) {
                event.getChannel().deleteMessageById(Main.mapDeleteId2.get(channelId)).queue(null, (error) -> {});
            }

            removeDB(channelId);
            event.getMessage().addReaction("\u2705").queue();
        } else if ( (args[0].equalsIgnoreCase(Main.prefix + "stickstop") || args[0].equalsIgnoreCase(Main.prefix + "unstick")) && (!permCheck(event.getMember() ))) {
            //Adds X mark
            event.getMessage().addReaction("\u274C").queue();
            //event.getChannel().sendMessage(event.getMember().getAsMention() + " you need the global `Manage Messages` permission to use this command!").queue();
        }

        if (Main.mapMessageSlow.get(channelId) != null) {


            List<Message> history = event.getChannel().getHistory().retrievePast(18).complete();



            try {
                for(Message m : history.subList(0, 13)) {
                    //if message is sticky message
                    if(m.getContentRaw().equals(Main.mapMessageSlow.get(channelId))) {
                        //if message is older then 35 sec
                        if(m.getTimeCreated().compareTo(OffsetDateTime.now().minusSeconds(35)) < 0) {
                            m.delete().queue(null, (error) -> {});
                            event.getChannel().sendMessage(Main.mapMessageSlow.get(channelId)).queue(mes -> Main.mapDeleteId2.put(channelId, mes.getId()));
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
                for(Message m : history.subList(0, 13)) {
                    if(m.getContentRaw().equals(Main.mapMessageSlow.get(channelId))) {
                        check = true;
                    }
                }
            } catch (Exception e) {
                //do nothing
            }



            if(!check) {
                if(Main.mapDeleteId2.get(channelId) != null) {
                    event.getChannel().deleteMessageById(Main.mapDeleteId2.get(channelId)).queue(null, (error) -> {});
                }

                //If message send fails, stickstop
                if (event.getChannel().canTalk()) {
                    event.getChannel().sendMessage(Main.mapMessageSlow.get(channelId)).queue();
                } else {
                    Main.mapMessageSlow.remove(channelId);
                    removeDB(channelId);
                    System.out.println("StickStop Override due to missing write permission");
                }

            }

            //Added to make sure it does not bug and send two stickies (next 5 lines)

            List<Message> indexes = new ArrayList<>();

            for (Message mes : history) {
                if (mes.getContentRaw().equals(Main.mapMessageSlow.get(channelId))) {
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
            String sql = "INSERT INTO slowMessages (channelId, message) VALUES ( ?, ?)";
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
            String sql = "DELETE FROM slowMessages WHERE channelId='" + channelId + "';";
            myStmt.execute(sql);
            myStmt.close();
        } catch ( SQLException e) {
            e.printStackTrace();
        }
    }


}
