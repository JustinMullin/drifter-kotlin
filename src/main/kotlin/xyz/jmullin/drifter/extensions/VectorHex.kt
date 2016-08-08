package xyz.jmullin.drifter.extensions

import xyz.jmullin.drifter.geometry.VectorHex

/**
 * Convenience methods for [[VectorHex]]s.
 */
fun Vh(q: Float, r: Float, s: Float) = VectorHex(q, r, s)
fun Vh(q: Int, r: Int, s: Int) = VectorHex(q, r, s)
fun Vh(q: Float, s: Float) = VectorHex(q, s)
fun Vh(q: Int, s: Int) = VectorHex(q, s)
fun Vho(x: Int, y: Int) = VectorHex(x.toFloat(), y - (x  + (x and 1)) / 2f)