import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteChannelDBClear extends ListenerAdapter {
    public void onTextChannelDelete(TextChannelDeleteEvent event) {
        removeDB(event.getChannel().getId());
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
