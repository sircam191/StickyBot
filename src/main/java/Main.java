import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManager;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.discordbots.api.client.DiscordBotListAPI;

import javax.security.auth.login.LoginException;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.dv8tion.jda.api.entities.Activity.playing;

public class Main {
    public static ShardManager jda;
    public static String prefix = "?";

    public static String botId = "642587979193516043";
    public static String token = "***";

    public static String dbUrl = "jdbc:mysql://localhost:3306/STICKYBOT4?useUnicode=true&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    public static String dbUser = "***";
    public static String dbPassword = "***";

    public static String topggAPIToken = "***";

    public static Map<String, String> mapMessage = new HashMap<>();
    public static Map<String, String> mapDeleteId = new HashMap<>();
    public static Map<String, String> mapPrefix = new HashMap<>();

    //subID, prem guildID
    public static Map<String, String> premiumGuilds = new HashMap<>();;


    //TODO Final Bot ID: 628400349979344919
    //TODO Beta Bot ID: 642587979193516043

    //TODO Final Build Token: ***
    //TODO Beta Build Token: ***

    public static void main (String[] args) throws LoginException{

        //Get sticky messages from DB
        try {
            Connection dbConn = DriverManager.getConnection(dbUrl,dbUser,dbPassword);
            Statement myStmt = dbConn.createStatement();
            String sql = "select * from newMessages";
            ResultSet rs = myStmt.executeQuery(sql);

            while (rs.next()) {
                mapMessage.put(rs.getString("channelId"), rs.getString("message"));
                //System.out.println(rs.getString("channelId") + " -> " + rs.getString("message"));
            }

        } catch ( SQLException e) {
            e.printStackTrace();
        }

        //Get Premium Guilds from DB
        try {
            Connection dbConn = DriverManager.getConnection(dbUrl,dbUser,dbPassword);
            Statement myStmt = dbConn.createStatement();
            String sql = "select * from premium";
            ResultSet rs = myStmt.executeQuery(sql);

            while (rs.next()) {
                premiumGuilds.put(rs.getString("userPaymentId"), rs.getString("premiumGuildId"));
            }

        } catch ( SQLException e) {
            e.printStackTrace();
        }

        //Get Premium Guilds from DB
        try {
            Connection dbConn = DriverManager.getConnection(dbUrl,dbUser,dbPassword);
            Statement myStmt = dbConn.createStatement();
            String sql = "select * from prefixs";
            ResultSet rs = myStmt.executeQuery(sql);

            while (rs.next()) {
                mapPrefix.put(rs.getString("serverId"), rs.getString("prefix"));
            }

        } catch ( SQLException e) {
            e.printStackTrace();
        }

        jda = DefaultShardManagerBuilder.create(token,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.DIRECT_MESSAGES
                ).setChunkingFilter(ChunkingFilter.NONE).setMemberCachePolicy(MemberCachePolicy.NONE)
                 .addEventListeners(
                         new Commands(),
                         new JoinNewGuild(),
                         new VirusCommand(),
                         new DeleteChannelDBClear(),
                         new StickyTime(),
                         new ShardCommands(),
                         new PrefixCommand(),
                         new ManualPremiumCommands(),
                         new LoadPremiumUpdates()
                 )
                 .disableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, CacheFlag.EMOTE, CacheFlag.CLIENT_STATUS)
                 .build();

        jda.setActivity(playing("?help"));

        DiscordBotListAPI api = new DiscordBotListAPI.Builder()
               .token(topggAPIToken)
               .botId(botId)
               .build();
        int serverCount = (int) jda.getGuildCache().size();
        api.setStats(serverCount);
    }
}

