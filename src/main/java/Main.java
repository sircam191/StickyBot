
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static net.dv8tion.jda.api.entities.Activity.playing;

public class Main {
    public static JDA jda;
    public static String prefix = "?";

    public static String botId = "******************";
    public static String token = "*******************************************";

    public static String dbUrl = "jdbc:mysql://***********/stickybot";
    public static String dbUser = "root";
    public static String dbPassword = "";

    public static Map<String, String> mapMessage = new HashMap<>();
    public static Map<String, String> mapDeleteId = new HashMap<>();


    //TODO Final Bot ID: ******************
    //TODO Beta Bot ID: ******************

    //TODO Final Build Token: *********************************************************
    //TODO Beta Build Token: *********************************************************

    public static void main (String[] args) throws LoginException{

        //MySQL DB
        try {
            Connection dbConn = DriverManager.getConnection(dbUrl,dbUser,dbPassword);
            Statement myStmt = dbConn.createStatement();
            String sql = "select * from messages";
            ResultSet rs = myStmt.executeQuery(sql);

            while (rs.next()) {
                mapMessage.put(rs.getString("channelId"),rs.getString("message"));
                mapDeleteId.put(rs.getString("channelId"),"");
                System.out.println(rs.getString("channelId") + " -> " + rs.getString("message"));
            }

        } catch ( SQLException e) {
            e.printStackTrace();
        }

        jda = new JDABuilder(token).build();

        jda.getPresence().setActivity(playing("?help"));

        jda.addEventListener(new Commands());
        jda.addEventListener(new JoinNewGuild());
        jda.addEventListener(new Sticky());
        jda.addEventListener(new VirusCommand());
    }

}

