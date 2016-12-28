package xyz.jmullin.drifter.extensions

import com.badlogic.gdx.graphics.Color

/**
 * Convenience extensions/methods for manipulation of [[Color]]s.
 */

fun C(r: Float, g: Float, b: Float) = Color(r, g, b, 1f)
fun C(r: Float, g: Float, b: Float, a: Float) = Color(r, g, b, a)
fun C(c: Color, a: Float) = Color(c.r, c.g, c.b, a)
fun C(i: Float) = Color(i, i, i, 1f)

operator fun Color.plus(o: Color): Color = cpy().add(o.alpha(0f))
operator fun Color.plus(n: Float): Color = cpy().add(n, n, n, 0f)
operator fun Color.minus(o: Color): Color = cpy().sub(o.alpha(0f))
operator fun Color.minus(n: Float): Color = cpy().sub(n, n, n, 0f)
operator fun Color.times(o: Color): Color = cpy().mul(o.alpha(1f))
operator fun Color.times(n: Float): Color = cpy().mul(n, n, n, 1f)
operator fun Color.div(o: Color): Color = cpy().mul(1f/o.r, 1f/o.g, 1f/o.b, 1f)
operator fun Color.div(n: Float): Color = cpy().mul(1f/n, 1f/n, 1f/n, 1f)

fun Color.alpha(a: Float): Color = cpy().set(r, g, b, a)