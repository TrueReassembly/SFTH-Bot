package dev.reassembly

import dev.reassembly.commands.CommandHandler
import dev.reassembly.commands.impl.PingCommand
import dev.reassembly.commands.impl.config.ConfigCommand
import dev.reassembly.commands.impl.config.RegisterCommand
import dev.reassembly.commands.impl.letters.EndLetterCommand
import dev.reassembly.database.DatabaseHandler
import dev.reassembly.handlers.NowPlayingHandler
import dev.reassembly.handlers.PatternHandler
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import net.dv8tion.jda.api.requests.GatewayIntent
import org.slf4j.LoggerFactory

lateinit var instance: JDA

object SFTHBot {

    val logger = LoggerFactory.getLogger(this::class.java)

    fun start() = runBlocking {
        val dotenv = dotenv()

        instance = JDABuilder.create(dotenv["TOKEN"], GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES).addEventListeners(CommandHandler, PatternHandler).build()

        logger.info("Preparing to register commands")
        instance.updateCommands().addCommands(
            Commands.slash("ping", "Ping!").setGuildOnly(true),
            Commands.slash("register", "Used to register a server. Can only be used by the bot developer").setGuildOnly(true),
            Commands.slash("config", "Used to configure a server").setGuildOnly(true)
                .addSubcommands(
                    SubcommandData("channels", "Configuration section for linking games to channels")
                        .addOptions(
                            OptionData(OptionType.STRING, "game", "Select the game")
                                .addChoice("change", "change")
                                .addChoice("letter", "letter")
                                .addChoice("pattern", "pattern")
                                .addChoice("freezetag", "freezetag")
                                .addChoice("lateforwork", "lateforwork")
                                .addChoice("partyquirks", "partyquirks")
                                .addChoice("bedtimestories", "bedtimestories")
                                .addChoice("timewarp", "timewarp")
                                .addChoice("guessinggame", "guessinggame")
                                .addChoice("seventhings", "seventhings")
                                .addChoice("nowplaying", "nowplaying")
                                .addChoice("book", "book")
                                .addChoice("longform", "longform")
                                .addChoice("genre", "genre")
                                .addChoice("alphabet", "alphabet")
                                .addChoice("flurry", "flurry")
                                .addChoice("pillars", "pillars")
                                .addChoice("chain", "chain")
                                .setRequired(true),
                            OptionData(OptionType.CHANNEL, "channel", "The channel where the game will be played").setRequired(true)
                        )
                ),
            Commands.slash("endletter", "Ends the letter, formats it and starts a new one").setGuildOnly(true),
            Commands.slash("writeup", "Collates and writes up the game").setGuildOnly(true)
        ).queue()

        logger.info("Registered Commands")

        CommandHandler.registerCommands(PingCommand, RegisterCommand, ConfigCommand, EndLetterCommand)

        DatabaseHandler.init()

        NowPlayingHandler.runNowPlayingUpdateLoop()
    }

    fun getInstance(): JDA {
        return instance
    }
}