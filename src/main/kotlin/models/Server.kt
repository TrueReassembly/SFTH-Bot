package dev.reassembly.models

import dev.morphia.annotations.Entity
import dev.morphia.annotations.Id
import dev.reassembly.database.DatabaseHandler

@Entity("servers")
data class Server(@Id val id: String) {

    // Dummy constructor so morphia doesn't cry
    constructor(): this("")

    // TODO, change these into pairs with one representing the normal channels and the other representing the HoF channel.

    // General
    var currentlyPlayingMessageId: String = ""
    var currentlyPlayingMessageChannel: String = ""

    // Change
    var changeChannel: String = ""
    var changeHofChannel: String = ""

    // One Word Letter
    var letterChannel: String = ""
    var letterHofChannel: String = ""

    // Pattern
    var patternChannel: String = ""
    var patternHofChannel: String = ""

    // Freeze Tag
    var freezeTagChannel: String = ""
    var freezeTagHofChannel: String = ""

    // Late for work
    var lateForWorkChannel: String = ""
    var lateForWorkHofChannel: String = ""

    // Party Quirks
    var partyQuirksChannel: String = ""
    var partyQuirksHofChannel: String = ""

    // Bedtime Stories
    var bedtimeStoriesChannel: String = ""
    var bedtimeStoriesHofChannel: String = ""

    // Time warp
    var timeWarpChannel: String = ""
    var timeWarpHofChannel: String = ""

    // Guessing Game
    var guessingGameChannel: String = ""
    var guessingGameHofChannel: String = ""

    // 7 Things
    var sevenThingsChannel: String = ""
    var sevenThingsHofChannel: String = ""

    // Long Form
    var longFormChannel: String = ""
    var longFormHofChannel: String = ""

    // Book
    var bookChannel: String = ""

    // Genre
    var genreChannel: String = ""

    // Alphabet Game
    var alphabetChannel: String = ""

    // Flurry
    var flurryChannel: String = ""

    // Pillars
    var pillarsChannel: String = ""

    // Translators
    val normalTranslatorChannel: String = ""
    val normalTranslatorHofChannel: String = ""
    val emojiTranslatorChannel: String = ""
    val emojiTanslatorHoFChannel: String = ""

    // Expert
    val threeHeadedExpertChannel: String = ""
    val threeHeadedExpertHofChannel: String = ""

    var chainChannel: String = ""

    fun save() {
        DatabaseHandler.getDatastore().save(this)
    }
}