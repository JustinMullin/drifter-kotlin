package xyz.jmullin.drifter.extensions

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import java.util.*

/**
 * Convenience methods for getting simple randomized results. Random functions require an implicit
 * [[Random]] instance. This allows for a fixed seed if repeatable random behavior is desirable.
 * A global [[Random]] instance with a nanotime seed is available at RandomUtil.Implicits.global if you prefer
 * not to supply your own.
 */
var r = Random()

fun <T> p(p: Number, a: T): T? = if (probability(p.toDouble())) a else null
fun <T> p(p: Number, a: () -> T): T? = if (probability(p.toDouble())) a() else null
fun <T> p(p: Number, a: () -> T, b: () -> T): T = if (probability(p.toDouble())) a() else b()
fun <T> p(p: Number, a: T, b: () -> T): T = if (probability(p.toDouble())) a else b()
fun <T> p(p: Number, a: () -> T, b: T): T = if (probability(p.toDouble())) a() else b
fun <T> p(p: Number, a: T, b: T): T = if (probability(p.toDouble())) a else b
fun probability(p: Double) = r.nextDouble() <= p
fun probability(p: Float) = r.nextFloat() <= p
fun rInt(n: Int) = r.nextInt(n)
fun rInt(n:Int, m:Int) = if(n == m) n else n+r.nextInt(m-n)
fun rInt(r: ClosedRange<Int>) = rInt(r.start, r.endInclusive)
fun rFloat(n: Float) = r.nextFloat()*n
fun rFloat(n: Float, m: Float): Float = n+r.nextFloat()*(m-n)
fun rFloat(r: ClosedRange<Float>) = rFloat(r.start, r.endInclusive)

fun <T> rElement(s: Iterable<T>) = s.toList().let { it[rInt(it.size)] }
fun <T> rElement(vararg s: T) = s.toList().let { it[rInt(it.size)] }
fun <T> Iterable<T>.shuffle(): List<T> {
    val list = this.toMutableList()
    Collections.shuffle(list, r)
    return list
}

fun rV(v: Vector2) = V2(rFloat(v.x), rFloat(v.y))
fun rV(a: Vector2, b: Vector2) = V2(rFloat(a.x, b.x), rFloat(a.y, b.y))
fun rV(r: Rectangle): Vector2 = rV(r.position, r.size)
fun rV(v: Vector3) = V3(rFloat(v.x), rFloat(v.y), rFloat(v.z))
fun rV(a: Vector3, b: Vector3) = V3(rFloat(a.x, b.x), rFloat(a.y, b.y), rFloat(a.z, b.z))
fun rColor(n: Float, m: Float) = C(rFloat(n, m), rFloat(n, m), rFloat(n, m))

fun ClosedRange<Float>.random() = rFloat(start, endInclusive)
fun ClosedRange<Int>.random() = rInt(start, endInclusive)
