import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;

public class Main {
    public static JDA jda;
    public static String prefix = "?";

    public static String botId = "642587979193516043";
    public static String token = "SECRET";

    //TODO Final Bot ID: 628400349979344919
    //TODO Beta Bot ID: 642587979193516043

    //TODO Final Build Token: SECRET
    //TODO Beta Build Token: SECRET

    public static void main (String[] args) throws LoginException{
        jda = new JDABuilder(AccountType.BOT).setToken(token).build();

        jda.getPresence().setGame(Game.playing("?help"));
       // jda.addEventListener(new StickyCommand());
        jda.addEventListener(new Commands());
        jda.addEventListener(new JoinNewGuild());

        jda.addEventListener(new NewSticky());
    }
}
