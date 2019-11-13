import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class StickyCommand extends ListenerAdapter {
    Map<String, String> mapChannel = new HashMap<>();
    Map<String, String> mapMessage = new HashMap<>();
    Map<String, String> mapMessageId = new HashMap<>();

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
            String[] args = event.getMessage().getContentRaw().split("\\s+");
            Member nttBot = event.getGuild().getMemberById("628400349979344919");

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
            /////////////////////////////
            if (event.getMember() == nttBot && (event.getChannel().getId() == mapChannel.get(event.getGuild().getId())) && (event.getMessage().getContentDisplay().contains(mapMessage.get(event.getGuild().getId())))) {
                mapMessageId.put(event.getGuild().getId(), event.getMessageId());
            }

            //The issue is prob in this if statement below me...
            if(mapMessageId.get(event.getGuild().getId()) != null && event.getMember() != nttBot && mapMessage.get(event.getGuild().getId()) != null && (event.getChannel().getId() == mapChannel.get(event.getGuild().getId()) ))  {
                if(!args[0].equalsIgnoreCase(Main.prefix + "stickstop")) {
                    event.getChannel().sendMessage(mapMessage.get(event.getGuild().getId())).queue();
                }
                event.getChannel().deleteMessageById(mapMessageId.get(event.getGuild().getId())).queue();
            }

            if (args[0].equalsIgnoreCase(Main.prefix + "stick") && (permCheck(event.getMember() ))) {
                mapChannel.put(event.getGuild().getId(), event.getChannel().getId());

                try {
                    mapMessage.put(event.getGuild().getId(), "__**Pinned Message:**__\n\n" + event.getMessage().getContentRaw().substring(7));

                    if(!args[0].equalsIgnoreCase(Main.prefix + "stickstop")) {
                        event.getChannel().sendMessage(mapMessage.get(event.getGuild().getId())).queue();
                    }
                    event.getMessage().addReaction("\u2705").queue();

                } catch (Exception e) {
                    event.getChannel().sendMessage("Use the format ``?stick <message>``").queue();
                    event.getMessage().addReaction("\u274C").queue();
                }
            }

            if (args[0].equalsIgnoreCase(Main.prefix + "stickstop") && (permCheck(event.getMember() ))) {
                mapMessage.put(event.getGuild().getId(), null);
                mapChannel.put(event.getGuild().getId(), null);
                mapMessageId.put(event.getGuild().getId(), null);
                event.getMessage().addReaction("\u2705").queue();
            }

            if ((args[0].equalsIgnoreCase(Main.prefix + "stickstop") || args[0].equalsIgnoreCase(Main.prefix + "stick") ) && (!(permCheck(event.getMember() )))) {
                event.getMessage().addReaction("\u274C").queue();
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

