package xyz.jmullin.drifter.extensions

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

/**
 * Convenience extensions/methods for manipulation of [Rectangle]s.
 */
fun Rect(x: Float, y: Float, width: Float, height: Float) = Rectangle(x, y, width, height)
fun Rect(v: Vector2, size: Vector2) = Rectangle(v.x, v.y, size.x, size.y)
fun Rect(v: Float, size: Float) = Rect(V2(v), V2(size))
fun Rect(size: Vector2) = Rect(V2(0f), size)
fun Rect(size: Float) = Rect(V2(0f), V2(size))

fun Vector2.rect(size: Vector2, align: Vector2 = V2(1f)) = Rect(this + (align - 1f) * size / 2f, size)
fun Vector2.rect(size: Float, align: Vector2 = V2(1f)) = rect(V2(size), align)
fun Vector2.rectCenter(size: Vector2) = Rect(this - size/2f, size)
fun Vector2.rectCenter(size: Float) = Rect(this - size/2f, V2(size))

val Rectangle.position: Vector2 get() = V2(x, y)
val Rectangle.size: Vector2 get() = V2(width, height)
val Rectangle.center: Vector2 get() = V2(x + width/2f, y + height/2f)

fun Rectangle.inset(amount: Float) = Rect(position + amount, size - amount * 2f)
fun Rectangle.inset(amount: Vector2) = Rect(position + amount, size - amount * 2f)

fun Rectangle.expand(amount: Float) = Rect(position - amount, size + amount * 2f)
fun Rectangle.expand(amount: Vector2) = Rect(position - amount, size + amount * 2f)

fun Rectangle.flipX() = Rect(x + width, y, -width, height)
fun Rectangle.flipY() = Rect(x, y + height, width, -height)

val Rectangle.left get() = x
val Rectangle.bottom get() = y
val Rectangle.right get() = x+width
val Rectangle.top get() = y+height

val Rectangle.leftEdge get() = V2(x, y) to V2(x, y+height)
val Rectangle.bottomEdge get() = V2(x, y) to V2(x+width, y)
val Rectangle.rightEdge get() = V2(x+width, y) to V2(x+width, y+height)
val Rectangle.topEdge get() = V2(x, y+height) to V2(x+width, y+height)

val Rectangle.rightTop get() = V2(x+width, y+height)
val Rectangle.leftTop get() = V2(x, y+height)
val Rectangle.rightBottom get() = V2(x+width, y)
val Rectangle.leftBottom get() = V2(x, y)
