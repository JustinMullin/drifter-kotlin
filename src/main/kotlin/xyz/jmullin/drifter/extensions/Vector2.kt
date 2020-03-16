package xyz.jmullin.drifter.extensions

import com.badlogic.gdx.math.Matrix3
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import xyz.jmullin.drifter.geometry.VectorHex
import kotlin.math.abs
import xyz.jmullin.drifter.extensions.FloatMath.abs as mAbs
import xyz.jmullin.drifter.extensions.FloatMath.ceil as mCeil
import xyz.jmullin.drifter.extensions.FloatMath.floor as mFloor
import xyz.jmullin.drifter.extensions.FloatMath.max as mMax
import xyz.jmullin.drifter.extensions.FloatMath.min as mMin
import xyz.jmullin.drifter.extensions.FloatMath.round as mRound

/**
 * Convenience extensions/methods for manipulation of [[Vector2]]s.
 */

fun V2(xy: Float) = Vector2(xy, xy)
fun V2(x: Number, y: Number) = Vector2(x.toFloat(), y.toFloat())
fun V2(xy: Pair<Number, Number>) = Vector2(xy.first.toFloat(), xy.second.toFloat())

val Vector2.xI: Int get() = x.toInt()
val Vector2.yI: Int get() = y.toInt()

operator fun Vector2.component1() = x
operator fun Vector2.component2() = y

operator fun Vector2.plus(o: Vector2): Vector2 = cpy().add(o)
operator fun Vector2.plus(n: Float): Vector2 = cpy().add(n, n)
operator fun Vector2.minus(o: Vector2): Vector2 = cpy().sub(o)
operator fun Vector2.minus(n: Float): Vector2 = cpy().sub(n, n)
operator fun Vector2.times(o: Vector2): Vector2 = cpy().scl(o)
operator fun Vector2.times(n: Float): Vector2 = cpy().scl(n, n)
operator fun Vector2.times(m: Matrix3): Vector2 = cpy().mul(m)
operator fun Vector2.div(o: Vector2): Vector2 = cpy().scl(1f/o.x, 1f/o.y)
operator fun Vector2.div(n: Float): Vector2 = cpy().scl(1f/n, 1f/n)
operator fun Vector2.unaryMinus() = inverse()

operator fun Vector2.rangeTo(v: Vector2) = (yI..v.yI).flatMap { y -> (xI..v.xI).map { x -> Pair(x, y) } }.map(::V2)
infix fun Vector2.until(v: Vector2) = this.rangeTo(v - V2(1f))

fun Vector2.towards(v: Vector2, amount: Float): Float {
    val distance = distanceTo(v)
    return if(amount <= distance) {
        lerp(v, amount / distance)
        0f
    } else {
        set(v)
        distance - amount
    }
}

val Left = V2(-1f, 0f)
val Right = V2(1f, 0f)
val Up = V2(0f, 1f)
val Down = V2(0f, -1f)

val Vector2.aspectRatio get() = x / y

fun nameDir(v: Vector2) = if(v == Up) "Up" else if(v == Down) "Down" else if(v == Right) "Right" else if(v == Left) "Left" else v.toString()

fun Vector2.abs() = V2(mAbs(x), mAbs(y))
fun Vector2.inverse() = (this * -1f).fixZeroes()
fun Vector2.flipX() = (this * V2(-1, 1)).fixZeroes()
fun Vector2.flipY() = (this * V2(1, -1)).fixZeroes()

fun Vector2.center(o: Vector2) = this + o/2f
fun Vector2.midpoint(o: Vector2) = V2((x+o.x)*0.5f, (y+o.y)*0.5f)

fun Vector2.min(o: Vector2) = V2(mMin(x, o.x), mMin(y, o.y))
fun Vector2.max(o: Vector2) = V2(mMax(x, o.x), mMax(y, o.y))
fun Vector2.floor() = V2(mFloor(x), mFloor(y)).fixZeroes()
fun Vector2.ceil() = V2(mCeil(x), mCeil(y)).fixZeroes()
fun Vector2.round() = V2(mRound(x), mRound(y)).fixZeroes()

fun Vector2.neighbors() = (V2(-1, -1)..V2(1, 1)).filter { !it.isZero }.map { this + it }
fun Vector2.orthogonal() = (V2(-1, -1)..V2(1, 1)).filter { it.len() == 1f }.map { this + it }
fun Vector2.diagonal() = (V2(-1, -1)..V2(1, 1)).filter { (abs(it.x) + abs(it.y) == 2f) }.map { this + it }

fun Vector2.snap(scale: Float=1f) = V2(mFloor(x/scale), mFloor(y/scale)) * scale
fun Vector2.snap(scale: Vector2) = (this / scale).floor() * scale

fun Vector2.toGrid(scale: Float=1f) = V2(mFloor(x/scale), mFloor(y/scale))
fun Vector2.toGrid(scale: Vector2) = (this / scale).floor()

/**
 * Gets rid of negative zero situations for reasons of equality. This is almost certainly bad 'cause
 * we shouldn't be naively comparing floats anyway...
 *
 * @return The vector with negative zero components replaced with positive zero
 */
fun Vector2.fixZeroes() = V2(if(x == 0.0f) 0.0f else x, if(y == 0.0f) 0.0f else y)

val Vector2.minComponent: Float get() = mMin(mAbs(x), mAbs(y))
val Vector2.maxComponent: Float get() = mMax(mAbs(x), mAbs(y))

fun Vector2.distanceTo(b: Vector2) = (b - this).len()

fun Vector2.manhattanTo(b: Vector2): Float {
    val difference = (b-this).abs()
    return difference.x + difference.y
}

fun Vector2.toHex(size: Vector2) = (VectorHex(x * (2/3f) / size.x, (-x / 3f + 3f.sqrt()/3f * y) / size.y)).snap()

// SWIZZLING

fun Vector2.list() = listOf(x, y)

val Vector2.xx: Vector2 get() = V2(x, x)
val Vector2.yy: Vector2 get() = V2(y, y)
val Vector2.xo: Vector2 get() = V2(x, 0)
val Vector2.yo: Vector2 get() = V2(y, 0)
val Vector2.ox: Vector2 get() = V2(0, x)
val Vector2.oy: Vector2 get() = V2(0, y)

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
