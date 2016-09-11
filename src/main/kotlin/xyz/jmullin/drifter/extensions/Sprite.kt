package xyz.jmullin.drifter.extensions

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2

/**
 * Convenience extensions/methods for [Sprite]s.
 */
val Sprite.size: Vector2 get() = V2(width, height)