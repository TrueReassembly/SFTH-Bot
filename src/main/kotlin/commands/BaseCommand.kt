package dev.reassembly.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

abstract class BaseCommand(val name: String) {

    abstract suspend fun execute(event: SlashCommandInteractionEvent)
}