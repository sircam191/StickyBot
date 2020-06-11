import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PrefixCommand extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if(event.getAuthor().isBot()) {
            return;
        }

            if (args[0].equalsIgnoreCase(Main.prefix + "prefix")) {

                if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                    event.getChannel().sendMessage("You need the `Manage Server` permission to use this command.").queue();
                } else if (!Main.premiumGuilds.containsValue(event.getGuild().getId())) {
                    event.getChannel().sendMessage("This command requires __StickyBot Premium__.\nLearn more at https://www.stickybot.info").queue();
                } else {
                    try {
                        if (args[1].isEmpty()) {
                            event.getChannel().sendMessage("Please provide the new prefix in the command. Example: `?prefix !`").queue();
                        } else {
                            Main.mapPrefix.put(event.getGuild().getId(), args[1]);
                            event.getChannel().sendMessage("Prefix set to `" + args[1] + "`").queue();
                            removeDB(event.getGuild().getId());
                            addDB(event.getGuild().getId(), args[1]);
                        }
                    } catch (Exception e) {
                        event.getChannel().sendMessage("Please provide the new prefix in the command.").queue();
                    }
                }
            }

        if ( args[0].equalsIgnoreCase(Main.prefix + "resetprefix") && (event.getMember().hasPermission(Permission.MANAGE_SERVER)) ) {
            Main.mapPrefix.remove(event.getGuild().getId());
            event.getChannel().sendMessage("Prefix reset to `?`").queue();
            removeDB(event.getGuild().getId());
        }
    }

    public void addDB(String serverId, String prefix) {
        try {
            Connection dbConn = DriverManager.getConnection(Main.dbUrl,Main.dbUser,Main.dbPassword);
            Statement myStmt = dbConn.createStatement();
            String sql = "INSERT INTO prefixs (serverId, prefix)\nVALUES ( '" + serverId + "', '" + prefix + "' );";
            myStmt.execute(sql);
            myStmt.close();
        } catch ( SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeDB(String serverId) {
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


}
