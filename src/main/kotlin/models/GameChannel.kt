package dev.reassembly.models

/**
 * Represents a GameChannel field, only really to be used in the [Server] class.
 *
 * @param nowPlayingName The name to be shown in the 'Now Playing' channel.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class GameChannel(val nowPlayingName: String)
