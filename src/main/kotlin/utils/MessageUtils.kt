package dev.reassembly.utils

import dev.reassembly.SFTHBot
import kotlinx.coroutines.future.await
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.channel.Channel
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel

object MessageUtils {

    suspend fun getLatestMessagesAsString(channel: Channel, removeMostRecent: Boolean): String {
        val words = getLatestMessages(channel, removeMostRecent)
        words.reverse()
        return words.joinToString(" ")
    }

    suspend fun getLatestMessages(channel: Channel, amount: Int, removeMostRecent: Boolean): MutableList<String> {
        val words = mutableListOf<String>()

        if (channel !is ThreadChannel && channel !is TextChannel) return words
        val messages = channel.iterableHistory
        val list = messages.takeAsync(amount).await()
        val self = SFTHBot.getInstance().selfUser.id
        if (removeMostRecent) list.removeFirst()
        for (message in list) {
            if (message.author.id == self) break
            words.add(message.contentDisplay)
        }
        return words
    }

    suspend fun getLatestMessages(channel: Channel, removeMostRecent: Boolean): MutableList<String> {
        return getLatestMessages(channel, 2500, removeMostRecent)
    }

    suspend fun getLatestMessageObjects(channel: TextChannel, removeMostRecent: Boolean): MutableList<Message> {
        val allMessages = channel.iterableHistory.takeAsync(500).await()
        val cutOffMessages = mutableListOf<Message>()
        val self = SFTHBot.getInstance().selfUser.id
        if (removeMostRecent) allMessages.removeFirst()
        for (message in allMessages) {
            if (message.author.id == self) break
            cutOffMessages.add(message)
        }
        return cutOffMessages
    }

    suspend fun safeSendMessage(message: String, channel: GuildMessageChannel, useCodeBlocks: Boolean = true) {
        var messageSegment = if (useCodeBlocks) "```" else ""
        var extraNeededChars = if (useCodeBlocks) 3 else 0

        for (word in message.split(' ')) {
            if (messageSegment.length + word.length + extraNeededChars > 2000) {
                messageSegment = messageSegment.removeSuffix(" ")
                messageSegment += "```"
                channel.sendMessage(messageSegment).queue();
                messageSegment = if (useCodeBlocks) "```" else ""
            } else {
                messageSegment += "$word "
            }
        }

        if (messageSegment.isNotEmpty()) {
            messageSegment = messageSegment.removeSuffix(" ")
            messageSegment += "```"
            channel.sendMessage(messageSegment).queue();
        }
    }
}