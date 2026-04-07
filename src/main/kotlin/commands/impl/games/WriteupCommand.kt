package dev.reassembly.commands.impl.games

import dev.reassembly.commands.BaseCommand
import dev.reassembly.database.DatabaseHandler
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal

object WriteupCommand: BaseCommand("writeup") {
    override suspend fun execute(event: SlashCommandInteractionEvent) {

        if (!DatabaseHandler.channelIsSet(event.channel.asTextChannel())) {
            event.reply("This channel does not support writeups").setEphemeral(true).queue()
            return
        }

        val title = TextInput.create("title", "Episode Title", TextInputStyle.SHORT)
            .setRequired(true)
            .setPlaceholder("The title of the episode")
            .build()

        val audiencePrompt = TextInput.create("prompt", "Audience Prompt", TextInputStyle.SHORT)
            .setRequired(true)
            .setPlaceholder("The prompt / topic the audience gave")
            .build()

        val synopsis = TextInput.create("synopsis", "Summary", TextInputStyle.PARAGRAPH)
            .setRequired(false)
            .setPlaceholder("A summary of the story told (Optional)")
            .build()

        val modal = Modal.create("writeupmodal", "Write Up Configuarion")
            .addActionRow(title, audiencePrompt, synopsis)

        event.replyModal(modal.build())
    }
}