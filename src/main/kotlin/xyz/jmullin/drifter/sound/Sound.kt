package xyz.jmullin.drifter.sound

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound as GdxSound

/**
 * Very simple convenience object used to wrap Sound and Music play methods to allow an "off switch"
 * for sound and music calls.
 */
object Sound {
    var sfxOn = true
    var musicOn = true

    /**
     * Play a sound (if sfx enabled).
     *
     * @param s Sound to play.
     * @param v Volume (0-1) to play the sound at.
     * @return The played Sound.
     */
    fun play(s: GdxSound, v: Float=1f): GdxSound {
        if(sfxOn) s.play(v)
        return s
    }

    /**
     * Start a looping music track.
     *
     * @param m Music to play.
     * @return The played Music.
     */
    fun play(m: Music): Music {
        m.isLooping = true
        m.volume = 1f
        if(musicOn) m.play()
        return m
    }
}
