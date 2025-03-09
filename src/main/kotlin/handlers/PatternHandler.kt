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
            logger.info("Preparing to deploy onto new thread for ${event.message.author.name}")
            handleDuplicates(event)
        }
    }

    suspend fun handleDuplicates(event: MessageReceivedEvent) = withContext(Dispatchers.Default) {
        logger.info("Handed off to new thread")
        if (!event.isFromGuild) return@withContext
        val server = DatabaseHandler.getServer(event.guild.id) ?: return@withContext
        if (event.channelType != ChannelType.TEXT) return@withContext


        val channel = event.channel.asTextChannel()
        if (channel.id != server.patternChannel && channel.id != server.chainChannel) return@withContext

        logger.info("Started Checking")
        val words = MessageUtils.getLatestMessages(channel, true).map { it.lowercase() }

        if (words.contains(event.message.contentDisplay.lowercase())) {
            logger.info("Found Repeated Message")
            event.message.reply("Oops! That message has already been sent. You said " + words.count() + " different words. Please restart.").queue()
            return@withContext
        }

        logger.info("Couldn't find a repeated message")
    }
}