package xyz.jmullin.drifter.extensions

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

/**
 * Convenience extensions/methods for manipulation of [[Rectangle]]s.
 */
fun Rect(x: Float, y: Float, width: Float, height: Float) = Rectangle(x, y, width, height)
fun Rect(v: Vector2, size: Vector2) = Rectangle(v.x, v.y, size.x, size.y)

fun Rectangle.toV2() = V2(x, y)
val Rectangle.size: Vector2 get() = V2(width, height)