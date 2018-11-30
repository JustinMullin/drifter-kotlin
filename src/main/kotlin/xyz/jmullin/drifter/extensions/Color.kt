package xyz.jmullin.drifter.extensions

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.BufferUtils
import java.nio.FloatBuffer

/**
 * Convenience extensions/methods for manipulation of [[Color]]s.
 */

fun C(r: Float, g: Float, b: Float) = Color(r, g, b, 1f)
fun C(r: Float, g: Float, b: Float, a: Float) = Color(r, g, b, a)
fun C(c: Color, a: Float) = Color(c.r, c.g, c.b, a)
fun C(i: Float) = Color(i, i, i, 1f)

fun Ch(h: Float, s: Float, v: Float): Color {
    val x = (h / 60f + 6) % 6
    val i = x.toInt()
    val f = x - i
    val p = v * (1 - s)
    val q = v * (1 - s * f)
    val t = v * (1 - s * (1 - f))
    return when (i) {
        0 -> C(v, t, p)
        1 -> C(q, v, p)
        2 -> C(p, v, t)
        3 -> C(p, q, v)
        4 -> C(t, p, v)
        else -> C(v, p, q)
    }.clamp()
}

operator fun Color.plus(o: Color): Color = cpy().add(o.alpha(0f))
operator fun Color.plus(n: Float): Color = cpy().add(n, n, n, 0f)
operator fun Color.minus(o: Color): Color = cpy().sub(o.alpha(0f))
operator fun Color.minus(n: Float): Color = cpy().sub(n, n, n, 0f)
operator fun Color.times(o: Color): Color = cpy().mul(o.alpha(1f))
operator fun Color.times(n: Float): Color = cpy().mul(n, n, n, 1f)
operator fun Color.div(o: Color): Color = cpy().mul(1f/o.r, 1f/o.g, 1f/o.b, 1f)
operator fun Color.div(n: Float): Color = cpy().mul(1f/n, 1f/n, 1f/n, 1f)

fun Color.alpha(a: Float): Color = cpy().set(r, g, b, a)

fun Color.toBuffer(): FloatBuffer {
    val buffer = BufferUtils.newFloatBuffer(4)
    listOf(r, g, b, a).forEach { buffer.put(it) }
    buffer.flip()
    return buffer
}

