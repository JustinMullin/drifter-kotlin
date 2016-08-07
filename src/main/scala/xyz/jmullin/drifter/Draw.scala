package xyz.jmullin.drifter

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d._
import com.badlogic.gdx.graphics.glutils.{ShaderProgram, ShapeRenderer}
import com.badlogic.gdx.graphics.{GL20, Texture}
import com.badlogic.gdx.utils.Align
import xyz.jmullin.drifter.extensions.RichGeometry._
import xyz.jmullin.drifter.entity.Layer2D

/**
 * Convenience methods for drawing things succinctly.
 */
object Draw {
  /**
   * Shared GlyphLayout for calculating text display.  Very not thread safe!
   */
  private var layout = new GlyphLayout()

  /**
   * Draw a sprite at a given position and size.
   */
  fun sprite(sprite: Sprite, v: Vector2, size: Vector2)(implicit batch: Batch) {
    sprite.setBounds(v.x, v.y, size.x, size.y)
    sprite.draw(batch)
  }

  /**
   * Draw a nine-patch at a given position and size.
   */
  fun sprite(patch: NinePatch, v: Vector2, size: Vector2)(implicit batch: Batch) {
    patch.draw(batch, v.x, v.y, size.x, size.y)
  }

  /**
   * Draw a texture at a given position and size.
   */
  fun texture(texture: Texture, v: Vector2, size: Vector2)(implicit batch: Batch) {
    batch.draw(texture, v.x, v.y, size.x, size.y)
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
  fun shapes(kind: ShapeRenderer.ShapeType, shader: ShaderProgram=Shaders.default.program)(f: ShapeRenderer -> Unit)(implicit layer: Layer2D, batch: Batch) {
    batch.end()
    Gdx.gl.glEnable(GL20.GL_BLEND)
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
    val r = new ShapeRenderer(5000, shader)
    r.begin(kind)
    r.setProjectionMatrix(layer.camera.combined)

    f(r)

    r.end()
    r.dispose()
    Gdx.gl.glDisable(GL20.GL_BLEND)
    batch.begin()
  }

  /**
    * Draw a string with the given location, font and no alignment.
    */
  fun string(str: String, v: Vector2, font: BitmapFont)(implicit batch: Batch) {
    layout.setText(font, str)
    font.draw(batch, str, v.x, v.y)
  }

  /**
   * Draw a string with the given location, font and alignment.
   */
  fun string(str: String, v: Vector2, font: BitmapFont, align: Vector2)(implicit batch: Batch) {
    layout.setText(font, str)
    font.draw(batch, str, v.x + (align.x - 1) * layout.width * 0.5f, v.y + (align.y + 1) * layout.height * 0.5f)
  }

  /**
   * Draw a string with the given location, font and alignment, wrapped to the specified width.
   */
  fun stringWrapped(str: String, v: Vector2, width: Float, font: BitmapFont, align: Vector2)(implicit batch: Batch) {
    layout.setText(font, str, font.getColor, width, Align.center, true)
    font.draw(batch, str, v.x - layout.width / 2f, v.y + (align.y + 1) * layout.height * 0.5f)
  }

  /**
    * Draw a string with the given location, font and alignment, wrapped to the specified width.
    */
  fun stringWrapped(str: String, v: Vector2, start: Int, end: Int, width: Float, font: BitmapFont, hAlign: Int, align: Vector2)(implicit batch: Batch) {
    layout.setText(font, str, start, end, font.getColor, width, hAlign, true, null)
    font.draw(batch, layout, v.x, v.y)
  }
}