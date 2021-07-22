import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;


public class HelpCommand extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        String prefix = "?";

        if (event.getAuthor().isBot()) {
            return;
        }

        if (Main.mapPrefix.containsKey(event.getGuild().getId())) {
            prefix = Main.mapPrefix.get(event.getGuild().getId());
        }

        //HELP or COMMANDS
        else if (args[0].equalsIgnoreCase(prefix + "help") || args[0].equalsIgnoreCase(prefix + "commands") || args[0].equalsIgnoreCase(Main.prefix + "help")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("**-StickyBot Commands-**");
            embed.setColor(Color.ORANGE);
            embed.setDescription("[www.stickybot.info](https://www.stickybot.info/)\n(Do not include `<>` when using commands)");
            embed.addField("\uD83D\uDCCC Sticky Commands:", "``" + prefix + "stick <message>`` - Sticks message to the channel.\n" +
                    "``" + prefix + "stickstop`` - Cancels stickied message.\n (*Member must have Manage Messages permissions to use sticky commands.*).", false);
            embed.addField("\uD83C\uDF9B Utility Commands:",
                    "``" + prefix + "poll <question>`` - Create a y/n poll for people to vote.\n" +
                            "``" + prefix + "apoll <question, option1, option2>`` - Create a multiple choice poll. Separate the question and options with commas. Supports up to 7 options.\n" +
                            "``" + prefix + "userinfo <@user>`` - Get info on a member. (@user can be a mention, ID, or left blank).\n" +
                            "``" + prefix + "serverinfo`` - Get info on the server.\n" +
                            "``" + prefix + "embed <message>`` - Turns your message into a embed.\n"
                    , false);

            embed.addField("\uD83D\uDE04 Fun Commands:",
                    "``" + prefix + "roll`` - Roll two dice.\n" +
                            "``" + prefix + "weather <location>`` - Get the current weather in a city.\n" +
                            "``" + prefix + "wiki <article>`` - Get the requested Wikipedia article.\n" +
                            "``" + prefix + "wiki random`` - Get a random Wikipedia article.\n" +
                            "``" + prefix + "wikihow`` - Get a random WikiHow article.\n" +
                            "``" + prefix + "urban <lookup>`` - Look something up on the Urban Dictionary.\n" +
                            "``" + prefix + "love <name1, name2>`` - Get the compatibility % on two names *(real names, not discord tags)*.\n" +
                            "``" + prefix + "coinflip`` - Flips a coin."
                    , false);


            embed.addField("\uD83D\uDCC3 Other Commands:",
                    "``" + prefix + "invite`` - Invite link for StickyBot.\n" +
                            "``" + prefix + "support`` - Invite to the StickyBot Support Server.\n" +
                            "``" + prefix + "about`` - Information about StickyBot.\n" +
                            "``" + prefix + "donate`` - Help keep StickyBot running smoothly.\n" +
                            "``" + prefix + "permcheck`` - Check if StickyBot has all the needed permissions.\n" +
                            "``" + prefix + "premium`` - Info about StickyBot Premium.\n" +
                            "``" + prefix + "disablecommands`` - Disable all non-sticky commands.\n" +
                            "``" + prefix + "enablecommands`` - Enable all non-sticky commands.\n"
                    , false);


            embed.addField("\uD83E\uDDE1 Premium Commands:",
                    "``" + prefix + "stickembed <message>`` - Creates a sticky with a embed.\n" +
                            "``" + prefix + "stickslow <message>`` - Creates a sticky that sends slower than a normal sticky.\n" +
                            "``" + prefix + "setimage <image link>`` - Sets image for sticky embed in the channel.\n" +
                            "``" + prefix + "removeimage`` - Removes image for sticky embed in the channel.\n" +
                            "``" + prefix + "setbigimage <image link>`` - Sets big image for sticky embed in the channel.\n" +
                            "``" + prefix + "removebigimage`` - Removes big image for sticky embed in the channel.\n" +
                            "``" + prefix + "getimage`` - See the current channels sticky embed image & link.\n" +
                            "``" + prefix + "prefix <prefix>`` - Sets StickyBots prefix.\n" +
                            "``?resetprefix`` - Resets prefix to `?`.\n" +
                            "(Sticky embed color will be the color of StickyBots role).\n" +
                            "(*Member must have Manage Server permissions to use prefix & image commands.*).", false);

            embed.addField("Prefix:", "This guilds prefix: `" + prefix + "`", true);

            embed.addField("Premium Status:", PremiumStatus(event.getGuild().getId()), true);

            embed.setFooter("Join our support server with any questions. We are happy to help!", Main.jda.getShards().get(0).getSelfUser().getAvatarUrl());

            try {
                event.getMessage().replyEmbeds(embed.build()).setActionRows(
                        ActionRow.of(Button.link("https://www.stickybot.info", "Website").withEmoji(Emoji.fromMarkdown("<:StickyBotCircle:693004145065590856>")),
                                Button.link("https://discord.com/invite/SvNQTtf", "Support Server").withEmoji(Emoji.fromMarkdown("<:discordEmote:853160010305765376>"))),
                        ActionRow.of(Button.link("https://www.stickybot.info/premium", "Premium").withEmoji(Emoji.fromMarkdown("\uD83E\uDDE1")),
                                Button.link("https://docs.stickybot.info", "Docs").withEmoji(Emoji.fromMarkdown("<:iBlue:860060995979706389>")))
                ).queue(null, (error) -> event.getChannel().sendMessage("I need the `Embed Links` Permission!").queue());
            } catch (Exception e) {
                event.getMessage().reply("I need the `Embed Links` Permission!").queue();
            }
        }
    }

    public static String PremiumStatus(String guildId) {
        if (Main.premiumGuilds.containsValue(guildId)) {
            return "[`Premium Active`](https://www.stickybot.info/manage-subscription)\uD83D\uDC9B";
        } else {
            return "[`Premium Not Active`](https://www.stickybot.info/premium)";
        }
    }
}



