package xyz.jmullin.drifter.assets

import com.badlogic.gdx.audio.Music

/**
 * Used as a placeholder noop implementation of [Music] for [DrifterAssets].
 */
class PlaceholderMusic : Music {
    val PlaceholderMessage = "This is a placeholder asset; this method should never have been called!"

    override fun isPlaying(): Boolean = throw UnsupportedOperationException(PlaceholderMessage)
    override fun isLooping(): Boolean = throw UnsupportedOperationException(PlaceholderMessage)
    override fun setOnCompletionListener(listener: Music.OnCompletionListener?) = throw UnsupportedOperationException(PlaceholderMessage)
    override fun pause() = throw UnsupportedOperationException(PlaceholderMessage)
    override fun setPan(pan: Float, volume: Float) = throw UnsupportedOperationException(PlaceholderMessage)
    override fun getPosition(): Float = throw UnsupportedOperationException(PlaceholderMessage)
    override fun setLooping(isLooping: Boolean) = throw UnsupportedOperationException(PlaceholderMessage)
    override fun getVolume(): Float = throw UnsupportedOperationException(PlaceholderMessage)
    override fun play() = throw UnsupportedOperationException(PlaceholderMessage)
    override fun stop() = throw UnsupportedOperationException(PlaceholderMessage)
    override fun setVolume(volume: Float) = throw UnsupportedOperationException(PlaceholderMessage)
    override fun setPosition(position: Float) = throw UnsupportedOperationException(PlaceholderMessage)
    override fun dispose() = throw UnsupportedOperationException(PlaceholderMessage)
}