package dev.reassembly.handlers

import dev.reassembly.database.DatabaseHandler
import dev.reassembly.utils.MessageUtils
import kotlinx.coroutines.*
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory
import kotlin.math.log

object PatternHandler: ListenerAdapter() {

    val logger = LoggerFactory.getLogger(this::class.java)

    override fun onMessageReceived(event: MessageReceivedEvent) {
        CoroutineScope(Dispatchers.Default).launch {
            handleDuplicates(event)
        }
    }

    suspend fun handleDuplicates(event: MessageReceivedEvent) = withContext(Dispatchers.Default) {
        if (!event.isFromGuild) return@withContext
        if (event.author.isBot || event.author.isSystem) return@withContext
        val server = DatabaseHandler.getServer(event.guild.id) ?: return@withContext
        if (event.channelType != ChannelType.TEXT) return@withContext

        val channelType = event.channelType
        val channel = if (channelType == ChannelType.TEXT) event.channel.asTextChannel() else event.channel.asThreadChannel()

        if (channel.id != server.patternChannel && channel.id != server.chainChannel) return@withContext

        if (channel.id == server.chainChannel) {
            val lastMessage = MessageUtils.getLatestMessages(channel, 2, true).first().lowercase()
            val lastLength = lastMessage.length
            val newMessage = event.message.contentDisplay.lowercase()
            if (lastMessage[lastLength - 2] != newMessage[0] || lastMessage[lastLength - 1] != newMessage[1]) {
                event.message.delete().queue()
                event.author.openPrivateChannel().submit().get().sendMessage("You need to make sure your word's two starting letters are the two ending letters of the previous word!").queue()
                return@withContext
            }
        }

        logger.info("Started Checking")
        val words = MessageUtils.getLatestMessages(channel, 400, true).map { it.lowercase() }

        if (words.contains(event.message.contentDisplay.lowercase())) {
            logger.info("Found Repeated Message")
            event.message.reply("Oops! That message has already been sent. You said " + words.count() + " different words. Please restart.").queue()
            return@withContext
        }

        logger.info("Couldn't find a repeated message")
    }
}