package xyz.jmullin.drifter.assets

import com.badlogic.gdx.audio.Sound

/**
 * Used as a placeholder noop implementation of [Sound] for [DrifterAssets].
 */
class PlaceholderSound : Sound {
    val PlaceholderMessage = "This is a placeholder asset; this method should never have been called!"

    override fun pause() = throw UnsupportedOperationException(PlaceholderMessage)
    override fun pause(soundId: Long) = throw UnsupportedOperationException(PlaceholderMessage)
    override fun setPitch(soundId: Long, pitch: Float) = throw UnsupportedOperationException(PlaceholderMessage)
    override fun setPan(soundId: Long, pan: Float, volume: Float) = throw UnsupportedOperationException(PlaceholderMessage)
    override fun setLooping(soundId: Long, looping: Boolean) = throw UnsupportedOperationException(PlaceholderMessage)
    override fun play(): Long = throw UnsupportedOperationException(PlaceholderMessage)
    override fun play(volume: Float): Long = throw UnsupportedOperationException(PlaceholderMessage)
    override fun play(volume: Float, pitch: Float, pan: Float): Long = throw UnsupportedOperationException(PlaceholderMessage)
    override fun stop() = throw UnsupportedOperationException(PlaceholderMessage)
    override fun stop(soundId: Long) = throw UnsupportedOperationException(PlaceholderMessage)
    override fun setVolume(soundId: Long, volume: Float) = throw UnsupportedOperationException(PlaceholderMessage)
    override fun resume() = throw UnsupportedOperationException(PlaceholderMessage)
    override fun resume(soundId: Long) = throw UnsupportedOperationException(PlaceholderMessage)
    override fun loop(): Long = throw UnsupportedOperationException(PlaceholderMessage)
    override fun loop(volume: Float): Long = throw UnsupportedOperationException(PlaceholderMessage)
    override fun loop(volume: Float, pitch: Float, pan: Float): Long = throw UnsupportedOperationException(PlaceholderMessage)
    override fun dispose() = throw UnsupportedOperationException(PlaceholderMessage)
}