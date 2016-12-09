
package xyz.jmullin.drifter.extensions

import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.Vector2

/**
 * Convenience extensions/methods for [FrameBuffer]s.
 */
val FrameBuffer.size: Vector2 get() = V2(width, height)