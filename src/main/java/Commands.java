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
    Map<String, String> mapDeleteId = new HashMap<>();

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Member stickyBot = event.getGuild().getMemberById(Main.botId);

        if (args[0].equalsIgnoreCase(Main.prefix + "stick")) {
            if (permCheck(event.getMember())) {
                //Sends message if bot does not have add reaction perm
                if (!stickyBot.hasPermission(Permission.MESSAGE_ADD_REACTION)) {
                    event.getChannel().sendMessage("**ERROR:** I __need__ the ``React to Messages`` permission to work correctly.").queue();
                }

                try {
                    //Puts channel ID and message into maps
                    mapChannel.put(event.getGuild().getId(), event.getChannel().getId());
                    mapMessage.put(event.getGuild().getId(), "__**Pinned Message:**__\n\n" + event.getMessage().getContentRaw().substring(7));
                    //Adds check mark if the have perms to use command
                    event.getMessage().addReaction("\u2705").queue();
                } catch (Exception e) {
                }

                } else {
                    //Adds X emote if they do not have perms to use sticky command
                    event.getMessage().addReaction("\u274C").queue();
                }
            }

        //Stickstop command, makes message and channel maps to null
        if (args[0].equalsIgnoreCase(Main.prefix + "stickstop")) {
           if(permCheck(event.getMember())) {
               //adds X emote if user does not have perms to use command
               if (!permCheck(event.getMember()) || mapMessage.get(event.getGuild().getId()) == null) {
                   event.getMessage().addReaction("\u274C").queue();
               } else {
                   event.getMessage().addReaction("\u2705").queue();
               }
               if(mapMessage.get(event.getGuild().getId()) != null) {
                   event.getChannel().deleteMessageById(mapDeleteId.get(event.getGuild().getId())).queue();
               }

               mapMessage.put(event.getGuild().getId(), null);
               mapChannel.put(event.getGuild().getId(), null);
               mapDeleteId.put(event.getGuild().getId(), null);
           } else {
               event.getMessage().addReaction("\u274C").queue();
           }
        }

        //Posts sticky message if mapMessage for the guild is not null and message is not from the sticky bot and channel is the same as the one used to start sticky command
        if (mapMessage.get(event.getGuild().getId()) != null && !event.getAuthor().getId().equals(Main.botId) && event.getChannel().getId().equals(mapChannel.get(event.getGuild().getId()))) {
            TextChannel textChannel = event.getGuild().getTextChannelById(mapChannel.get(event.getGuild().getId()));
            textChannel.sendMessage(mapMessage.get(event.getGuild().getId())).queue(m -> mapDeleteId.put(event.getGuild().getId(), m.getId()));
        }

        //Deletes old sticky message by message ID
        if(mapMessage.get(event.getGuild().getId()) != null) {
           try {
               event.getChannel().deleteMessageById(mapDeleteId.get(event.getGuild().getId())).queue();
           } catch (Exception e) {
               System.out.println("delete message error does not really matter");
           }
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
