import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.*;

public class PrefixCommand extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        String prefix = "?";

        if(event.getAuthor().isBot()) {
            return;
        }

        if(Main.mapPrefix.containsKey(event.getGuild().getId())) {
            prefix = Main.mapPrefix.get(event.getGuild().getId());
        }

        if (args[0].equalsIgnoreCase(Main.prefix + "prefix") || args[0].equalsIgnoreCase(prefix + "prefix")) {

             if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                 event.getMessage().reply(event.getMember().getAsMention() + " you need the `Manage Server` permission to use this command.").queue();
             } else if (!Main.premiumGuilds.containsValue(event.getGuild().getId())) {
                 event.getMessage().reply("This command requires __StickyBot Premium__.\nLearn more at https://www.stickybot.info").queue();
             } else {
                 try {
                     if (args[1].isEmpty()) {
                         event.getMessage().reply(event.getMember().getAsMention() + " please provide the new prefix in the command. Example: `?prefix !`").queue();
                      } else {
                         Main.mapPrefix.put(event.getGuild().getId(), args[1]);
                         event.getChannel().sendMessage("Prefix set to `" + args[1] + "`").queue();
                         removeDB(event.getGuild().getId());
                         addDB(event.getGuild().getId(), args[1]);
                      }
                 } catch (Exception e) {
                     event.getMessage().reply(event.getMember().getAsMention() + " please provide the new prefix in the command.").queue();
                    }
                }
            }

        if ( args[0].equalsIgnoreCase(Main.prefix + "resetprefix") || args[0].equalsIgnoreCase("?resetprefix") && (event.getMember().hasPermission(Permission.MANAGE_SERVER)) ) {
            Main.mapPrefix.remove(event.getGuild().getId());
            event.getMessage().reply("Prefix reset to `?`").queue();
            removeDB(event.getGuild().getId());
        }
    }

    public void addDB(String serverId, String prefix) {
        try {
            Connection dbConn = DriverManager.getConnection(Main.dbUrl,Main.dbUser,Main.dbPassword);
            String sql = "INSERT INTO prefixs (serverId, prefix) VALUES ( ?, ?)";
            PreparedStatement myStmt = dbConn.prepareStatement(sql);
            myStmt.setString(1, serverId);
            myStmt.setString(2, prefix);
            myStmt.execute();
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
