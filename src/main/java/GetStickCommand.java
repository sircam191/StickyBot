import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GetStickCommand extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        String prefix = "?";
        String guildID = event.getGuild().getId();

        if (event.getAuthor().isBot()) {
            return;
        }

        if (Main.mapPrefix.containsKey(event.getGuild().getId())) {
            prefix = Main.mapPrefix.get(event.getGuild().getId());
        }



        if (args[0].equalsIgnoreCase(prefix + "getstick") || args[0].equalsIgnoreCase(prefix + "getsticks") || args[0].equalsIgnoreCase(prefix + "getstickies")) {

            if (!event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                event.getMessage().reply("Whoops! You need the `Manage Messages` permission to use this command.").queue();
                return;
            }

            if (getClassicStickyChannels(guildID).isEmpty() && getSlowStickyChannels(guildID).isEmpty() && getEmbedStickyChannels(guildID).isEmpty() && getWebhookStickyChannels(guildID).isEmpty()) {
                event.getMessage().reply("No active stickies in this server!\nYou can make one with `" + prefix + "stick` or use `" + prefix + "help` for a full list of commands.").queue();
                return;
            }

            Member stickyBot = event.getGuild().getMemberById(Main.botId);
            EmbedBuilder emb = new EmbedBuilder();
            emb.setColor(Color.ORANGE)
                    .setTitle("-Active Stickies in **" + event.getGuild().getName() + "**-")
                    .setFooter("StickyBot", Main.jda.getShards().get(0).getSelfUser().getAvatarUrl());


            if (!getClassicStickyChannels(guildID).isEmpty()) {
                //Add classic stickies to embed
                for (String channelID : getClassicStickyChannels(guildID)) {
                    emb.addField("Classic Sticky:", "Channel: " + event.getGuild().getGuildChannelById(channelID).getAsMention() + "\n__Stickied Message:__```\n" + Main.mapMessage.get(channelID) + "```", false);
                }
            }
            if (!getSlowStickyChannels(guildID).isEmpty()) {
                //Add Sticky Slow to embed
                for (String channelID : getSlowStickyChannels(guildID)) {
                    emb.addField("Slow Sticky:", "Channel: " + event.getGuild().getGuildChannelById(channelID).getAsMention() + "\n__Stickied Message:__```\n" + Main.mapMessageSlow.get(channelID) + "```", false);
                }
            }
            if (!getEmbedStickyChannels(guildID).isEmpty()) {
                //Add Sticky Embeds to embed
                for (String channelID : getEmbedStickyChannels(guildID)) {
                    emb.addField("Sticky Embed:", "Channel: " + event.getGuild().getGuildChannelById(channelID).getAsMention() + "__\nStickied Message:__```\n" + Main.mapMessageEmbed.get(channelID) + "```", false);
                }
            }
            if (!getWebhookStickyChannels(guildID).isEmpty()) {
                //Add Sticky WebHook to embed
                for (String channelID : getEmbedStickyChannels(guildID)) {
                    emb.addField("WebHook Sticky Embed:", "Channel: " + event.getGuild().getGuildChannelById(channelID).getAsMention() + "__\nStickied Message:__```\n" + Main.webhookMessage.get(channelID) + "```", false);
                }
            }

            event.getMessage().replyEmbeds(emb.build()).queue();

        }
    }


    public List<String> getClassicStickyChannels(String guildId) {
        List<String> channelIds = Main.jda.getGuildById(guildId).getTextChannels().stream().map(textChannel -> textChannel.getId()).collect(Collectors.toList());
        List<String> stickyChannelIDs = new ArrayList<>();
        for (String id : channelIds) {
            if (Main.mapMessage.containsKey(id)) {
                stickyChannelIDs.add(id);
            }
        }
        return stickyChannelIDs;
    }

    public List<String> getSlowStickyChannels(String guildId) {
        List<String> channelIds = Main.jda.getGuildById(guildId).getTextChannels().stream().map(textChannel -> textChannel.getId()).collect(Collectors.toList());
        List<String> stickyChannelIDs = new ArrayList<>();
        for (String id : channelIds) {
            if (Main.mapMessageSlow.containsKey(id)) {
                stickyChannelIDs.add(id);
            }
        }
        return stickyChannelIDs;
    }

    public List<String> getEmbedStickyChannels(String guildId) {
        List<String> channelIds = Main.jda.getGuildById(guildId).getTextChannels().stream().map(textChannel -> textChannel.getId()).collect(Collectors.toList());
        List<String> stickyChannelIDs = new ArrayList<>();
        for (String id : channelIds) {
            if (Main.mapMessageEmbed.containsKey(id)) {
                stickyChannelIDs.add(id);
            }
        }
        return stickyChannelIDs;
    }

    public List<String> getWebhookStickyChannels(String guildId) {
        List<String> channelIds = Main.jda.getGuildById(guildId).getTextChannels().stream().map(textChannel -> textChannel.getId()).collect(Collectors.toList());
        List<String> stickyChannelIDs = new ArrayList<>();
        for (String id : channelIds) {
            if (Main.webhookMessage.containsKey(id)) {
                stickyChannelIDs.add(id);
            }
        }
        return stickyChannelIDs;
    }

}
