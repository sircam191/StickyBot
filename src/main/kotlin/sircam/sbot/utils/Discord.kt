package sircam.sbot.utils

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import sircam.sbot.command.Help
import sircam.sbot.command.Ping
import sircam.sbot.command.Sticky
import java.awt.Color

object Discord {
    private val jda = JDABuilder(Config.token)
        .addEventListeners(CommandHandler)
        .setActivity(Activity.playing("${Config.prefix}help"))
        .build()

    object CommandHandler: ListenerAdapter() {
        override fun onGuildMessageReceived(e: GuildMessageReceivedEvent) {
            //TODO: Later use KukiTeam's brand new library to make this whole thing simpler
            val cmd = e.message.contentRaw.split(Config.prefix, limit=2)[1].split(" ")
            when(cmd[0]) {
                "stick" -> Sticky.start(e, cmd)
                "unstick",
                "stickstop" -> Sticky.stop(e, cmd)
                "help",
                "commands" -> Help.run(e, cmd)
                "ping" -> Ping.run(e, cmd)
            }
        }

        override fun onGuildJoin(e: GuildJoinEvent) {
            val g = e.guild
            g.defaultChannel!!.sendMessage("Thanks for adding ${e.jda.selfUser.asMention} !\n-Use **?help** for a list of commands.").queue()
            e.jda.getTextChannelById(Config.channel)!!.sendMessage(
                EmbedBuilder()
                    .setTitle("StickyBOT Joined a new server!")
                    .addField("Server Name: ", g.name, false)
                    .addField("Guild Members: ", g.members.size.toString(), false)
                    .addField("Guild Region: ", "${g.region.emoji} ${g.region.name}", false)
                    .addField("Guild Owner: ", g.owner!!.asMention, false)
                    .setFooter("StickyBot is now in ${e.jda.guilds.size} guilds", e.jda.selfUser.effectiveAvatarUrl)
                    .setThumbnail(e.guild.iconUrl)
                    .setColor(Color.decode("#2f3136"))
                    .build()
            ).queue()
        }
    }
}