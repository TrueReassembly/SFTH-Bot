package dev.reassembly.commands

import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter


object CommandHandler: ListenerAdapter() {

    private val registeredCommands: MutableMap<String, BaseCommand> = mutableMapOf() // Name: Command

    fun registerCommands(vararg commands: BaseCommand) {
        for (command in commands) {
            registeredCommands[command.name] = command
        }
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) = runBlocking {
        registeredCommands[event.name]?.execute(event) ?: event.reply("Couldn't find the command. Contact the bot developer").setEphemeral(true).queue()
    }
}