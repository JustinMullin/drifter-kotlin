package xyz.jmullin.drifter.rendering

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import xyz.jmullin.drifter.entity.Layer2D
import xyz.jmullin.drifter.extensions.V2

/**
 * Convenience methods for drawing things succinctly.
 */
object Draw {
    /**
     * Shared GlyphLayout for calculating text display.  Very not thread safe!
     */
    internal var layout = GlyphLayout()

    /**
     * Shared PolygonSpriteBatch for rendering polygons.
     */
    internal val polygonBatch = PolygonSpriteBatch(2000, Shaders.default.program)

    /**
     * Simple 1px white fill sprite for drawing filled regions.
     */
    val fill: Sprite by lazy {
        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.WHITE)
        pixmap.fill()
        Sprite(Texture(pixmap))
    }
}

/**
 * Draw a sprite at a given position and size.
 */
fun SpriteBatch.sprite(sprite: Sprite, v: Vector2, size: Vector2) {
    sprite.setBounds(v.x, v.y, size.x, size.y)
    sprite.draw(this)
}

/**
 * Draw a nine-patch at a given position and size.
 */
fun SpriteBatch.sprite(patch: NinePatch, v: Vector2, size: Vector2) {
    patch.draw(this, v.x, v.y, size.x, size.y)
}

/**
 * Draw a texture at a given position and size.
 */
fun SpriteBatch.texture(texture: Texture, v: Vector2, size: Vector2) {
    draw(texture, v.x, v.y, size.x, size.y)
}

/**
 * Draw a texture at a given position and size.
 */
fun SpriteBatch.texture(texture: Texture, v: Vector2, size: Vector2, uvA: Vector2, uvB: Vector2) {
    draw(texture, v.x, v.y, size.x, size.y, uvA.x, uvA.y, uvB.x, uvB.y)
}

fun SpriteBatch.quad(sprite: Sprite, a: Vector2, b: Vector2, c: Vector2, d: Vector2, color: Color = Color.WHITE) {
    val u = sprite.u
    val v = sprite.v
    val u2 = sprite.u2
    val v2 = sprite.v2

    quad(sprite.texture, a, b, c, d, V2(u, v), V2(u, v2), V2(u2, v2), V2(u2, v), color)
}

fun SpriteBatch.quad(texture: Texture,
                     a: Vector2, b: Vector2, c: Vector2, d: Vector2,
                     uvA: Vector2, uvB: Vector2, uvC: Vector2, uvD: Vector2,
                     color: Color = Color.WHITE) {
    val col = color.toFloatBits()

    val vertices = floatArrayOf(
        a.x, a.y, col, uvA.x, uvA.y,
        b.x, b.y, col, uvB.x, uvB.y,
        d.x, d.y, col, uvD.x, uvD.y,
        a.x, a.y, col, uvA.x, uvA.y,
        d.x, d.y, col, uvD.x, uvD.y,
        b.x, b.y, col, uvB.x, uvB.y,
        c.x, c.y, col, uvC.x, uvC.y,
        d.x, d.y, col, uvD.x, uvD.y)

    draw(texture, vertices, 0, vertices.size)
}

/**
 * Given a [[ShapeRenderer.ShapeType]] to draw and a Unit block, provides an active [[ShapeRenderer]] with
 * basic parameters setup to simplify the process of drawing primitives to the screen.
 *
 * '''Example:'''
 *
 * {{{
 * Draw.shapes(ShapeType.Filled) { r ->
 *   r.rect(0, 0, 100, 100)
 * }
 * }}}
 */
fun SpriteBatch.shapes(kind: ShapeRenderer.ShapeType, shader: ShaderProgram?= Shaders.default.program, f: (ShapeRenderer) -> Unit, layer: Layer2D) {
    end()
    Gdx.gl.glEnable(GL20.GL_BLEND)
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
    val r = ShapeRenderer(5000, shader)
    r.begin(kind)
    r.projectionMatrix = layer.camera.combined

    f(r)

    r.end()
    r.dispose()
    Gdx.gl.glDisable(GL20.GL_BLEND)
    begin()
}

/**
 * Draw a string with the given location, font and no alignment.
 */
fun SpriteBatch.string(str: String, v: Vector2, font: BitmapFont) {
    Draw.layout.setText(font, str)
    font.draw(this, str, v.x, v.y)
}

/**
 * Draw a string with the given location, font and alignment.
 */
fun SpriteBatch.string(str: String, v: Vector2, font: BitmapFont, align: Vector2) {
    Draw.layout.setText(font, str)
    font.draw(this, str, v.x + (align.x - 1) * Draw.layout.width * 0.5f, v.y + (align.y + 1) * Draw.layout.height * 0.5f)
}

/**
 * Draw a string with the given location, font and alignment, wrapped to the specified width.
 */
fun SpriteBatch.stringWrapped(str: String, v: Vector2, width: Float, font: BitmapFont, align: Vector2) {
    Draw.layout.setText(font, str, font.color, width, Align.center, true)
    font.draw(this, str, v.x - Draw.layout.width / 2f, v.y + (align.y + 1) * Draw.layout.height * 0.5f)
}

/**
 * Draw a string with the given location, font and alignment, wrapped to the specified width.
 */
fun SpriteBatch.stringWrapped(str: String, v: Vector2, start: Int, end: Int, width: Float, font: BitmapFont, hAlign: Int) {
    Draw.layout.setText(font, str, start, end, font.color, width, hAlign, true, null)
    font.draw(this, Draw.layout, v.x, v.y)
}

/**
 * Set up a polygon renderer for drawing filled polygons.
 */
fun polygons(layer: Layer2D?, f: (PolygonRenderer) -> Unit) {
    Draw.polygonBatch.begin()
    Draw.polygonBatch.projectionMatrix = layer?.camera?.projection

    f(PolygonRenderer(Draw.polygonBatch))

    Draw.polygonBatch.end()
}