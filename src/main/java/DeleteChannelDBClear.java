import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteChannelDBClear extends ListenerAdapter {
    public void onTextChannelDelete(TextChannelDeleteEvent event) {

        if (Main.mapMessage.containsKey(event.getChannel().getId())) {
            Main.mapMessage.remove(event.getChannel().getId());
            try {
                Connection dbConn = DriverManager.getConnection(Main.dbUrl,Main.dbUser,Main.dbPassword);
                Statement myStmt = dbConn.createStatement();
                String sql = "DELETE FROM messages WHERE channelId='" + event.getChannel().getId() + "';";
                myStmt.execute(sql);
                myStmt.close();

            } catch ( SQLException e) {
                e.printStackTrace();
            }
        
        } else if (Main.mapMessageEmbed.containsKey(event.getChannel().getId())) {
            Main.mapMessageEmbed.remove(event.getChannel().getId());
            try {
                Connection dbConn = DriverManager.getConnection(Main.dbUrl,Main.dbUser,Main.dbPassword);
                Statement myStmt = dbConn.createStatement();
                String sql = "DELETE FROM messagesEmbed WHERE channelId='" + event.getChannel().getId() + "';";
                myStmt.execute(sql);
                myStmt.close();

            } catch ( SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
