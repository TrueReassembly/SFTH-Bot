package dev.reassembly.handlers

import dev.reassembly.SFTHBot
import dev.reassembly.database.DatabaseHandler
import dev.reassembly.models.Server
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object JoinHandler : ListenerAdapter() {

    override fun onGuildJoin(event: GuildJoinEvent) {
        if (DatabaseHandler.getServer(event.guild.id) == null) {
            Server(event.guild.id).save()
        }
    }
}