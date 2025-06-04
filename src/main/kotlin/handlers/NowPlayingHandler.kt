package dev.reassembly.handlers

import dev.reassembly.SFTHBot
import dev.reassembly.database.DatabaseHandler
import dev.reassembly.database.datastore
import kotlinx.coroutines.delay
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import org.slf4j.LoggerFactory

object NowPlayingHandler {

    private val logger = LoggerFactory.getLogger(NowPlayingHandler::class.java)

    suspend fun runNowPlayingUpdateLoop() {
        while (true) {
            for (server in DatabaseHandler.getAllServersToUpdate()) {
                val guild: Guild? = SFTHBot.getInstance().getGuildById(server.id) ?: run {
                    datastore.delete(server)
                    null
                }
                if (guild == null) continue
                if (server.currentlyPlayingMessageChannel.isEmpty()) continue
                updateNowPlaying(guild)
            }
            delay(60000L)
        }
    }

    private fun updateNowPlaying(guild: Guild) {
        var message = "Currently Active Games:\n"

        val server = DatabaseHandler.getServer(guild.id) ?: return
        if (server.changeChannel.isNotEmpty()) {
            val channel = guild.getTextChannelById(server.changeChannel) ?: return
            if (isActive(channel)) {
                message += "\nChange: " + channel.asMention
            }
        }
        if (server.letterChannel.isNotEmpty()) {
            val channel = guild.getTextChannelById(server.letterChannel) ?: return
            if (isActive(channel)) {
                message += "\nOne Word Letter: " + channel.asMention
            }
        }
        if (server.patternChannel.isNotEmpty()) {
            val channel = guild.getTextChannelById(server.patternChannel) ?: return
            if (isActive(channel)) {
                message += "\nPattern: " + channel.asMention
            }
        }
        if (server.freezeTagChannel.isNotEmpty()) {
            val channel = guild.getTextChannelById(server.freezeTagChannel) ?: return
            if (isActive(channel)) {
                message += "\nFreeze Tag: " + channel.asMention
            }
        }
        if (server.lateForWorkChannel.isNotEmpty()) {
            val channel = guild.getTextChannelById(server.lateForWorkChannel) ?: return
            if (isActive(channel)) {
                message += "\nLate For Work: " + channel.asMention
            }
        }
        if (server.partyQuirksChannel.isNotEmpty()) {
            val channel = guild.getTextChannelById(server.partyQuirksChannel) ?: return
            if (isActive(channel)) {
                message += "\nParty Quirks: " + channel.asMention
            }
        }
        if (server.bedtimeStoriesChannel.isNotEmpty()) {
            val channel = guild.getTextChannelById(server.bedtimeStoriesChannel) ?: return
            if (isActive(channel)) {
                message += "\nBedtime Stories: " + channel.asMention
            }
        }
        if (server.timeWarpChannel.isNotEmpty()) {
            val channel = guild.getTextChannelById(server.timeWarpChannel) ?: return
            if (isActive(channel)) {
                message += "\nTime Warp: " + channel.asMention
            }
        }
        if (server.guessingGameChannel.isNotEmpty()) {
            val channel = guild.getTextChannelById(server.guessingGameChannel) ?: return
            if (isActive(channel)) {
                message += "\nGuessing Game: " + channel.asMention
            }
        }
        if (server.longFormChannel.isNotEmpty()) {
            val channel = guild.getTextChannelById(server.longFormChannel) ?: return
            if (isActive(channel)) {
                message += "\nLong Form: " + channel.asMention
            }
        }
        if (server.bookChannel.isNotEmpty()) {
            val channel = guild.getTextChannelById(server.bookChannel) ?: return
            if (isActive(channel)) {
                message += "\nBook: " + channel.asMention
            }
        }
        if (server.genreChannel.isNotEmpty()) {
            val channel = guild.getTextChannelById(server.genreChannel) ?: return
            if (isActive(channel)) {
                message += "\nGenre: " + channel.asMention
            }
        }
        if (server.alphabetChannel.isNotEmpty()) {
            val channel = guild.getTextChannelById(server.alphabetChannel) ?: return
            if (isActive(channel)) {
                message += "\nAlphabet: " + channel.asMention
            }
        }
        if (server.flurryChannel.isNotEmpty()) {
            val channel = guild.getTextChannelById(server.flurryChannel) ?: return
            if (isActive(channel)) {
                message += "\nFlurry: " + channel.asMention
            }
        }
        if (server.pillarsChannel.isNotEmpty()) {
            val channel = guild.getTextChannelById(server.pillarsChannel) ?: return
            if (isActive(channel)) {
                message += "\nPillars: " + channel.asMention
            }
        }
        if (server.chainChannel.isNotEmpty()) {
            val channel = guild.getTextChannelById(server.chainChannel) ?: return
            if (isActive(channel)) {
                message += "\nChain: " + channel.asMention
            }
        }
        if (server.sevenThingsChannel.isNotEmpty()) {
            val channel = guild.getTextChannelById(server.sevenThingsChannel) ?: return
            if (isActive(channel)) {
                message += "\n7 Things: " + channel.asMention
            }
        }
        if (server.normalTranslatorChannel.isNotEmpty()) {
            val channel = guild.getTextChannelById(server.normalTranslatorChannel) ?: return
            if (isActive(channel)) {
                message += "\nTranslator: " + channel.asMention
            }
        }
        if (server.emojiTranslatorChannel.isNotEmpty()) {
            val channel = guild.getTextChannelById(server.emojiTranslatorChannel) ?: return
            if (isActive(channel)) {
                message += "\nEmoji Translator: " + channel.asMention
            }
        }
        if (server.threeHeadedExpertChannel.isNotEmpty()) {
            val channel = guild.getTextChannelById(server.threeHeadedExpertChannel) ?: return
            if (isActive(channel)) {
                message += "\nThree-headed Expert: " + channel.asMention
            }
        }
        val oldMessage = guild.getTextChannelById(server.currentlyPlayingMessageChannel)!!.retrieveMessageById(server.currentlyPlayingMessageId).complete()
        oldMessage.editMessage(message).queue()
    }

    /**
     * Gets whether a channel is active based on a given timespan
     *
     * @param channel The text channel to check
     * @return true if the channel is active, false if not
     */
    private fun isActive(channel: TextChannel): Boolean {
        val expiredTime = 480000
        val latestMsg = channel.history.retrievePast(1).complete().firstOrNull() ?: return false
        // If the time since the last message exceeds 8 minutes
        return (System.currentTimeMillis() - latestMsg.timeCreated.toInstant().toEpochMilli()) < expiredTime
    }
}