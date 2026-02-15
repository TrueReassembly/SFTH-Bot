package dev.reassembly.commands.impl.letters

import dev.reassembly.SFTHBot
import dev.reassembly.commands.BaseCommand
import dev.reassembly.database.DatabaseHandler
import dev.reassembly.handlers.GPTHandler
import dev.reassembly.utils.MessageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

object EndLetterCommand: BaseCommand("endletter") {
    override suspend fun execute(event: SlashCommandInteractionEvent) {
        val hook = withContext(Dispatchers.IO) {
            event.deferReply().complete()
        }

        val channelId = DatabaseHandler.getServer(event.guild!!.id)?.letterChannel ?: run {
            hook.sendMessage("Letters is not set up for this server").setEphemeral(true).queue()
            return
        }
        val channel = SFTHBot.getInstance().getTextChannelById(channelId) ?: run {
            hook.sendMessage("There was a problem with finding the channel, try setting the letters channel in the /config command again").setEphemeral(true).queue()
            return
        }
        if (event.channel.id != channelId) {
            hook.sendMessage("You can only run this command in " + SFTHBot.getInstance().getTextChannelById(channelId)!!.asMention).setEphemeral(true).queue()
            return
        }

        val rawLetter = MessageUtils.getLatestMessagesAsString(channel, true)
        if (rawLetter.isEmpty()) {
            hook.sendMessage("There are no words to submit in the letter!").setEphemeral(true).queue()
            return
        }

        val formattedLetter = GPTHandler.formatLetter(rawLetter) ?: run {
            hook.sendMessage("There was an error when trying to format the letter. Is GPT down?").setEphemeral(true).queue()
            return
        }

        MessageUtils.safeSendMessage(formattedLetter, channel)
        hook.sendMessage("Formatted Letter.").queue()
        event.channel.asTextChannel().sendMessage("You may start a new letter:").queue()
    }
}