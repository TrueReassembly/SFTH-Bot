package dev.reassembly.commands.impl.games

import dev.reassembly.SFTHBot
import dev.reassembly.commands.BaseCommand
import dev.reassembly.database.DatabaseHandler
import dev.reassembly.handlers.GPTHandler
import dev.reassembly.utils.Constants
import dev.reassembly.utils.MessageUtils
import io.ktor.events.Events
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

object PromptCommand: BaseCommand("prompt") {
    override suspend fun execute(event: SlashCommandInteractionEvent) {
        var list = arrayOf("")

        list = when(event.getOption("type")!!.asString) {
            "tv_genre" -> Constants.TV_GENRES
            "film_genre" -> Constants.FILM_GENRES
            "theatre_genre" -> Constants.THEATRE_GENRES
            "relationships" -> Constants.RELATIONSHIPS
            "locations" -> Constants.LOCATIONS
            "objects" -> Constants.OBJECTS
            else -> {
                event.reply("This is not a valid prompt type. Please use one of the dropdown options.").setEphemeral(true).queue()
                return
            }
        }

        event.reply(list.random() + "!").queue()
    }
}