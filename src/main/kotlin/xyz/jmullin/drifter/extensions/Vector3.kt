package xyz.jmullin.drifter.extensions

import com.badlogic.gdx.math.Matrix3
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import xyz.jmullin.drifter.extensions.FloatMath.abs as mAbs

/**
 * Convenience extensions/methods for manipulation of [[Vector3]]s.
 */

fun V3(xyz: Float) = Vector3(xyz, xyz, xyz)
fun V3(v: Vector2, z: Float) = Vector3(v.x, v.y, z)
fun V3(x: Float, v: Vector2) = Vector3(x, v.x, v.y)
fun V3(x: Float, y: Float, z: Float) = Vector3(x, y, z)

operator fun Vector3.plus(o: Vector3) = cpy().add(o)
operator fun Vector3.plus(n: Float) = cpy().add(n, n, n)
operator fun Vector3.minus(o: Vector3) = cpy().sub(o)
operator fun Vector3.minus(n: Float) = cpy().sub(n, n, n)
operator fun Vector3.times(o: Vector3) = cpy().scl(o)
operator fun Vector3.times(n: Float) = cpy().scl(n, n, n)
operator fun Vector3.times(m: Matrix3) = cpy().mul(m)
operator fun Vector3.times(m: Matrix4) = cpy().mul(m)
operator fun Vector3.div(o: Vector3) = cpy().scl(1f/o.x, 1f/o.y, 1f/o.z)
operator fun Vector3.div(n: Float) = cpy().scl(1f/n, 1f/n, 1f/n)

fun Vector3.abs() = V3(mAbs(x), mAbs(y), mAbs(z))
fun Vector3.inverse() = (this * -1f).fixZeroes()
fun Vector3.fixZeroes() = V3(if(x == 0.0f) 0.0f else x, if(y == 0.0f) 0.0f else y, if(z == 0.0f) 0.0f else z)

fun Vector3.center(o: Vector3) = this + o/2f
fun Vector3.midpoint(o: Vector3) = V3((x-o.x)*0.5f, (y-o.y)*0.5f, (z-o.z)*0.5f)

fun Vector3.manhattanTo(b: Vector3) = {
    val difference = (b-this).abs()
    difference.x + difference.y + difference.z
}

// SWIZZLING

val Vector3.xxx: Vector3 get() = V3(x, x, x)
val Vector3.xxy: Vector3 get() = V3(x, x, y)
val Vector3.xxz: Vector3 get() = V3(x, x, z)
val Vector3.xyx: Vector3 get() = V3(x, y, x)
val Vector3.xyy: Vector3 get() = V3(x, y, y)
val Vector3.xyz: Vector3 get() = V3(x, y, z)
val Vector3.xzx: Vector3 get() = V3(x, z, x)
val Vector3.xzy: Vector3 get() = V3(x, z, y)
val Vector3.xzz: Vector3 get() = V3(x, z, z)
val Vector3.yxx: Vector3 get() = V3(y, x, x)
val Vector3.yxy: Vector3 get() = V3(y, x, y)
val Vector3.yxz: Vector3 get() = V3(y, x, z)
val Vector3.yyx: Vector3 get() = V3(y, y, x)
val Vector3.yyy: Vector3 get() = V3(y, y, y)
val Vector3.yyz: Vector3 get() = V3(y, y, z)
val Vector3.yzx: Vector3 get() = V3(y, z, x)
val Vector3.yzy: Vector3 get() = V3(y, z, y)
val Vector3.yzz: Vector3 get() = V3(y, z, z)
val Vector3.zxx: Vector3 get() = V3(z, x, x)
val Vector3.zxy: Vector3 get() = V3(z, x, y)
val Vector3.zxz: Vector3 get() = V3(z, x, z)
val Vector3.zyx: Vector3 get() = V3(z, y, x)
val Vector3.zyy: Vector3 get() = V3(z, y, y)
val Vector3.zyz: Vector3 get() = V3(z, y, z)
val Vector3.zzx: Vector3 get() = V3(z, z, x)
val Vector3.zzy: Vector3 get() = V3(z, z, y)
val Vector3.zzz: Vector3 get() = V3(z, z, z)

val Vector3.xx: Vector2 get() = V2(x, x)
val Vector3.xy: Vector2 get() = V2(x, y)
val Vector3.xz: Vector2 get() = V2(x, z)
val Vector3.yx: Vector2 get() = V2(y, x)
val Vector3.yy: Vector2 get() = V2(y, y)
val Vector3.yz: Vector2 get() = V2(y, z)
val Vector3.zx: Vector2 get() = V2(z, x)
val Vector3.zy: Vector2 get() = V2(z, y)
val Vector3.zz: Vector2 get() = V2(z, z)