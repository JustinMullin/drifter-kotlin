package xyz.jmullin.drifter.extensions

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2

/**
 * Convenience extensions/methods for [Texture]s.
 */
val Texture.size: Vector2 get() = V2(width, height)