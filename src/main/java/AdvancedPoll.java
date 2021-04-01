import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;


public class AdvancedPoll extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if(event.getAuthor().isBot()) {
            return;
        }

        String prefix = "?";
        if(Main.mapPrefix.containsKey(event.getGuild().getId())) {
            prefix = Main.mapPrefix.get(event.getGuild().getId());
        }


        if (args[0].equalsIgnoreCase(prefix + "apoll") || args[0].equalsIgnoreCase(prefix + "advancedpoll")) {


            try {
                String rawInput = event.getMessage().getContentRaw().replace(prefix + "apoll", "");

                String[] options = rawInput.split("\\s*,\\s*");
                EmbedBuilder emb = new EmbedBuilder();
                emb.setFooter("Poll by: " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
                emb.setColor(event.getGuild().getMemberById(Main.botId).getColor());
                emb.setDescription(options[0]);
                emb.setTitle("POLL:");


                if(options.length > 7) {
                    emb.addField("Options:",
                            "\uD83C\uDDE6**:** " + options[1] +
                                    "\n\uD83C\uDDE7**:** " + options[2] +
                                    "\n\uD83C\uDDE8**:** " + options[3] +
                                    "\n\uD83C\uDDE9**:** " + options[4] +
                                    "\n\uD83C\uDDEA**:** " + options[5] +
                                    "\n\uD83C\uDDEB**:** " + options[6] +
                                    "\n\uD83C\uDDEC**:** " + options[7]
                            , false);
                    event.getChannel().sendMessage(emb.build()).queue(m -> {
                        m.addReaction("\uD83C\uDDE6").queue();
                        m.addReaction("\uD83C\uDDE7").queue();
                        m.addReaction("\uD83C\uDDE8").queue();
                        m.addReaction("\uD83C\uDDE9").queue();
                        m.addReaction("\uD83C\uDDEA").queue();
                        m.addReaction("\uD83C\uDDEB").queue();
                        m.addReaction("\uD83C\uDDEC").queue();
                    });
                    event.getMessage().delete().queue();
                }
                else if(options.length > 6) {
                    emb.addField("Options:",
                            "\uD83C\uDDE6**:** " + options[1] +
                                    "\n\uD83C\uDDE7**:** " + options[2] +
                                    "\n\uD83C\uDDE8**:** " + options[3] +
                                    "\n\uD83C\uDDE9**:** " + options[4] +
                                    "\n\uD83C\uDDEA**:** " + options[5] +
                                    "\n\uD83C\uDDEB**:** " + options[6]
                            , false);
                    event.getChannel().sendMessage(emb.build()).queue(m -> {
                        m.addReaction("\uD83C\uDDE6").queue();
                        m.addReaction("\uD83C\uDDE7").queue();
                        m.addReaction("\uD83C\uDDE8").queue();
                        m.addReaction("\uD83C\uDDE9").queue();
                        m.addReaction("\uD83C\uDDEA").queue();
                        m.addReaction("\uD83C\uDDEB").queue();
                    });
                    event.getMessage().delete().queue();
                }
                else if(options.length > 5) {

                    emb.addField("Options:",
                            "\uD83C\uDDE6**:** " + options[1] +
                                    "\n\uD83C\uDDE7**:** " + options[2] +
                                    "\n\uD83C\uDDE8**:** " + options[3] +
                                    "\n\uD83C\uDDE9**:** " + options[4] +
                                    "\n\uD83C\uDDEA**:** " + options[5]
                            , false);
                    event.getChannel().sendMessage(emb.build()).queue(m -> {
                        m.addReaction("\uD83C\uDDE6").queue();
                        m.addReaction("\uD83C\uDDE7").queue();
                        m.addReaction("\uD83C\uDDE8").queue();
                        m.addReaction("\uD83C\uDDE9").queue();
                        m.addReaction("\uD83C\uDDEA").queue();
                    });
                    event.getMessage().delete().queue();
                }
                else if(options.length > 4) {
                    emb.addField( "Options:",
                            "\uD83C\uDDE6**:** " + options[1] +
                            "\n\uD83C\uDDE7**:** " + options[2] +
                            "\n\uD83C\uDDE8**:** " + options[3] +
                            "\n\uD83C\uDDE9**:** " + options[4]
                    , false);
                    event.getChannel().sendMessage(emb.build()).queue(m -> {
                        m.addReaction("\uD83C\uDDE6").queue();
                        m.addReaction("\uD83C\uDDE7").queue();
                        m.addReaction("\uD83C\uDDE8").queue();
                        m.addReaction("\uD83C\uDDE9").queue();
                    });
                    event.getMessage().delete().queue();
                } else if(options.length > 3) {

                    emb.addField("Options:",
                            "\uD83C\uDDE6**:** " + options[1] +
                                    "\n\uD83C\uDDE7**:** " + options[2] +
                                    "\n\uD83C\uDDE8**:** " + options[3]
                            , false);
                    event.getChannel().sendMessage(emb.build()).queue(m -> {
                        m.addReaction("\uD83C\uDDE6").queue();
                        m.addReaction("\uD83C\uDDE7").queue();
                        m.addReaction("\uD83C\uDDE8").queue();
                    });
                    event.getMessage().delete().queue();
                } else if(options.length > 2) {
                    emb.addField("Options:",
                            "\uD83C\uDDE6**:** " + options[1] +
                                    "\n\uD83C\uDDE7**:** " + options[2]
                            , false);
                    event.getChannel().sendMessage(emb.build()).queue(m -> {
                        m.addReaction("\uD83C\uDDE6").queue();
                        m.addReaction("\uD83C\uDDE7").queue();
                    });
                    event.getMessage().delete().queue();
                } else {
                    event.getChannel().sendMessage("Please use this format: `" + prefix + "apoll What is your fav color?, Option1, Option2`.\n(*You can have up to 4 options*)").queue();
                }
            } catch (Exception e) {
                event.getChannel().sendMessage("Whoops!\nPlease make sure StickyBot has the `Manage Messages` and `Embed Links` permissions.").queue();
            }

        }

    }

}


/*

?apoll option1, option3

A: \uD83C\uDDE6 (blue)
B: \uD83C\uDDE7 (blue)
C: \uD83C\uDDE8 (blue)

 */
