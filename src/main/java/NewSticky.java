import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class NewSticky extends ListenerAdapter {

    Map<String, String> mapChannel = new HashMap<>();
    Map<String, String> mapMessage = new HashMap<>();


    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Member stickyBot = event.getGuild().getMemberById(Main.botId);


        if (args[0].equalsIgnoreCase(Main.prefix + "stick") && (permCheck(event.getMember() ))) {

            //Sends message if bot does not have add reaction perm
            if (!stickyBot.hasPermission(Permission.MESSAGE_ADD_REACTION)) {
                event.getChannel().sendMessage("**ERROR:** I __need__ the ``React to Messages`` permission to work correctly.").queue(); }
                    event.getMessage().addReaction("\u2705").queue();
                try {
                    //Puts channel ID and message into maps
                    mapChannel.put(event.getGuild().getId(), event.getChannel().getId());
                    mapMessage.put(event.getGuild().getId(), "__**Pinned Message:**__\n\n" + event.getMessage().getContentRaw().substring(7));

                } catch (Exception e) {
                }
        }

        //Adds X emote if user does not have perm to use stick / stickstop command
        if (args[0].equalsIgnoreCase(Main.prefix + "stick") && (!permCheck(event.getMember() ))) {
            event.getMessage().addReaction("\u274C").queue();
        }
        if (args[0].equalsIgnoreCase(Main.prefix + "stickstop") && (!permCheck(event.getMember() ))) {
            event.getMessage().addReaction("\u274C").queue();
        }

        //Stickstop command, puts makes message and channel maps to null
        if (args[0].equalsIgnoreCase(Main.prefix + "stickstop") && (permCheck(event.getMember() ))) {
            //adds X emote if user does not have perms to use command
            if (!permCheck(event.getMember()) || mapMessage.get(event.getGuild().getId()) == null) {
                event.getMessage().addReaction("\u274C").queue();
            } else {
                event.getMessage().addReaction("\u2705").queue();
            }
           if (mapMessage.get(event.getGuild().getId()) != null) {
               event.getChannel().getHistory().retrievePast(2).queue(m -> {
                  m.get(1).delete().queue();
               });
           }

            mapMessage.put(event.getGuild().getId(), null);
            mapChannel.put(event.getGuild().getId(), null);
        }

        //Posts sticky message if mapMessage for the guild is not null and message is not from the sticky bot and channel is the same as the one used to start sticky command
        if (mapMessage.get(event.getGuild().getId()) != null && !event.getAuthor().getId().equals(Main.botId) && event.getChannel().getId().equals(mapChannel.get(event.getGuild().getId()))) {
            TextChannel textChannel = event.getGuild().getTextChannelById(mapChannel.get(event.getGuild().getId()));
            textChannel.sendMessage(mapMessage.get(event.getGuild().getId())).queue();
        }

        //Checks last 3 massages every time one is sent to check if it contains the sticky message, if so deletes it.
        if(mapMessage.get(event.getGuild().getId()) != null) {
            event.getChannel().getHistory().retrievePast(3).queue(m -> {
                for (int i = 1; i < 3; i++) {
                    if (m.get(i).getContentDisplay().contains(mapMessage.get(event.getGuild().getId()))) {
                        m.get(i).delete().queue();
                    }
                }
            });
        }
    }

    public boolean permCheck(Member member) {
        if(member.hasPermission(Permission.MESSAGE_MANAGE)) {
            return true;
        } else {
            return false;
        }
    }
}
