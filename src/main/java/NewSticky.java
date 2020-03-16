import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.*;
import java.util.List;

public class Sticky extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Member stickyBot = event.getGuild().getMemberById(Main.botId);
        Guild stickyServer = event.getJDA().getGuildById("641158383138897941");

        if (args[0].equalsIgnoreCase(Main.prefix + "stick") && (permCheck(event.getMember() ))) {
            Main.mapMessage.put(event.getChannel().getId(), "__**Stickied Message:**__\n\n" + event.getMessage().getContentRaw().substring(7));
            addDB(event.getChannel().getId(),event.getMessage().getContentRaw().substring(7));
            event.getMessage().addReaction("\u2705").queue();

        } else if (args[0].equalsIgnoreCase(Main.prefix + "stick") && (!permCheck(event.getMember() ))) {
            event.getMessage().addReaction("\u274C").queue();
        }

        if ( (args[0].equalsIgnoreCase(Main.prefix + "stickstop") || args[0].equalsIgnoreCase(Main.prefix + "unstick")) && (permCheck(event.getMember() ))) {
            event.getChannel().deleteMessageById(Main.mapDeleteId.get(event.getChannel().getId())).queue();
            Main.mapMessage.remove(event.getChannel().getId());
            Main.mapDeleteId.remove(event.getChannel().getId());
            removeDB(event.getChannel().getId());
            event.getMessage().addReaction("\u2705").queue();
        } else if ( (args[0].equalsIgnoreCase(Main.prefix + "stickstop") || args[0].equalsIgnoreCase(Main.prefix + "unstick")) && (!permCheck(event.getMember() ))) {
            event.getMessage().addReaction("\u274C").queue();
        }

        if (Main.mapMessage.get(event.getChannel().getId()) != null && !event.getAuthor().getId().equals(Main.botId)) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            event.getChannel().sendMessage(Main.mapMessage.get(event.getChannel().getId())).queue(m -> Main.mapDeleteId.put(event.getChannel().getId(), m.getId()));
        }


        if (Main.mapDeleteId.get(event.getChannel().getId()) != null && (event.getChannel().getLatestMessageId() != Main.mapDeleteId.get(event.getChannel().getId()))) {

            List<Message> messageCheck = event.getChannel().getHistory().retrievePast(1).complete();

           if (!messageCheck.get(0).getContentRaw().contains(Main.mapMessage.get(event.getChannel().getId()))) {
               event.getChannel().deleteMessageById(Main.mapDeleteId.get(event.getChannel().getId())).queue();
           } else {
               List<Message> messageListDelete = event.getChannel().getHistory().retrievePast(10).complete();
               for (Message m : messageListDelete.subList(1, 10)) {
                   if (m.getContentRaw().contains(Main.mapMessage.get(event.getChannel().getId()))) {
                       try {
                           m.delete().queue();
                       } catch (Exception e) {
                       }

                   }
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
            Statement myStmt = dbConn.createStatement();
            String sql = "INSERT INTO messages (channelId, message)\nVALUES ( '" + channelId + "', '" + message + "' );";

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
            String sql = "DELETE FROM messages WHERE channelId='" + channelId + "';";

            myStmt.execute(sql);
            myStmt.close();

        } catch ( SQLException e) {
            e.printStackTrace();
        }
    }
}
