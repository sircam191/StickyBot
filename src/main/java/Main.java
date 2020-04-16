
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.discordbots.api.client.DiscordBotListAPI;

import javax.security.auth.login.LoginException;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static net.dv8tion.jda.api.entities.Activity.playing;

public class Main {
    public static JDA jda;
    public static String prefix = "?";

    public static String botId = "***************";
    public static String token = "**************************************";

    public static String dbUrl = "jdbc:mysql://localhost:****/STICKYBOT4?useUnicode=true&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    public static String dbUser = "******";
    public static String dbPassword = "*******";

    public static String topggAPIToken = "**************************************************************************************************************************";

    public static Map<String, String> mapMessage = new HashMap<>();
    public static Map<String, String> mapDeleteId = new HashMap<>();


    public static void main (String[] args) throws LoginException{

        try {
            Connection dbConn = DriverManager.getConnection(dbUrl,dbUser,dbPassword);
            Statement myStmt = dbConn.createStatement();
            String sql = "select * from messages";
            ResultSet rs = myStmt.executeQuery(sql);

            while (rs.next()) {
                mapMessage.put(rs.getString("channelId"),"__**Stickied Message:**__\n\n" + rs.getString("message"));
                mapDeleteId.put(rs.getString("channelId"),"");
                //System.out.println(rs.getString("channelId") + " -> " + rs.getString("message"));
            }

        } catch ( SQLException e) {
            e.printStackTrace();
        }

        try {
            jda = new JDABuilder(token).build().awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        jda.getPresence().setActivity(playing("?help"));

        jda.addEventListener(new Commands());
        jda.addEventListener(new JoinNewGuild());
        jda.addEventListener(new Sticky());
        jda.addEventListener(new VirusCommand());
        jda.addEventListener(new DeleteChannelDBClear());


        DiscordBotListAPI api = new DiscordBotListAPI.Builder()
                .token(topggAPIToken)
               .botId(botId)
               .build();

        int serverCount = jda.getGuilds().size();
        api.setStats(serverCount);

    }

}

