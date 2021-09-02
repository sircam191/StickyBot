import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public class LoadPremiumUpdates extends ListenerAdapter {

    String addId = "719689829440421890";
    String cancelId= "719698880018776075";

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        //SUB Purchase
        if(event.getChannel().getId().equals(addId)) {
            Main.premiumGuilds.put(args[14], args[1]);
            System.out.println("GUILD ID: " + args[1] + ". SUB ID: " + args[14]);
            addDB(args[14], args[1]);

            //Sends server owner DM
            EmbedBuilder em = new EmbedBuilder();
            String serverName;
            try {
                serverName = Main.jda.getGuildById(args[1]).getName();
            } catch (Exception e) {
                serverName = "Null";
            }

            em.setColor(Color.ORANGE);
            em.setTitle("\uD83C\uDF89 You Unlocked StickyBot Premium! \uD83C\uDF89");
            em.setDescription("You or someone in your server has purchased StickyBot Premium for your server **" + serverName +
                    "**. To see what new features this has unlocked visit [www.stickybot.info](https://www.stickybot.info).");

            Main.jda.getGuildById(args[1]).retrieveOwner().queue((u) -> {
                u.getUser().openPrivateChannel().queue((c) -> {
                    c.sendMessageEmbeds(em.build()).setActionRows(
                            ActionRow.of(net.dv8tion.jda.api.interactions.components.Button.link("https://www.stickybot.info", "Website").withEmoji(Emoji.fromMarkdown("<:StickyBotCircle:693004145065590856>")),
                                    net.dv8tion.jda.api.interactions.components.Button.link("https://discord.com/invite/SvNQTtf", "Support Server").withEmoji(Emoji.fromMarkdown("<:discordEmote:853160010305765376>"))),
                            ActionRow.of(net.dv8tion.jda.api.interactions.components.Button.link("https://www.stickybot.info/manage-subscription", "Manage Premium").withEmoji(Emoji.fromMarkdown("\uD83E\uDDE1")),
                                    Button.link("https://docs.stickybot.info", "Docs").withEmoji(Emoji.fromMarkdown("<:iBlue:860060995979706389>"))))
                            .queue();
                });
            });


        }
        //Sub Cancel
        else if(event.getChannel().getId().equals(cancelId)) {
            String guildId = Main.premiumGuilds.get(args[4]);

            Main.premiumGuilds.remove(args[4]);

            System.out.println("CANCEL SUB ID: " + args[4]);
            removeDB(args[4]);

            //remove all stickies in server & prefix
            if (!Main.premiumGuilds.containsValue(guildId)) {
                Main.mapPrefix.remove(guildId);
                removeStickies(guildId);
                removePrefixDB(guildId);
                removeEmbeds(guildId);
                removeStickySlow(guildId);
                removeStickyWebHook(guildId);
            }

        }
    }

    public void addDB(String userPaymentId, String premiumGuildId) {
        try {
            Connection dbConn = DriverManager.getConnection(Main.dbUrl,Main.dbUser,Main.dbPassword);
            Statement myStmt = dbConn.createStatement();
            String sql = "INSERT INTO premium (userPaymentId, premiumGuildId)\nVALUES ( '" + userPaymentId + "', '" + premiumGuildId.replaceAll("'", "") + "' );";
            myStmt.execute(sql);
            myStmt.close();
        } catch ( SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeDB(String userPaymentId) {
        try {
            Connection dbConn = DriverManager.getConnection(Main.dbUrl,Main.dbUser,Main.dbPassword);
            Statement myStmt = dbConn.createStatement();
            String sql = "DELETE FROM premium WHERE userPaymentId='" + userPaymentId + "';";
            myStmt.execute(sql);
            myStmt.close();
        } catch ( SQLException e) {
            e.printStackTrace();
        }
    }

    public void removePrefixDB(String serverId) {
        try {
            Connection dbConn = DriverManager.getConnection(Main.dbUrl,Main.dbUser,Main.dbPassword);
            Statement myStmt = dbConn.createStatement();
            String sql = "DELETE FROM prefixs WHERE serverId='" + serverId + "';";
            myStmt.execute(sql);
            myStmt.close();
        } catch ( SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeStickies(String guildId) {

        List<String> channelIds = Main.jda.getGuildById(guildId).getTextChannels().stream().map(textChannel -> textChannel.getId()).collect(Collectors.toList());

        for (String id : channelIds) {
            if (Main.mapMessage.containsKey(id)) {
                Main.mapMessage.remove(id);

                try {
                    Connection dbConn = DriverManager.getConnection(Main.dbUrl,Main.dbUser,Main.dbPassword);
                    Statement myStmt = dbConn.createStatement();
                    String sql = "DELETE FROM newMessages WHERE channelId='" + id + "';";
                    myStmt.execute(sql);
                    myStmt.close();
                } catch ( SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void removeEmbeds(String guildId) {
        List<String> channelIds = Main.jda.getGuildById(guildId).getTextChannels().stream().map(textChannel -> textChannel.getId()).collect(Collectors.toList());

        for (String id : channelIds) {
            if (Main.mapMessageEmbed.containsKey(id)) {
                Main.mapMessageEmbed.remove(id);

                try {
                    Connection dbConn = DriverManager.getConnection(Main.dbUrl,Main.dbUser,Main.dbPassword);
                    Statement myStmt = dbConn.createStatement();
                    String sql = "DELETE FROM messagesEmbed WHERE channelId='" + id + "';";
                    myStmt.execute(sql);
                    myStmt.close();
                } catch ( SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void removeStickySlow(String guildId) {
        List<String> channelIds = Main.jda.getGuildById(guildId).getTextChannels().stream().map(textChannel -> textChannel.getId()).collect(Collectors.toList());

        for (String id : channelIds) {
            if (Main.mapMessageSlow.containsKey(id)) {
                Main.mapMessageSlow.remove(id);

                try {
                    Connection dbConn = DriverManager.getConnection(Main.dbUrl,Main.dbUser,Main.dbPassword);
                    Statement myStmt = dbConn.createStatement();
                    String sql = "DELETE FROM slowMessages WHERE channelId='" + id + "';";
                    myStmt.execute(sql);
                    myStmt.close();
                } catch ( SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void removeStickyWebHook(String guildId) {
        List<String> channelIds = Main.jda.getGuildById(guildId).getTextChannels().stream().map(textChannel -> textChannel.getId()).collect(Collectors.toList());

        for (String id : channelIds) {
            if (Main.webhookMessage.containsKey(id)) {
                Main.webhookMessage.remove(id);

                try {
                    Connection dbConn = DriverManager.getConnection(Main.dbUrl,Main.dbUser,Main.dbPassword);
                    Statement myStmt = dbConn.createStatement();
                    String sql = "DELETE FROM webhookMessage WHERE channelId='" + id + "';";
                    myStmt.execute(sql);
                    myStmt.close();
                } catch ( SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}



