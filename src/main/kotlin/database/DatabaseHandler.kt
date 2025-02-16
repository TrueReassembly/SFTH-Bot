package dev.reassembly.database

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import dev.morphia.Datastore
import dev.morphia.Morphia
import dev.morphia.query.filters.Filters
import dev.reassembly.models.Server
import io.github.cdimascio.dotenv.dotenv
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import org.slf4j.LoggerFactory
import kotlin.reflect.full.memberProperties

lateinit var datastore: Datastore

object DatabaseHandler {

    val logger = LoggerFactory.getLogger(DatabaseHandler::class.java)

    fun init() {
        val dotenv = dotenv()
        try {
            val settings =
                MongoClientSettings.builder().applyConnectionString(ConnectionString(dotenv["MONGO_URI"])).build()
            val client = MongoClients.create(settings)
            datastore = Morphia.createDatastore(client, "improvbot")
            logger.info("Connected to DB")
        } catch (e: Exception) {
           logger.error("Unable to connect to database")
        }

    }

    fun getDatastore(): Datastore {
        return datastore
    }

    fun getServer(guildId: String): Server? {
        return datastore.find(Server::class.java).filter(Filters.eq("id", guildId)).firstOrNull()
    }

    fun getAllServersToUpdate(): List<Server> {
        return datastore.find(Server::class.java).filter(Filters.ne("currentlyPlayingMessageId", "")).iterator().toList()
    }

    /**
     * Checks if there is a game with its channel set to `id`
     * @param id the id of the channel
     *
     * @return true if the channel is set, false otehrwise
     */
    fun channelIsSet(channel: TextChannel): Boolean {
        val server = getServer(channel.guild.id) ?: return false

        for (property in Server::class.memberProperties) {
            if (property.get(server)?.toString() == channel.id) {
                return true
            }
        }
        return false
    }
}