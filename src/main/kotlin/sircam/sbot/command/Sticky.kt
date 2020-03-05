package sircam.sbot.command

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.util.*

object Sticky {

    private val mapMessage: HashMap<String, String> = HashMap()
    private val mapDeleteId: HashMap<String, String> = HashMap()

    fun start(e: GuildMessageReceivedEvent, args: List<String>) {
        if (permCheck(e.member!!)) {
            mapMessage[e.channel.id] = "__**Stickied Message:**__\n\n${args.drop(2).joinToString(" ")}"
            e.message.addReaction("\u2705").queue()
        } else {
            e.message.addReaction("\u274C").queue()
        }
    }

    fun stop(e: GuildMessageReceivedEvent, args: List<String>) {
        if (permCheck(e.member!!)) {
            e.channel.deleteMessageById(mapDeleteId[e.channel.id]!!).queue()
            e.message.addReaction("\u2705").queue()
        } else {
            e.message.addReaction("\u274C").queue()
        }
    }

    fun permCheck(m: Member): Boolean  = m.hasPermission(Permission.MESSAGE_MANAGE)
}