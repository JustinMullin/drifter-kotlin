package xyz.jmullin.drifter.rendering

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack
import com.badlogic.gdx.utils.Align
import xyz.jmullin.drifter.entity.Layer2D
import xyz.jmullin.drifter.extensions.*
import xyz.jmullin.drifter.gl.Blend
import xyz.jmullin.drifter.rendering.shader.ShaderSet
import xyz.jmullin.drifter.rendering.shader.Shaders

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
     * Generates a single-pixel texture filled with the specified color.
     */
    fun colorTexture(c: Color): Texture {
        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmap.setColor(c)
        pixmap.fill()
        return Texture(pixmap)
    }

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
    val bounds = Rectangle(sprite.boundingRectangle)
    sprite.setBounds(v.x, v.y, size.x, size.y)
    sprite.draw(this)
    sprite.setBounds(bounds.x, bounds.y, bounds.width, bounds.height)
}

/**
 * Draw a sprite with given bounds.
 */
fun SpriteBatch.sprite(sprite: Sprite, bounds: Rectangle) {
    val oldBounds = Rectangle(sprite.boundingRectangle)
    sprite.setBounds(bounds.x, bounds.y, bounds.width, bounds.height)
    sprite.draw(this)
    sprite.setBounds(oldBounds.x, oldBounds.y, oldBounds.width, oldBounds.height)
}

/**
 * Draw a nine-patch at a given position and size.
 */
fun SpriteBatch.sprite(patch: NinePatch, v: Vector2, size: Vector2) {
    patch.draw(this, v.x, v.y, size.x, size.y)
}

/**
 * Draw a nine-patch with given bounds.
 */
fun SpriteBatch.sprite(patch: NinePatch, bounds: Rectangle) {
    patch.draw(this, bounds.x, bounds.y, bounds.width, bounds.height)
}



/**
 * Draw a texture at a given position and size.
 */
fun SpriteBatch.texture(texture: Texture, v: Vector2, size: Vector2) {
    draw(texture, v.x, v.y, size.x, size.y)
}

/**
 * Draw a texture with given bounds.
 */
fun SpriteBatch.texture(texture: Texture, bounds: Rectangle) {
    draw(texture, bounds.x, bounds.y, bounds.width, bounds.height)
}

/**
 * Draw a texture at a given position and size with the specified texture coords.
 */
fun SpriteBatch.texture(texture: Texture, v: Vector2, size: Vector2, uvA: Vector2, uvB: Vector2) {
    draw(texture, v.x, v.y, size.x, size.y, uvA.x, uvA.y, uvB.x, uvB.y)
}

/**
 * Draw a texture with given bounds and texture coords.
 */
fun SpriteBatch.texture(texture: Texture, bounds: Rectangle, uvA: Vector2, uvB: Vector2) {
    draw(texture, bounds.x, bounds.y, bounds.width, bounds.height, uvA.x, uvA.y, uvB.x, uvB.y)
}

/**
 * Draws a simple filled rectangle with the specified color.
 */
fun SpriteBatch.fill(v: Vector2, size: Vector2, color: Color) {
    Draw.fill.color = color
    sprite(Draw.fill, v, size)
}

/**
 * Draws a simple filled rectangle with the specified color.
 */
fun SpriteBatch.fill(bounds: Rectangle, color: Color) {
    Draw.fill.color = color
    sprite(Draw.fill, bounds)
}

/**
 * Draws a textured quad with the given vertices.
 */
fun SpriteBatch.quad(sprite: Sprite, a: Vector2, b: Vector2, c: Vector2, d: Vector2, color: Color = Color.WHITE) {
    val u = sprite.u
    val v = sprite.v
    val u2 = sprite.u2
    val v2 = sprite.v2

    quad(sprite.texture, a, b, c, d, V2(u, v), V2(u, v2), V2(u2, v2), V2(u2, v), color)
}

/**
 * Draws a textured quad with the given vertices.
 */
fun SpriteBatch.quad(sprite: Sprite, a: Vector2, b: Vector2, c: Vector2, d: Vector2,
                     colA: Color, colB: Color, colC: Color, colD: Color) {
    val u = sprite.u
    val v = sprite.v
    val u2 = sprite.u2
    val v2 = sprite.v2

    quad(sprite.texture, a, b, c, d, V2(u, v), V2(u, v2), V2(u2, v2), V2(u2, v), colA, colB, colC, colD)
}

/**
 * Draws a thick line between two points.
 */
