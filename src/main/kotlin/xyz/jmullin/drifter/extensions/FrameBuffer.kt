
package xyz.jmullin.drifter.extensions

import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.Vector2
import xyz.jmullin.drifter.gdx.FloatFrameBuffer
import xyz.jmullin.drifter.gdx.MultiTargetFrameBuffer

/**
 * Convenience extensions/methods for [FrameBuffer]s.
 */
val FrameBuffer.size: Vector2 get() = V2(width, height)

/**
 * Convenience extensions/methods for [FrameBuffer]s.
 */
val FloatFrameBuffer.size: Vector2 get() = V2(width, height)

/**
 * Convenience extensions/methods for [FrameBuffer]s.
 */
val MultiTargetFrameBuffer.size: Vector2 get() = V2(width, height)