package dev.reassembly.handlers

import dev.reassembly.database.DatabaseHandler
import dev.reassembly.utils.MessageUtils
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object PatternHandler: ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) = runBlocking {
        if (!event.isFromGuild) return@runBlocking
        val server = DatabaseHandler.getServer(event.guild.id) ?: return@runBlocking
        if (event.channelType != ChannelType.TEXT) return@runBlocking

        val channel = event.channel.asTextChannel()
        if (channel.id != server.patternChannel || channel.id != server.chainChannel) return@runBlocking
        val words = MessageUtils.getLatestMessages(channel, true).map { it.lowercase() }

        if (event.message.contentDisplay.lowercase() in words) {
            event.message.reply("Oops! That message has already been sent. You said " + words.count() + " different words. Please restart.").queue()
            return@runBlocking
        }
    }
}