package xyz.jmullin.drifter.extensions

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import xyz.jmullin.drifter.VectorHex

/**
 * Convenience methods for [[VectorHex]]s.
 */
fun Vh(q: Float, r: Float, s: Float) = VectorHex(q, r, s)
fun Vh(q: Float, s: Float) = VectorHex(q, s)
fun Vho(x: Int, y: Int) = VectorHex(x*1f, y - (x  - (x and 1)) / 2f)