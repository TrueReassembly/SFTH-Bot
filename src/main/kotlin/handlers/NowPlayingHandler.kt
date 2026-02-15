package dev.reassembly.handlers

import dev.reassembly.SFTHBot
import dev.reassembly.database.DatabaseHandler
import dev.reassembly.database.datastore
import dev.reassembly.models.GameChannel
import dev.reassembly.models.Server
import kotlinx.coroutines.delay
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import org.slf4j.LoggerFactory
import kotlin.math.log
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.jvm.isAccessible

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
        // If you're coming here in the future looking for why this is broken, it's because it's only getting them
        // as text channels, not as thread channels. I notice this now but im so tired i dont wanna fix it.
        val server = DatabaseHandler.getServer(guild.id) ?: return

        if (server.currentlyPlayingMessageChannel.isEmpty() || server.currentlyPlayingMessageId.isEmpty()) return
        for (field in server::class.declaredMemberProperties) {

            val annotations = field.findAnnotations<GameChannel>()
            if (annotations.isEmpty()) continue
            val gameChannelAnnotation = annotations[0]

            val value = field.getter.call(server) as String
            if (value.isEmpty()) continue
            val channel = guild.getChannelById(GuildMessageChannel::class.java, value) ?: run {
                logger.warn("There was no findable channel for {} in the server {} ({})", gameChannelAnnotation.nowPlayingName, guild.name, guild.id)
                continue
            }

            if (isActive(channel)) message += "\n${gameChannelAnnotation.nowPlayingName}: ${channel.asMention}"
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
    private fun isActive(channel: GuildMessageChannel): Boolean {
        val expiredTime = 480000
        val latestMsg = channel.history.retrievePast(1).complete().firstOrNull() ?: return false
        // If the time since the last message exceeds 8 minutes
        return (System.currentTimeMillis() - latestMsg.timeCreated.toInstant().toEpochMilli()) < expiredTime
    }
}