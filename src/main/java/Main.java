import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.*;
import java.text.NumberFormat;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class StickyTime extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

       if(event.getAuthor().isBot()) {
           return;
       }

        String[] args = event.getMessage().getContentRaw().split("\\s+");
        //Member stickyBot = event.getGuild().getMemberById(Main.botId);
        Guild stickyServer = event.getJDA().getGuildById("641158383138897941");
        String channelId = event.getChannel().getId();

        String prefix = "?";
        if(Main.mapPrefix.containsKey(event.getGuild().getId())) {
            prefix = Main.mapPrefix.get(event.getGuild().getId());
        }

        if (args[0].equalsIgnoreCase(prefix + "stick") && (permCheck(event.getMember() ))) {
            if (guildHasSticky(event.getGuild().getId()) && !Main.premiumGuilds.containsValue(event.getGuild().getId())) {
                event.getChannel().sendMessage("**There is already a active sticky message in this server in:** " +
                        event.getGuild().getTextChannelById(getActiveStickyChannelId(event.getGuild().getId())).getAsMention() +
                        ".\n\n__StickyBot Premium__ allows for unlimited stickies and other features." +
                        "\nLearn more at https://www.stickybot.info").queue();
            } else {
                try {
                    //remove last sticky message if there is one (user used sticky command while already having one)
                    if(Main.mapDeleteId.get(channelId) != null) {
                        event.getChannel().deleteMessageById(Main.mapDeleteId.get(channelId)).queue();
                    }

                    //premium sticky
                    if (Main.premiumGuilds.containsValue(event.getGuild().getId())) {
                        Main.mapMessage.put(event.getChannel().getId(), event.getMessage().getContentRaw().substring(7));
                        removeDB(channelId);
                        addDB(channelId,(event.getMessage().getContentRaw().substring(7)));
                    } else

                    //classic sticky
                    if (!Main.premiumGuilds.containsValue(event.getGuild().getId())) {
                        Main.mapMessage.put(event.getChannel().getId(), "__**Stickied Message:**__\n\n" + event.getMessage().getContentRaw().substring(7));
                        removeDB(channelId);
                        addDB(channelId,("__**Stickied Message:**__\n\n" + event.getMessage().getContentRaw().substring(7)));
                    }

                    event.getChannel().sendMessage(Main.mapMessage.get(channelId)).queue(m -> Main.mapDeleteId.put(event.getChannel().getId(), m.getId()));
                    event.getMessage().addReaction("\u2705").queue();
                } catch (Exception e) {
                    event.getChannel().sendMessage("Please use this format: `?stick <message>`").queue();
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
                event.getChannel().deleteMessageById(Main.mapDeleteId.get(channelId)).queue();
            }

            removeDB(channelId);
            event.getMessage().addReaction("\u2705").queue();
        } else if ( (args[0].equalsIgnoreCase(Main.prefix + "stickstop") || args[0].equalsIgnoreCase(Main.prefix + "unstick")) && (!permCheck(event.getMember() ))) {
            //Adds X mark
            event.getMessage().addReaction("\u274C").queue();
            event.getMessage().addReaction("\u274C").queue();
            event.getChannel().sendMessage("You need the global `Manage Messages` permission to use this command!").queue();
        }

        if(Main.mapMessage.get(channelId) != null) {
            List<Message> history = event.getChannel().getHistory().retrievePast(5).complete();
            for(Message m : history) {
                //if message is sticky message
                if(m.getContentRaw().equals(Main.mapMessage.get(channelId))) {
                    //if message is older then 30 sec
                    if(m.getTimeCreated().compareTo(OffsetDateTime.now().minusSeconds(15)) < 0) {
                        m.delete().queue();
                        event.getChannel().sendMessage(Main.mapMessage.get(channelId)).queue(mes -> Main.mapDeleteId.put(channelId, mes.getId()));

                        //Added to make sure it does not bug and send two stickies (next 5 lines)
                        List<Message> messageListDelete = event.getChannel().getHistory().retrievePast(10).complete();
                        for (Message mess : messageListDelete.subList(1, 10)) {
                            if (mess.getContentRaw().contains(Main.mapMessage.get(channelId))) {
                                mess.delete().queue();
                            }
                        }
                    }
                    break;
                }
            }

            //gets set to true if one of last five messages contains sticky message.
            Boolean check = false;

            for(Message m : history) {
                if(m.getContentRaw().equals(Main.mapMessage.get(channelId))) {
                    check = true;
                }
            }
            if(!check) {
                if(Main.mapDeleteId.get(channelId) != null) {
                   event.getChannel().deleteMessageById(Main.mapDeleteId.get(channelId)).queue();
                }
                event.getChannel().sendMessage(Main.mapMessage.get(channelId)).queue();
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
            Statement myStmt = dbConn.createStatement();
            String sql = "INSERT INTO newMessages (channelId, message)\nVALUES ( '" + channelId + "', '" + message + "' );";
            myStmt.execute(sql);
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
