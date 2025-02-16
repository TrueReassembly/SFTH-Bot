package dev.reassembly.commands.impl.config

import dev.reassembly.commands.BaseCommand
import dev.reassembly.models.Server
import dev.reassembly.utils.Constants
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

object RegisterCommand: BaseCommand("register") {
    override suspend fun execute(event: SlashCommandInteractionEvent) {

        if (event.user.id != Constants.OWNER_ID) {
            event.reply("Only the bot owner can do this!").setEphemeral(true).queue()
            return
        }
        event.deferReply()
        Server(event.guild!!.id).save()
        event.reply("Registered the server successfully").queue()
    }
}