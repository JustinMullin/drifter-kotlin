package xyz.jmullin.drifter.extensions

import com.badlogic.gdx.math.Matrix3
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import xyz.jmullin.drifter.extensions.FloatMath.abs as mAbs
import xyz.jmullin.drifter.extensions.FloatMath.min as mMin
import xyz.jmullin.drifter.extensions.FloatMath.max as mMax
import xyz.jmullin.drifter.extensions.FloatMath.ceil as mCeil
import xyz.jmullin.drifter.extensions.FloatMath.floor as mFloor
import xyz.jmullin.drifter.extensions.FloatMath.round as mRound
import xyz.jmullin.drifter.VectorHex

/**
 * Convenience extensions/methods for manipulation of [[Vector2]]s.
 */

fun V2(xy: Float) = Vector2(xy, xy)
fun V2(x: Float, y: Float) = Vector2(x, y)
fun V2(x: Int, y: Int) = Vector2(x*1f, y*1f)

operator fun Vector2.plus(o: Vector2) = cpy().add(o)
operator fun Vector2.plus(n: Float) = cpy().add(n, n)
operator fun Vector2.minus(o: Vector2) = cpy().sub(o)
operator fun Vector2.minus(n: Float) = cpy().sub(n, n)
operator fun Vector2.times(o: Vector2) = cpy().scl(o)
operator fun Vector2.times(n: Float) = cpy().scl(n, n)
operator fun Vector2.times(m: Matrix3) = cpy().mul(m)
operator fun Vector2.div(o: Vector2) = cpy().scl(1f/o.x, 1f/o.y)
operator fun Vector2.div(n: Float) = cpy().scl(1f/n, 1f/n)

fun Vector2.abs() = V2(mAbs(x), mAbs(y))
fun Vector2.inverse() = (this * -1f).fixZeroes()
fun Vector2.flipX() = (this * V2(-1, 1)).fixZeroes()
fun Vector2.flipY() = (this * V2(1, -1)).fixZeroes()

fun Vector2.center(o: Vector2) = this + o/2f
fun Vector2.midpoint(o: Vector2) = V2((x-o.x)*0.5f, (y-o.y)*0.5f)

fun Vector2.min(o: Vector2) = V2(mMin(x, x), mMin(y, y))
fun Vector2.max(o: Vector2) = V2(mMax(x, x), mMax(y, y))
fun Vector2.floor() = V2(mFloor(x), mFloor(y)).fixZeroes()
fun Vector2.ceil() = V2(mCeil(x), mCeil(y)).fixZeroes()
fun Vector2.round() = V2(mRound(x), mRound(y)).fixZeroes()

fun Vector2.snap(scale: Float=1f) = V2(mFloor(x/scale), mFloor(y/scale))

fun Vector2.neighbors() = (-1..1).flatMap { i -> (-1..1).map { j -> V2(i, j) } }.filter { !it.isZero }.map { this + it }
fun Vector2.orthogonal() = (-1..1).flatMap { i -> (-1..1).map { j -> V2(i, j) } }.filter { it.len() == 1f }.map { this + it }

/**
 * Gets rid of negative zero situations for reasons of equality. This is almost certainly bad 'cause
 * we shouldn't be naively comparing floats anyway...
 *
 * @return The vector with negative zero components replaced with positive zero
 */
fun Vector2.fixZeroes() = V2(if(x == 0.0f) 0.0f else x, if(y == 0.0f) 0.0f else y)

val Vector2.minComponent: Float get() = mMin(mAbs(x), mAbs(y))
val Vector2.maxComponent: Float get() = mMax(mAbs(x), mAbs(y))

fun Vector2.manhattanTo(b: Vector2) = {
    val difference = (b-this).abs()
    difference.x + difference.y
}

fun Vector2.toHex(size: Vector2) = VectorHex((x * (3f.sqrt())/3 - y / 3) / size.x, y * 2/3 / size.y).snap()

// SWIZZLING

val Vector2.xx: Vector2 get() = V2(x, x)
val Vector2.yy: Vector2 get() = V2(y, y)

val Vector2.xxx: Vector3 get() = V3(x, x, x)
val Vector2.xxy: Vector3 get() = V3(x, x, y)
val Vector2.xxo: Vector3 get() = V3(x, x, 0f)
val Vector2.xyx: Vector3 get() = V3(x, y, x)
val Vector2.xyy: Vector3 get() = V3(x, y, y)
val Vector2.xyo: Vector3 get() = V3(x, y, 0f)
val Vector2.xox: Vector3 get() = V3(x, 0f, x)
val Vector2.xoy: Vector3 get() = V3(x, 0f, y)
val Vector2.xoo: Vector3 get() = V3(x, 0f, 0f)
val Vector2.yxx: Vector3 get() = V3(y, x, x)
val Vector2.yxy: Vector3 get() = V3(y, x, y)
val Vector2.yxo: Vector3 get() = V3(y, x, 0f)
val Vector2.yyx: Vector3 get() = V3(y, y, x)
val Vector2.yyy: Vector3 get() = V3(y, y, y)
val Vector2.yyo: Vector3 get() = V3(y, y, 0f)
val Vector2.yox: Vector3 get() = V3(y, 0f, x)
val Vector2.yoy: Vector3 get() = V3(y, 0f, y)
val Vector2.yoo: Vector3 get() = V3(y, 0f, 0f)
val Vector2.oxx: Vector3 get() = V3(0f, x, x)
val Vector2.oxy: Vector3 get() = V3(0f, x, y)
val Vector2.oxo: Vector3 get() = V3(0f, x, 0f)
val Vector2.oyx: Vector3 get() = V3(0f, y, x)
val Vector2.oyy: Vector3 get() = V3(0f, y, y)
val Vector2.oyo: Vector3 get() = V3(0f, y, 0f)
val Vector2.oox: Vector3 get() = V3(0f, 0f, x)
val Vector2.ooy: Vector3 get() = V3(0f, 0f, y)