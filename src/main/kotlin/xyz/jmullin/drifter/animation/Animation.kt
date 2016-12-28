package xyz.jmullin.drifter.animation

import com.badlogic.gdx.graphics.g2d.Sprite
import xyz.jmullin.drifter.rendering.Draw

/**
 * Convenience collection of a set of sprites for use in sequential animation.
 */
data class Animation(val sprites: List<Sprite>) {
    /**
     * Retrieves the proper sprite by alpha representing progress through the animation.
     *
     * @param a Alpha value 0-1 to be used in picking a sprite from the sequence.
     * @return The active sprite lerped from the specified alpha and sprite sequence.
     */
    fun frame(a: Float): Sprite {
        return sprites.getOrNull(Math.min(sprites.size-1f, a*sprites.size).toInt() % sprites.size) ?: sprites.last()
    }
}
