package xyz.jmullin.drifter.rendering

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.PolygonRegion
import com.badlogic.gdx.graphics.g2d.PolygonSprite
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.EarClippingTriangulator
import com.badlogic.gdx.math.Vector2
import xyz.jmullin.drifter.extensions.list

/**
 * Simple renderer wrapped around a PolygonBatch
 */
class PolygonRenderer(val batch: PolygonSpriteBatch, var texture: TextureRegion = Draw.fill, var color: Color = Color.WHITE) {

    val triangulator = EarClippingTriangulator()

    fun polygon(points: List<Vector2>) {
        val verts = points.flatMap { it.list() }.toFloatArray()

        val sprite = PolygonSprite(PolygonRegion(texture, verts, triangulator.computeTriangles(verts).items))

        sprite.color = color
        sprite.draw(batch)

        batch.flush()
    }
}