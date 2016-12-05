package xyz.jmullin.drifter.debug

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import xyz.jmullin.drifter.entity.Entity2D
import xyz.jmullin.drifter.extensions.*
import xyz.jmullin.drifter.rendering.Draw
import xyz.jmullin.drifter.rendering.RenderStage

/**
 * Simple FPS debug display.
 *
 * @param font Font used to draw FPS counter.
 * @param color Color used to draw FPS counter.
 * @param align Alignment of FPS counter on the screen - if align is (1, 1), for example, FPS will be drawn
 *              aligned at the top-right of the screen.  At (0, 0) it will be drawn at the lower left.
 */
class FPSDisplay(val font: BitmapFont, val color: Color = Color.YELLOW, val align: Vector2 = V2(0, 1)) : Entity2D() {
    override fun render(stage: RenderStage) {
        font.color = Color.YELLOW
        Draw.string("FPS: ${gameFps()}", align.max(V2(0f))*gameSize() + align.inverse()*3f, font, align.inverse(), stage.batch)
    }
}