fun SpriteBatch.line(a: Vector2, b: Vector2, thickness: Float, color: Color) {
    val dir = (b - a).nor()
    quad(Draw.fill,
        a + dir.cpy().rotate(90f) * thickness*0.5f,
        a - dir.cpy().rotate(90f) * thickness*0.5f,
        b - dir.cpy().rotate(90f) * thickness*0.5f,
        b + dir.cpy().rotate(90f) * thickness*0.5f,
        color)
}

/**
 * Draws a textured triangle with the given vertices and texture coordinates.
 */
fun SpriteBatch.triangle(texture: Texture,
                         a: Vector2, b: Vector2, c: Vector2,
                         uvA: Vector2, uvB: Vector2, uvC: Vector2,
                         color: Color = Color.WHITE) {
    val col = color.toFloatBits()

    // Really we're drawing a quad with one vertex duplicated,
    // just 'cause that's what SpriteBatch is good at
    val vertices = floatArrayOf(
        a.x, a.y, col, uvA.x, uvA.y,
        b.x, b.y, col, uvB.x, uvB.y,
        c.x, c.y, col, uvC.x, uvC.y,
        a.x, a.y, col, uvA.x, uvA.y)

    draw(texture, vertices, 0, vertices.size)
}

/**
 * Draws a textured quad with the given vertices and texture coordinates.
 */
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
 * Draws a textured quad with the given vertices and texture coordinates.
 */
fun SpriteBatch.quad(texture: Texture,
                     a: Vector2, b: Vector2, c: Vector2, d: Vector2,
                     uvA: Vector2, uvB: Vector2, uvC: Vector2, uvD: Vector2,
                     colA: Color, colB: Color, colC: Color, colD: Color) {
    val cA = colA.toFloatBits()
    val cB = colB.toFloatBits()
    val cC = colC.toFloatBits()
    val cD = colD.toFloatBits()

    val vertices = floatArrayOf(
        a.x, a.y, cA, uvA.x, uvA.y,
        b.x, b.y, cB, uvB.x, uvB.y,
        d.x, d.y, cD, uvD.x, uvD.y,
        a.x, a.y, cA, uvA.x, uvA.y,
        d.x, d.y, cD, uvD.x, uvD.y,
        b.x, b.y, cB, uvB.x, uvB.y,
        c.x, c.y, cC, uvC.x, uvC.y,
        d.x, d.y, cD, uvD.x, uvD.y)

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
    font.draw(this, str, v.x + (align.x - 1) * Draw.layout.width * 0.5f, v.y + (align.y + 2) * Draw.layout.height * 0.5f, width, Align.center, true)
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
fun polygons(layer: Layer2D?, f: (PolygonRenderer) -> Unit, batch: PolygonSpriteBatch = Draw.polygonBatch) {
    batch.begin()
    batch.projectionMatrix = layer?.camera?.projection

    f(PolygonRenderer(batch))

    batch.end()
}

/**
 * Sets up a scissor stack for efficient rectangular clipping.
 */
fun SpriteBatch.clip(layer: Layer2D?, bounds: Rectangle, f: SpriteBatch.() -> Unit) {
    flush()

    val scissors = Rectangle()
    ScissorStack.calculateScissors(layer?.camera, 0f, 0f, gameW().toFloat(), gameH().toFloat(), transformMatrix, bounds, scissors)
    ScissorStack.pushScissors(scissors)

    this.f()

    flush()
    ScissorStack.popScissors();
}

/**
 * Switches to a new shader set, switching back to the default shader afterwards.
 */
fun SpriteBatch.inShader(shaderSet: ShaderSet, f: SpriteBatch.(ShaderProgram) -> Unit) {
    Shaders.switch(shaderSet, this)
    this.f(shaderSet.program!!)

    Shaders.switch(Shaders.default, this)
}

/**
 * Enables a custom blending mode, returning to regular alpha blending afterwards.
 */
fun SpriteBatch.blend(source: Pair<Blend, Blend>, f: SpriteBatch.() -> Unit) = blend(source.first, source.second, f)

/**
 * Enables a custom blending mode, returning to regular alpha blending afterwards.
 */
fun SpriteBatch.blend(source: Blend, dest: Blend, f: SpriteBatch.() -> Unit) {
    flush()

    enableBlending()
    setBlendFunction(source.gl, dest.gl)

    this.f()

    flush()

    setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
}

fun SpriteBatch.transform(rotation: Float, translation: Vector2, f: SpriteBatch.() -> Unit) {
    val previousMatrix = transformMatrix.cpy()
    end()

    transformMatrix
        .setToRotation(V3(0f, 0f, 1f), rotation)
        .translate(translation.xyo)
    begin()

    this.f()

    end()

    transformMatrix.set(previousMatrix)
    begin()
}
