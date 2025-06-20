package dev.reassembly.commands.impl.config

import dev.reassembly.commands.BaseCommand
import dev.reassembly.database.DatabaseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

object ConfigCommand: BaseCommand("config") {
    override suspend fun execute(event: SlashCommandInteractionEvent) {

        val server = DatabaseHandler.getServer(event.guild!!.id)
        if (server == null) {
            event.reply("This server has not been registered").setEphemeral(true).queue()
            return
        }

        if (!event.member!!.hasPermission(Permission.MANAGE_SERVER)) {
            event.reply("You need the ``Manage Server`` permission to run this command!")
        }

        when (event.subcommandName) {
            "channels" -> {

                val channel = event.getOption("channel")!!.asChannel
                when (event.getOption("game")!!.asString) {

                    "change" -> {
                        server.changeChannel = channel.id
                        event.reply("Set Change channel to " + channel.asMention).queue()
                    }
                    "letter" -> {
                        server.letterChannel = channel.id
                        event.reply("Set Letter channel to " + channel.asMention).queue()
                        if (channel is TextChannel || channel is ThreadChannel) {
                            channel.sendMessage("Start your letter here:").queue()
                        }
                    }
                    "pattern" -> {
                        server.patternChannel = channel.id
                        event.reply("Set Pattern channel to " + channel.asMention).queue()
                        if (channel is TextChannel || channel is ThreadChannel) {
                            channel.sendMessage("Start pattern game here:").queue()
                        }
                    }
                    "freezetag" -> {
                        server.freezeTagChannel = channel.id
                        event.reply("Set Freeze Tag channel to " + channel.asMention).queue()
                    }
                    "lateforwork" -> {
                        server.lateForWorkChannel = channel.id
                        event.reply("Set Late For Work channel to " + channel.asMention).queue()
                    }
                    "partyquirks" -> {
                        server.partyQuirksChannel = channel.id
                        event.reply("Set Party Quirks channel to " + channel.asMention).queue()
                    }
                    "bedtimestories" -> {
                        server.bedtimeStoriesChannel = channel.id
                        event.reply("Set Bedtime Stories channel to " + channel.asMention).queue()
                    }
                    "timewarp" -> {
                        server.timeWarpChannel = channel.id
                        event.reply("Set Time Warp channel to " + channel.asMention).queue()
                    }
                    "guessinggame" -> {
                        server.guessingGameChannel = channel.id
                        event.reply("Set Guessing Game channel to " + channel.asMention).queue()
                    }
                    "seventhings" -> {
                        server.sevenThingsChannel = channel.id
                        event.reply("Set Seven Things channel to " + channel.asMention).queue()
                    }
                    "nowplaying" -> {
                        server.currentlyPlayingMessageChannel = channel.id
                        if (channel is TextChannel || channel is ThreadChannel) {
                            val nowPlayingMessage = withContext(Dispatchers.IO) {
                                channel.sendMessage("Currently Active Games").complete()
                            }
                            server.currentlyPlayingMessageId = nowPlayingMessage.id
                        }
                        event.reply("Set Now Playing channel to " + channel.asMention).queue()
                    }
                    "book" -> {
                        server.bookChannel = channel.id
                        event.reply("Set Book channel to " + channel.asMention).queue()
                    }
                    "longform" -> {
                        server.longFormChannel = channel.id
                        event.reply("Set Longform channel to " + channel.asMention).queue()
                    }
                    "genre" -> {
                        server.genreChannel = channel.id
                        event.reply("Set Genre channel to " + channel.asMention).queue()
                    }
                    "alphabet" -> {
                        server.alphabetChannel = channel.id
                        event.reply("Set Alphabet channel to " + channel.asMention).queue()
                    }
                    "flurry" -> {
                        server.flurryChannel = channel.id
                        event.reply("Set Flurry channel to " + channel.asMention).queue()
                    }
                    "pillars" -> {
                        server.pillarsChannel = channel.id
                        event.reply("Set Pillars channel to " + channel.asMention).queue()
                    }
                    "chain" -> {
                        server.chainChannel = channel.id
                        event.reply("Set Chain channel to " + channel.asMention).queue()
                        if (channel is TextChannel || channel is ThreadChannel) {
                            channel.sendMessage("Start chain game here:").queue()
                        }
                    }
                    else -> {
                        event.reply("Could not find game").setEphemeral(true).queue()
                    }
                }
                server.save()
            }
        }
    }
}