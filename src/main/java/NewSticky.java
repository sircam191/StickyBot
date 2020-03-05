import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sticky extends ListenerAdapter {

    Map<String, String> mapMessage = new HashMap<>();
    Map<String, String> mapDeleteId = new HashMap<>();

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Member stickyBot = event.getGuild().getMemberById(Main.botId);
        Guild stickyServer = event.getJDA().getGuildById("641158383138897941");

        if (args[0].equalsIgnoreCase(Main.prefix + "stick") && (permCheck(event.getMember() ))) {
            mapMessage.put(event.getChannel().getId(), "__**Stickied Message:**__\n\n" + event.getMessage().getContentRaw().substring(7));
            event.getMessage().addReaction("\u2705").queue();

        } else if (args[0].equalsIgnoreCase(Main.prefix + "stick") && (!permCheck(event.getMember() ))) {
            event.getMessage().addReaction("\u274C").queue();
        }

        if ( (args[0].equalsIgnoreCase(Main.prefix + "stickstop") || args[0].equalsIgnoreCase(Main.prefix + "unstick")) && (permCheck(event.getMember() ))) {
            event.getChannel().deleteMessageById(mapDeleteId.get(event.getChannel().getId())).queue();
            event.getMessage().addReaction("\u2705").queue();
        } else  if ( (args[0].equalsIgnoreCase(Main.prefix + "stickstop") || args[0].equalsIgnoreCase(Main.prefix + "unstick")) && (!permCheck(event.getMember() ))) {
            event.getMessage().addReaction("\u274C").queue();
        }

        if ( (args[0].equalsIgnoreCase(Main.prefix + "stickstop") || args[0].equalsIgnoreCase(Main.prefix + "unstick")) && (permCheck(event.getMember() ))) {
            //adds X emote if user does not have perms to use command
            if (!permCheck(event.getMember()) || mapMessage.get(event.getChannel().getId()) == null) {
                event.getMessage().addReaction("\u274C").queue();
            } else {
                event.getMessage().addReaction("\u2705").queue();
            }
//            if(mapMessage.get(event.getChannel().getId()) != null) {
//                event.getChannel().deleteMessageById(mapDeleteId.get(event.getChannel().getId())).queue();
//            }
            mapMessage.remove(event.getChannel().getId());
            mapDeleteId.remove(event.getChannel().getId());
        }

        if (mapMessage.get(event.getChannel().getId()) != null && !event.getAuthor().getId().equals(Main.botId)) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            event.getChannel().sendMessage(mapMessage.get(event.getChannel().getId())).queue(m -> mapDeleteId.put(event.getChannel().getId(), m.getId()));
        }


        if (mapDeleteId.get(event.getChannel().getId()) != null && (event.getChannel().getLatestMessageId() != mapDeleteId.get(event.getChannel().getId()))) {

            List<Message> messageCheck = event.getChannel().getHistory().retrievePast(1).complete();

           if (!messageCheck.get(0).getContentRaw().contains(mapMessage.get(event.getChannel().getId()))) {
               event.getChannel().deleteMessageById(mapDeleteId.get(event.getChannel().getId())).queue();
           } else {
               List<Message> messageListDelete = event.getChannel().getHistory().retrievePast(10).complete();
               for (Message m : messageListDelete.subList(1, 10)) {
                   if (m.getContentRaw().contains(mapMessage.get(event.getChannel().getId()))) {
                       m.delete().queue();
                   }
               }
           }
        }
    }

    public boolean permCheck(Member member) {
        if (member.hasPermission(Permission.MESSAGE_MANAGE)) {
            return true;
        } else {
            return false;
        }
    }
}
