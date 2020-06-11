import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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
            em.setTitle("You Unlocked StickyBot Premium!");
            em.setDescription("You or someone in your server has purchased StickyBot Premium for your server **" + serverName +
                    "**. To see what new features this has unlocked visit [www.stickybot.info](https://www.stickybot.info).");
            
            Main.jda.getGuildById(args[1]).retrieveOwner().queue((u) -> {
                u.getUser().openPrivateChannel().queue((c) -> {
                    c.sendMessage(em.build()).queue();
                });
            });
        }

        else if(event.getChannel().getId().equals(cancelId)) {
            String guildId = Main.premiumGuilds.get(args[4]);

            //remove all stickies in server
            removeStickies(guildId);
            removePrefixDB(guildId);
            Main.mapPrefix.remove(guildId);
            Main.premiumGuilds.remove(args[4]);
            System.out.println("CANCEL SUB ID: " + args[4]);
            removeDB(args[4]);
        }
    }

    public void addDB(String userPaymentId, String premiumGuildId) {
        try {
            Connection dbConn = DriverManager.getConnection(Main.dbUrl,Main.dbUser,Main.dbPassword);
            Statement myStmt = dbConn.createStatement();
            String sql = "INSERT INTO premium (userPaymentId, premiumGuildId)\nVALUES ( '" + userPaymentId + "', '" + premiumGuildId + "' );";
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
}



