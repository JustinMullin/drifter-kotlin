package xyz.jmullin.drifter.extensions

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2

/**
 * Convenience extensions/methods for manipulation of [[ShapeRenderer]]s.
 */
fun ShapeRenderer.circle(v: Vector2, radius: Float) = circle(v.x, v.y, radius)
fun ShapeRenderer.circle(v: Vector2, radius: Float, n: Int) = circle(v.x, v.y, radius, n)