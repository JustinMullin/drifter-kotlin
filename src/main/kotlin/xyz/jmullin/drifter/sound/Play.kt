package xyz.jmullin.drifter.sound

import com.badlogic.gdx.audio.Music
import xyz.jmullin.drifter.extensions.random
import com.badlogic.gdx.audio.Sound as GdxSound

/**
 * Very simple convenience object used to wrap Sound and Music play methods to allow an "off switch"
 * for sound and music calls.
 */
object Play {
    var soundOn = true
    var musicOn = true
    var multiplier = 0.6f

    /**
     * Play a sound (if sound effects are enabled).
     *
     * @param sound Sound to play.
     * @param volume Volume (0-1) to play the sound at.
     * @return The ID of the played Sound.
     */
    fun sound(sound: GdxSound, volume: Float=1f, pitch: Float=1f): Long {
        if(soundOn) return sound.play(volume * multiplier).apply { if(pitch != 1f) sound.setPitch(this, pitch) }
        return -1
    }

    /**
     * Play a sound (if sound effects are enabled).
     *
     * @param sound Sound to play.
     * @param volume Random volume range (0.0-1.0) to play the sound at.
     * @return The ID of the played Sound.
     */
    fun sound(sound: GdxSound, volume: ClosedRange<Float>): Long {
        return sound(sound, volume.random())
    }

    /**
     * Play a sound (if sound effects are enabled).
     *
     * @param sound Sound to play.
     * @param volume Random volume range (0.0-1.0) to play the sound at.
     * @param pitch Random relative pitch range (0.5-2.0) to play the sound at.
     * @return The ID of the played Sound.
     */
    fun sound(sound: GdxSound, volume: ClosedRange<Float>, pitch: ClosedRange<Float>): Long {
        return sound(sound, volume.random(), pitch.random())
    }

    /**
     * Start a looping music track (if music is enabled).
     *
     * @param music Music to play.
     * @return The played Music.
     */
    fun music(music: Music, volume: Float=1f): Music {
        music.isLooping = true
        music.volume = volume
        if(musicOn) music.play()
        return music
    }
}
