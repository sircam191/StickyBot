package sircam.sbot.command

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

object Ping {
    fun run(e: GuildMessageReceivedEvent, args: List<String>) {
        e.channel.sendMessage("Pong!\n> WebSocket Latency: ${e.jda.gatewayPing}ms").queue()
    }
}