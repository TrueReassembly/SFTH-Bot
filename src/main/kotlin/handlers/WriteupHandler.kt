package dev.reassembly.handlers

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object WriteupHandler: ListenerAdapter() {

    override fun onModalInteraction(event: ModalInteractionEvent) {
        if (event.interaction.modalId == "writeupmodal") {
            val channel = event.channel.asTextChannel();
            val title = event.interaction.getValue("title")!!.asString
            val audiencePrompt = event.interaction.getValue("audienceprompt")!!.asString
            // val synopsis =
        }
    }
}