package dev.reassembly.utils

import dev.reassembly.SFTHBot
import kotlinx.coroutines.future.await
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel

object MessageUtils {

    suspend fun getLatestMessagesAsString(channel: TextChannel, removeMostRecent: Boolean): String {
        val words = getLatestMessages(channel, removeMostRecent)
        words.reverse()
        return words.joinToString(" ")
    }

    suspend fun getLatestMessages(channel: TextChannel, removeMostRecent: Boolean): MutableList<String> {
        val messages = channel.iterableHistory
        val words = mutableListOf<String>()
        val list = messages.takeAsync(500).await()
        val self = SFTHBot.getInstance().selfUser.id
        if (removeMostRecent) list.removeFirst()
        for (message in list) {
            if (message.author.id == self) break
            words.add(message.contentDisplay)
        }
        return words
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
}