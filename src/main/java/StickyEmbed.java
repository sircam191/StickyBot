import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;
import java.sql.*;
import java.text.NumberFormat;
import java.time.OffsetDateTime;
import java.util.List;

public class StickyEmbed extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String[] args = event.getMessage().getContentRaw().split("\\s+");
        //Member stickyBot = event.getGuild().getMemberById(Main.botId);
        Guild stickyServer = Main.jda.getGuildById("641158383138897941");
        String channelId = event.getChannel().getId();

        String prefix = "?";
        if(Main.mapPrefix.containsKey(event.getGuild().getId())) {
            prefix = Main.mapPrefix.get(event.getGuild().getId());
        }

        if (event.getMessage().getContentRaw().startsWith(prefix + "stickembed") && event.getMessage().getContentRaw().matches("[\\S]+\\s{2,}.*") && (permCheck(event.getMember())) && !event.getAuthor().isBot() && Main.premiumGuilds.containsValue(event.getGuild().getId())) {
            event.getMessage().reply(event.getMember().getAsMention() + " please only use one space after the `?stickembed` command!").queue();
            return;
        }

        //Set image for embed command
        if (args[0].equalsIgnoreCase(prefix + "setimage") && (permCheck(event.getMember())) && !event.getAuthor().isBot()) {
            if (!Main.premiumGuilds.containsValue(event.getGuild().getId())) {
                EmbedBuilder em = new EmbedBuilder();
                em.setTitle("**Whoops! This is a StickyBot Premium Command!** ")
                        .addField("__StickyBot Premium__ allows for sticky embeds plus other awesome features!", "Try it for free: [www.stickybot.com](https://www.stickybot.info)", false);
                event.getMessage().reply(em.setColor(Color.ORANGE).build()).queue();
            } else {

                if (args.length == 2) {
                    if (Main.mapImageLinkEmbed.containsKey(channelId)) {
                        removeDBimage(channelId);
                        Main.mapImageLinkEmbed.remove(channelId);
                    }
                    Main.mapImageLinkEmbed.put(channelId, args[1]);
                    addDBimage(channelId, args[1]);
                    event.getMessage().reply(event.getMember().getAsMention() + " success! Image set for `?stickembed` stickies.").queue();
                } else {
                    event.getMessage().reply(event.getMember().getAsMention() + " please provide a link to a image!\nExample: `?setimage https://imgur.com/123`.").queue();
                }
            }

        } else if (args[0].equalsIgnoreCase(prefix + "setimage") && (!permCheck(event.getMember()))) {
        //Adds X emote
        event.getMessage().addReaction("\u274C").queue();
            event.getMessage().reply(event.getMember().getAsMention() + " you need the `Manage Messages` permission to use this command!").queue();
    }

        if (args[0].equalsIgnoreCase(prefix + "removeimage") && (permCheck(event.getMember())) && !event.getAuthor().isBot()) {

            if (!Main.premiumGuilds.containsValue(event.getGuild().getId())) {
                EmbedBuilder em = new EmbedBuilder();
                em.setTitle("**Whoops! This is a StickyBot Premium Command!** ")
                        .addField("__StickyBot Premium__ allows for sticky embeds plus other awesome features!", "Try it for free: [www.stickybot.com](https://www.stickybot.info)", false);
                event.getMessage().reply(em.setColor(Color.ORANGE).build()).queue();
            } else {
                removeDBimage(channelId);
                Main.mapImageLinkEmbed.remove(channelId);
                event.getMessage().reply(event.getMember().getAsMention() + " success! Image removed for sticky embeds in this channel.").queue();
            }

        } else if (args[0].equalsIgnoreCase(prefix + "removeimage") && (!permCheck(event.getMember()))) {
        //Adds X emote
        event.getMessage().addReaction("\u274C").queue();
            event.getMessage().reply(event.getMember().getAsMention() + " you need the `Manage Messages` permission to use this command!").queue();
        }


        if (args[0].equalsIgnoreCase(prefix + "getimage") && (permCheck(event.getMember())) && !event.getAuthor().isBot()) {

            if (Main.mapImageLinkEmbed.containsKey(channelId)) {
                EmbedBuilder em = new EmbedBuilder();
                em.setTitle("Current image for sticky embeds in this channel:");
                em.setThumbnail(Main.mapImageLinkEmbed.get(channelId));
                em.setDescription("Link: " + Main.mapImageLinkEmbed.get(channelId));
                event.getMessage().reply(em.setColor(Color.ORANGE).build()).setActionRow(Button.link(Main.mapImageLinkEmbed.get(channelId), "Image")).queue();

            } else {
                event.getMessage().reply(event.getMember().getAsMention() + " there is no image currently set for sticky embeds in this channel.\nSet one with the `" + prefix + "setimage` command.").queue();
            }

        }

        //Set BIG image for embed command
        if (args[0].equalsIgnoreCase(prefix + "setbigimage") && (permCheck(event.getMember())) && !event.getAuthor().isBot()) {
            if (!Main.premiumGuilds.containsValue(event.getGuild().getId())) {
                EmbedBuilder em = new EmbedBuilder();
                em.setTitle("**Whoops! This is a StickyBot Premium Command!** ")
                        .addField("__StickyBot Premium__ allows for sticky embeds plus other awesome features!", "Try it for free: [www.stickybot.com](https://www.stickybot.info)", false);
                event.getMessage().replyEmbeds(em.setColor(Color.ORANGE).build()).queue();
            } else {

                if (args.length == 2) {
                    if (Main.mapBigImageLinkEmbed.containsKey(channelId)) {
                        removeDBBigImage(channelId);
                        Main.mapBigImageLinkEmbed.remove(channelId);
                    }
                    Main.mapBigImageLinkEmbed.put(channelId, args[1]);
                    addDBBigImage(channelId, args[1]);
                    event.getMessage().reply(event.getMember().getAsMention() + " success! Big image set for `?stickembed` stickies.").queue();
                } else {
                    event.getMessage().reply(event.getMember().getAsMention() + " please provide a link to a image!\nExample: `?setbigimage https://imgur.com/123`.").queue();
                }
            }

        } else if (args[0].equalsIgnoreCase(prefix + "setbigimage") && (!permCheck(event.getMember()))) {
            //Adds X emote
            event.getMessage().addReaction("\u274C").queue();
            event.getMessage().reply(event.getMember().getAsMention() + " you need the `Manage Messages` permission to use this command!").queue();
        }

        if (args[0].equalsIgnoreCase(prefix + "removebigimage") && (permCheck(event.getMember())) && !event.getAuthor().isBot()) {

            if (!Main.premiumGuilds.containsValue(event.getGuild().getId())) {
                EmbedBuilder em = new EmbedBuilder();
                em.setTitle("**Whoops! This is a StickyBot Premium Command!** ")
                        .addField("__StickyBot Premium__ allows for sticky embeds plus other awesome features!", "Try it for free: [www.stickybot.com](https://www.stickybot.info)", false);
                event.getMessage().replyEmbeds(em.setColor(Color.ORANGE).build()).queue();
            } else {
                removeDBBigImage(channelId);
                Main.mapBigImageLinkEmbed.remove(channelId);
                event.getMessage().reply(event.getMember().getAsMention() + " success! Big image removed for sticky embeds in this channel.").queue();
            }

        } else if (args[0].equalsIgnoreCase(prefix + "removebigimage") && (!permCheck(event.getMember()))) {
            //Adds X emote
            event.getMessage().addReaction("\u274C").queue();
            event.getMessage().reply(event.getMember().getAsMention() + " you need the `Manage Messages` permission to use this command!").queue();
        }

        if (args[0].equalsIgnoreCase(prefix + "getbigimage") && (permCheck(event.getMember())) && !event.getAuthor().isBot()) {

            if (Main.mapImageLinkEmbed.containsKey(channelId)) {
                EmbedBuilder em = new EmbedBuilder();
                em.setTitle("Current big image for sticky embeds in this channel:");
                em.setThumbnail(Main.mapBigImageLinkEmbed.get(channelId));
                em.setDescription("Link: " + Main.mapBigImageLinkEmbed.get(channelId));
                event.getMessage().replyEmbeds(em.setColor(Color.ORANGE).build()).setActionRow(Button.link(Main.mapImageLinkEmbed.get(channelId), "Image")).queue();

            } else {
                event.getMessage().reply(event.getMember().getAsMention() + " there is no image currently set for sticky embeds in this channel.\nSet one with the `" + prefix + "setimage` command.").queue();
            }
        }


        if (args[0].equalsIgnoreCase(prefix + "stickembed") && (permCheck(event.getMember())) && !event.getAuthor().isBot()) {


            if (!Main.premiumGuilds.containsValue(event.getGuild().getId())) {
                EmbedBuilder em = new EmbedBuilder();
                em.setTitle("**Whoops! This is a StickyBot Premium Command!** ")
                        .addField("__StickyBot Premium__ allows for sticky embeds plus other awesome features!", "Try it for free: [www.stickybot.com](https://www.stickybot.info)", false);
                event.getMessage().replyEmbeds(em.setColor(Color.ORANGE).build()).queue();
            }
            else  {
                try {

                    for (Emote emote : event.getMessage().getEmotes()) {
                        event.getGuild().retrieveEmoteById(emote.getId()).queue(success -> {}, failure -> {
                            event.getMessage().reply(event.getMember().getAsMention() + " Error: Please only use emotes that are from this server.").queue();
                            Main.mapMessageEmbed.remove(event.getChannel().getId());
                            removeDB(channelId);
                        });
                    }

                    if (event.getMessage().getContentRaw().contains(prefix + "stickembed \n")) {
                        event.getMessage().reply(event.getMember().getAsMention() + " Error: Please provide text after `" + prefix + "stickembed` before using a new line.").queue();
                        return;
                    }

                    String o = event.getMessage().getContentRaw();
                    String [] arr = o.split(" ", 2);

                    String message = arr[1];

                    if(Character.isWhitespace(message.charAt(0))) {
                        message.replaceFirst("\\s+", "");
                    }


                    Main.mapMessageEmbed.put(event.getChannel().getId(), message);
                    removeDB(channelId);
                    addDB(channelId,(message));

                    EmbedBuilder emb = new EmbedBuilder();
                    emb.setDescription(message);

                    if (Main.mapImageLinkEmbed.containsKey(channelId)) {
                        emb.setThumbnail(Main.mapImageLinkEmbed.get(channelId));
                    }
                    if (Main.mapBigImageLinkEmbed.containsKey(channelId)) {
                        emb.setImage(Main.mapBigImageLinkEmbed.get(channelId));
                    }

                    emb.setColor(event.getGuild().getMemberById(Main.botId).getColor());
                    event.getChannel().sendMessage(emb.build()).queue(m -> Main.mapDeleteIdEmbed.put(event.getChannel().getId(), m.getId()));
                    event.getMessage().addReaction("\u2705").queue();
                } catch (Exception e) {
                    event.getMessage().reply(event.getMember().getAsMention() + " please use this format: `" + prefix + "stickembed <message>`\n*Only include emotes that are from this server.*").queue();
                }
            }

        } else if (args[0].equalsIgnoreCase(prefix + "stickembed") && (!permCheck(event.getMember()))) {
            //Adds X emote
            event.getMessage().addReaction("\u274C").queue();
            //event.getChannel().sendMessage("You need the `Manage Messages` permission to use this command!").queue();
        }

        else if ( (args[0].equalsIgnoreCase(prefix + "stickstop") || args[0].equalsIgnoreCase(prefix + "unstick")) && (permCheck(event.getMember()))) {
            Main.mapMessageEmbed.remove(channelId);

            if(Main.mapDeleteIdEmbed.get(channelId) != null) {
                event.getChannel().deleteMessageById(Main.mapDeleteIdEmbed.get(channelId)).queue(null, (error) -> {});
            }

            removeDB(channelId);
            event.getMessage().addReaction("\u2705").queue();
        } else if ( (args[0].equalsIgnoreCase(Main.prefix + "stickstop") || args[0].equalsIgnoreCase(Main.prefix + "unstick")) && (!permCheck(event.getMember() ))) {
            //Adds X mark
            event.getMessage().addReaction("\u274C").queue();
            //event.getChannel().sendMessage("You need the global `Manage Messages` permission to use this command!").queue();
        }

        if(Main.mapMessageEmbed.get(channelId) != null) {
            event.getChannel().getHistory().retrievePast(5).queue(history -> {

                for(Message m : history) {
                    //if message is sticky message
                    if(!m.getEmbeds().isEmpty() && embedCheck(m, channelId)) {
                        //if message is older then 30 sec
                        if(m.getTimeCreated().compareTo(OffsetDateTime.now().minusSeconds(15)) < 0) {
                            m.delete().queue(null, (error) -> {});

                            EmbedBuilder emb = new EmbedBuilder();
                            emb.setDescription(Main.mapMessageEmbed.get(channelId));
                            emb.setColor(event.getGuild().getMemberById(Main.botId).getColor());
                            if (Main.mapImageLinkEmbed.containsKey(channelId)) {
                                emb.setThumbnail(Main.mapImageLinkEmbed.get(channelId));
                            }
                            if (Main.mapBigImageLinkEmbed.containsKey(channelId)) {
                                emb.setImage(Main.mapBigImageLinkEmbed.get(channelId));
                            }
                            event.getChannel().sendMessage(emb.build()).queue(mes -> Main.mapDeleteIdEmbed.put(channelId, mes.getId()));

                            //Added to make sure it does not bug and send two stickies (next 5 lines)
                            event.getChannel().getHistory().retrievePast(10).queue(messageListDelete -> {

                            for (Message mess : messageListDelete.subList(1, 10)) {
                                if (!mess.getEmbeds().isEmpty() && embedCheck(mess, channelId)) {
                                    mess.delete().queue(null, (error) -> {});
                                }
                            }
                            });
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
                    if(Main.mapDeleteIdEmbed.get(channelId) != null) {
                        event.getChannel().deleteMessageById(Main.mapDeleteIdEmbed.get(channelId)).queue(null, (error) -> {});
                    }

                    event.getChannel().getHistory().retrievePast(6).queue(history2 -> {
                        for(Message m : history2) {
                            //if message is sticky message
                            if(!m.getEmbeds().isEmpty() && m.getEmbeds().get(0).getDescription().equals(Main.mapMessageEmbed.get(channelId))) {
                                m.delete().queue(null, (error) -> {});
                            }
                        }
                    });

                    EmbedBuilder emb = new EmbedBuilder();
                    emb.setDescription(Main.mapMessageEmbed.get(channelId));
                    if (Main.mapImageLinkEmbed.containsKey(channelId)) {
                        emb.setThumbnail(Main.mapImageLinkEmbed.get(channelId));
                    }
                    if (Main.mapBigImageLinkEmbed.containsKey(channelId)) {
                        emb.setImage(Main.mapBigImageLinkEmbed.get(channelId));
                    }
                    emb.setColor(event.getGuild().getMemberById(Main.botId).getColor());

                    try {
                        event.getChannel().sendMessage(emb.build()).queue(null, (error) -> {

                            Main.mapMessageEmbed.remove(channelId);
                            Main.mapDeleteId.remove(channelId);
                            removeDB(channelId);
                            System.out.println("Tried to send sticky message in channel with no MESSAGE_WRITE perms. Sticky message has been stopped:");
                            System.out.println("Channel ID: " + channelId + "\nServer ID: " + event.getGuild().getId());

                        });
                    } catch (Exception e) {
                        Main.mapMessageEmbed.remove(channelId);
                        Main.mapDeleteId.remove(channelId);
                        removeDB(channelId);
                        System.out.println("Tried to send sticky message in channel with no MESSAGE_WRITE perms. Sticky message has been stopped:");
                        System.out.println("Channel ID: " + channelId + "\nServer ID: " + event.getGuild().getId());
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

    public boolean embedCheck(Message mess, String channelId) {
        //returns true if embed description is sticky message

        try {
            if (mess.getEmbeds().get(0).getDescription().contains(Main.mapMessageEmbed.get(channelId)) ) {
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

    public void addDBBigImage(String channelId, String message) {
        try {
            Connection dbConn = DriverManager.getConnection(Main.dbUrl,Main.dbUser,Main.dbPassword);
            String sql = "INSERT INTO bigEmbedImageLink (channelId, link) VALUES ( ?, ?)";
            PreparedStatement myStmt = dbConn.prepareStatement(sql);
            myStmt.setString(1, channelId);
            myStmt.setString(2, message);
            myStmt.execute();
            myStmt.close();

        } catch ( SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeDBBigImage(String channelId) {
        try {
            Connection dbConn = DriverManager.getConnection(Main.dbUrl,Main.dbUser,Main.dbPassword);
            Statement myStmt = dbConn.createStatement();
            String sql = "DELETE FROM bigEmbedImageLink WHERE channelId='" + channelId + "';";
            myStmt.execute(sql);
            myStmt.close();
        } catch ( SQLException e) {
            e.printStackTrace();
        }
    }

}
