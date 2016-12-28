
package xyz.jmullin.drifter.extensions

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.Vector2

/**
 * Convenience extensions/methods for [Pixmap]s.
 */
val Pixmap.size: Vector2 get() = V2(width, height)