package xyz.jmullin.drifter.extensions

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2

/**
 * Convenience extensions/methods for [Sprite]s.
 */
val TextureRegion.position: Vector2 get() = V2(regionX, regionY)
val TextureRegion.size: Vector2 get() = V2(regionWidth, regionHeight)