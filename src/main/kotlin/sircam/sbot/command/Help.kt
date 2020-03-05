package sircam.sbot.command

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import sircam.sbot.utils.Config
import java.awt.Color

object Help {
    fun run(e: GuildMessageReceivedEvent, args: List<String>) {
        //TODO: Automatically generate help message using KukiTeam's brand new library
        e.channel.sendMessage(
            EmbedBuilder()
                .setTitle("**Commands**")
                .addField("Sticky Commands:",
                    "``${Config.prefix}stick <message>`` - Sticks message to the channel.\n" +
                            "``${Config.prefix}stickstop`` - Cancels stickied message.\n (*Member must have Manage Messages permissions to use sticky commands.*).",
                    false
                ).addField(
                    "Other Commands:",
                    "``${Config.prefix}about`` - Support Server and other useful info.\n" +
                            "``${Config.prefix}poll <question>`` - Create a poll for people to vote.\n" +
                            "``${Config.prefix}userinfo <@user>`` - Get info on a member.\n" +
                            "``${Config.prefix}erverinfo`` - Get info on the server.\n" +
                            "``${Config.prefix}roll`` - Role two dice.\n" +
                            "``${Config.prefix}faq`` - Get the FAQ for StickyBot.\n" +
                            "``${Config.prefix}invite`` - Invite link for StickyBot.\n"
                    , false
                ).setFooter(
                    "For support please join the Support Server. Use ${Config.prefix}about for the invite.",
                    "https://cdn.discordapp.com/attachments/641158383138897943/663491981111984129/SupportServer1.png"
                )
                .setColor(Color.decode("#2f3136"))
                .build()
        ).queue()
    }
}