import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.*;
import java.text.NumberFormat;
import java.time.OffsetDateTime;
import java.util.List;

public class StickEmbedSlow extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String[] args = event.getMessage().getContentRaw().split("\\s+");
        //Member stickyBot = event.getGuild().getMemberById(Main.botId);
        Guild stickyServer = Main.jda.getGuildById("641158383138897941");
        String channelId = event.getChannel().getId();

        String prefix = "?";
        if(Main.mapPrefix.containsKey(event.getGuild().getId())) {
            prefix = Main.mapPrefix.get(event.getGuild().getId());
        }

        if (event.getMessage().getContentRaw().startsWith(prefix + "stickembedslow") && event.getMessage().getContentRaw().matches("[\\S]+\\s{2,}.*") && (permCheck(event.getMember())) && !event.getAuthor().isBot() && Main.premiumGuilds.containsValue(event.getGuild().getId())) {
            //Removed since it sends error to user in normal stick embed class.
            //event.getChannel().sendMessage(event.getMember().getAsMention() + " please only use one space after the `?stickembedslow` command!").queue();
            return;
        }


        if (args[0].equalsIgnoreCase(prefix + "stickembedslow") && (permCheck(event.getMember())) && !event.getAuthor().isBot()) {


            if (!Main.premiumGuilds.containsValue(event.getGuild().getId())) {
                EmbedBuilder em = new EmbedBuilder();
                em.setTitle("**Whoops! This is a StickyBot Premium Command!** ")
                        .addField("__StickyBot Premium__ allows for sticky embeds plus other awesome features!", "Learn more: [www.stickybot.com](https://www.stickybot.info)", false);
                event.getChannel().sendMessage(em.setColor(Color.ORANGE).build()).queue();
            }
            else  {
                try {

                    for (Emote emote : event.getMessage().getEmotes()) {
                        event.getGuild().retrieveEmoteById(emote.getId()).queue(success -> {}, failure -> {
                            event.getChannel().sendMessage(event.getMember().getAsMention() + " Error: Please only use emotes that are from this server.").queue();
                            Main.mapMessageEmbed2.remove(event.getChannel().getId());
                            removeDB(channelId);
                        });
                    }

                    if (event.getMessage().getContentRaw().contains(prefix + "stickembedslow \n")) {
                        event.getChannel().sendMessage(event.getMember().getAsMention() + " Error: Please provide text after `" + prefix + "stickembedslow` before using a new line.").queue();
                        return;
                    }

                    String o = event.getMessage().getContentRaw();
                    String [] arr = o.split(" ", 2);

                    String message = arr[1];

                    if(Character.isWhitespace(message.charAt(0))) {
                        message.replaceFirst("\\s+", "");
                    }


                    Main.mapMessageEmbed2.put(event.getChannel().getId(), message);
                    removeDB(channelId);
                    addDB(channelId,(message));

                    EmbedBuilder emb = new EmbedBuilder();
                    emb.setDescription(message);

                    if (Main.mapImageLinkEmbed.containsKey(channelId)) {
                        emb.setThumbnail(Main.mapImageLinkEmbed.get(channelId));
                    }

                    emb.setColor(event.getGuild().getMemberById(Main.botId).getColor());
                    event.getChannel().sendMessage(emb.build()).queue(m -> Main.mapDeleteIdEmbed2.put(event.getChannel().getId(), m.getId()));
                    event.getMessage().addReaction("\u2705").queue();
                } catch (Exception e) {
                    event.getChannel().sendMessage(event.getMember().getAsMention() + " please use this format: `?stickembedslow <message>`\n*Only include emotes that are from this server.*").queue();
                }
            }

        } else if (args[0].equalsIgnoreCase(prefix + "stickembedslow") && (!permCheck(event.getMember()))) {
            //Adds X emote
            event.getMessage().addReaction("\u274C").queue();
            //event.getChannel().sendMessage("You need the `Manage Messages` permission to use this command!").queue();
        }

        else if ( (args[0].equalsIgnoreCase(prefix + "stickstop") || args[0].equalsIgnoreCase(prefix + "unstick")) && (permCheck(event.getMember()))) {
            Main.mapMessageEmbed2.remove(channelId);

            if(Main.mapDeleteIdEmbed2.get(channelId) != null) {
                event.getChannel().deleteMessageById(Main.mapDeleteIdEmbed2.get(channelId)).queue(null, (error) -> {});
            }

            removeDB(channelId);
            event.getMessage().addReaction("\u2705").queue();
        } else if ( (args[0].equalsIgnoreCase(Main.prefix + "stickstop") || args[0].equalsIgnoreCase(Main.prefix + "unstick")) && (!permCheck(event.getMember() ))) {
            //Adds X mark
            event.getMessage().addReaction("\u274C").queue();
            //event.getChannel().sendMessage("You need the global `Manage Messages` permission to use this command!").queue();
        }

        if(Main.mapMessageEmbed2.get(channelId) != null) {
            List<Message> history = event.getChannel().getHistory().retrievePast(18).complete();
            for(Message m : history) {
                //if message is sticky message
                if(!m.getEmbeds().isEmpty() && embedCheck(m, channelId)) {
                    //if message is older then 30 sec
                    if(m.getTimeCreated().compareTo(OffsetDateTime.now().minusSeconds(35)) < 0) {
                        m.delete().queue(null, (error) -> {});

                        EmbedBuilder emb = new EmbedBuilder();
                        emb.setDescription(Main.mapMessageEmbed2.get(channelId));
                        emb.setColor(event.getGuild().getMemberById(Main.botId).getColor());
                        if (Main.mapImageLinkEmbed.containsKey(channelId)) {
                            emb.setThumbnail(Main.mapImageLinkEmbed.get(channelId));
                        }
                        event.getChannel().sendMessage(emb.build()).queue(mes -> Main.mapDeleteIdEmbed2.put(channelId, mes.getId()));

                        //Added to make sure it does not bug and send two stickies (next 5 lines)
                        List<Message> messageListDelete = event.getChannel().getHistory().retrievePast(13).complete();
                        for (Message mess : messageListDelete.subList(1, 13)) {
                            if (!mess.getEmbeds().isEmpty() && embedCheck(mess, channelId)) {
                                mess.delete().queue(null, (error) -> {});
                            }
                        }
                    }
                    break;
                }
            }

            //gets set to true if one of last five messages contains sticky message.
            Boolean check = false;

            for(Message m : history) {
                if(!m.getEmbeds().isEmpty() && embedCheck(m, channelId)) {
                    check = true;
                }
            }
            if(!check) {
                if(Main.mapDeleteIdEmbed2.get(channelId) != null) {
                    event.getChannel().deleteMessageById(Main.mapDeleteIdEmbed2.get(channelId)).queue(null, (error) -> {});
                }

                List<Message> history2 = event.getChannel().getHistory().retrievePast(6).complete();
                for(Message m : history2) {
                    //if message is sticky message
                    if(!m.getEmbeds().isEmpty() && m.getEmbeds().get(0).getDescription().equals(Main.mapMessageEmbed2.get(channelId))) {
                        m.delete().queue(null, (error) -> {});
                    }
                }
                EmbedBuilder emb = new EmbedBuilder();
                emb.setDescription(Main.mapMessageEmbed2.get(channelId));
                if (Main.mapImageLinkEmbed.containsKey(channelId)) {
                    emb.setThumbnail(Main.mapImageLinkEmbed.get(channelId));
                }
                emb.setColor(event.getGuild().getMemberById(Main.botId).getColor());

                try {
                    event.getChannel().sendMessage(emb.build()).queue(null, (error) -> {

                        Main.mapMessageEmbed2.remove(channelId);
                        Main.mapDeleteId.remove(channelId);
                        removeDB(channelId);
                        System.out.println("Tried to send sticky message in channel with no MESSAGE_WRITE perms. Sticky message has been stopped:");
                        System.out.println("Channel ID: " + channelId + "\nServer ID: " + event.getGuild().getId());

                    });
                } catch (Exception e) {
                    Main.mapMessageEmbed2.remove(channelId);
                    Main.mapDeleteId.remove(channelId);
                    removeDB(channelId);
                    System.out.println("Tried to send sticky message in channel with no MESSAGE_WRITE perms. Sticky message has been stopped:");
                    System.out.println("Channel ID: " + channelId + "\nServer ID: " + event.getGuild().getId());
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

    public boolean embedCheck(Message mess, String channelId) {
        //returns true if embed description is sticky message

        try {
            if (mess.getEmbeds().get(0).getDescription().contains(Main.mapMessageEmbed2.get(channelId)) ) {
                return true;
            }
            else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }


    public void addDB(String channelId, String message) {
        try {
            Connection dbConn = DriverManager.getConnection(Main.dbUrl,Main.dbUser,Main.dbPassword);
            String sql = "INSERT INTO messagesEmbed (channelId, message) VALUES ( ?, ?)";
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
            String sql = "DELETE FROM messagesEmbed WHERE channelId='" + channelId + "';";
            myStmt.execute(sql);
            myStmt.close();
        } catch ( SQLException e) {
            e.printStackTrace();
        }
    }


    public void addDBimage(String channelId, String message) {
        try {
            Connection dbConn = DriverManager.getConnection(Main.dbUrl,Main.dbUser,Main.dbPassword);
            String sql = "INSERT INTO embedImageLink (channelId, link) VALUES ( ?, ?)";
            PreparedStatement myStmt = dbConn.prepareStatement(sql);
            myStmt.setString(1, channelId);
            myStmt.setString(2, message);
            myStmt.execute();
            myStmt.close();

        } catch ( SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeDBimage(String channelId) {
        try {
            Connection dbConn = DriverManager.getConnection(Main.dbUrl,Main.dbUser,Main.dbPassword);
            Statement myStmt = dbConn.createStatement();
            String sql = "DELETE FROM embedImageLink WHERE channelId='" + channelId + "';";
            myStmt.execute(sql);
            myStmt.close();
        } catch ( SQLException e) {
            e.printStackTrace();
        }
    }

}
