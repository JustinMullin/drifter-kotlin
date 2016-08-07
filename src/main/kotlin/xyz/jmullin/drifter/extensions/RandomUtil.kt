package xyz.jmullin.drifter.extensions

import java.util.Random

/**
 * Convenience methods for getting simple randomized results. Random functions require an implicit
 * [[Random]] instance. This allows for a fixed seed if repeatable random behavior is desirable.
 * A global [[Random]] instance with a nanotime seed is available at RandomUtil.Implicits.global if you prefer
 * not to supply your own.
 */
val r = Random()

fun probability(p: Double) = r.nextDouble() <= p
fun rInt(n: Int) = r.nextInt(n)
fun rInt(n:Int, m:Int) = if(n == m) n else n+r.nextInt(m-n)
fun rFloat(n: Float) = r.nextFloat()*n
fun rFloat(n: Float, m: Float) = n+r.nextFloat()*(m-n)
fun <T> rElement(s: Iterable<T>) = s.ra

fun rV(v: Vector2) = V2(rFloat(v.x)(r), rFloat(v.y)(r))
fun rV(a: Vector2, b: Vector2) = V2(rFloat(a.x, b.x)(r), rFloat(a.y, b.y)(r))
fun rV(r: Rect)(implicit ra: Random): Vector2 = rV(r.v, r.size)(ra)
fun rV(v: V3) = V3(rFloat(v.x)(r), rFloat(v.y)(r), rFloat(v.z)(r))
fun rV(a: V3, b: V3) = V3(rFloat(a.x, b.x)(r), rFloat(a.y, b.y)(r), rFloat(a.z, b.z)(r))
fun rColor(n: Float, m: Float) = C(rFloat(n, m)(r), rFloat(n, m)(r), rFloat(n, m)(r))

fun shuffle[T](seq: Seq[T]) = r.shuffle(seq)