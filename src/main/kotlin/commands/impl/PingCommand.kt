package dev.reassembly.commands.impl

import dev.reassembly.commands.BaseCommand
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

object PingCommand: BaseCommand("ping") {
    override suspend fun execute(event: SlashCommandInteractionEvent) {
        event.reply("Pong!").queue()
    }

}