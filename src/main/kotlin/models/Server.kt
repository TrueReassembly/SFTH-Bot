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
    @GameChannel("Change")
    var changeChannel: String = ""

    // One Word Letter
    @GameChannel("One Word Letter")
    var letterChannel: String = ""

    // Pattern
    @GameChannel("Pattern Channel")
    var patternChannel: String = ""

    // Freeze Tag
    @GameChannel("Freeze Tag")
    var freezeTagChannel: String = ""

    // Late for work
    @GameChannel("Late For Work")
    var lateForWorkChannel: String = ""

    // Party Quirks
    @GameChannel("Party Quirks")
    var partyQuirksChannel: String = ""

    // Bedtime Stories
    @GameChannel("Bedtime Stories")
    var bedtimeStoriesChannel: String = ""

    // Time warp
    @GameChannel("Time Warp")
    var timeWarpChannel: String = ""

    // Guessing Game
    @GameChannel("Guessing Game")
    var guessingGameChannel: String = ""

    // 7 Things
    @GameChannel("Seven Things")
    var sevenThingsChannel: String = ""

    // Long Form
    @GameChannel("Long Form")
    var longFormChannel: String = ""

    // Book
    @GameChannel("Book")
    var bookChannel: String = ""

    // Genre
    @GameChannel("Genres")
    var genreChannel: String = ""

    // Alphabet Game
    @GameChannel("Alphabet")
    var alphabetChannel: String = ""

    // Flurry
    @GameChannel("Flurry")
    var flurryChannel: String = ""

    // Pillars
    @GameChannel("Pillars")
    var pillarsChannel: String = ""

    // Translators
    @GameChannel("Translator (Normal)")
    val normalTranslatorChannel: String = ""
    @GameChannel("Translator (Emoji)")
    val emojiTranslatorChannel: String = ""

    // Expert
    @GameChannel("Three-Headed Expert")
    val threeHeadedExpertChannel: String = ""

    // Chain
    @GameChannel("Chain")
    var chainChannel: String = ""

    fun save() {
        DatabaseHandler.getDatastore().save(this)
    }
}