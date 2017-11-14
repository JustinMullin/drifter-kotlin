package xyz.jmullin.drifter.debug

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.math.Vector2
import xyz.jmullin.drifter.entity.Entity2D
import xyz.jmullin.drifter.entity.EntityContainer2D
import xyz.jmullin.drifter.extensions.*
import xyz.jmullin.drifter.rendering.Draw
import xyz.jmullin.drifter.rendering.RenderStage
import xyz.jmullin.drifter.rendering.sprite
import xyz.jmullin.drifter.rendering.string

/**
 * Simple FPS debug display.
 *
 * @param font Font used to draw FPS counter.
 * @param color Color used to draw FPS counter.
 * @param backgroundColor Color used to draw a background box behind FPS counter.
 * @param align Alignment of FPS counter on the screen - if align is (1, 1), for example, FPS will be drawn
 *              aligned at the top-right of the screen.  At (0, 0) it will be drawn at the lower left.
 */
class FPSDisplay(val font: BitmapFont,
                 val color: Color = Color.YELLOW,
                 val backgroundColor: Color = Color.CLEAR,
                 val align: Vector2 = V2(0, 1),
                 val attachedStage: RenderStage) : Entity2D() {

    private val layout = GlyphLayout()

    override fun create(container: EntityContainer2D) {
        if(!drifter().devMode) remove()

        super.create(container)
    }

    override fun render(stage: RenderStage) {
        layout.setText(font, "FPS: 100")
        val textBounds = V2(layout.width, layout.height)

        val v = V2(5f) + (gameSize() - 10f - textBounds) * align

        font.color = color
        stage.draw(attachedStage) {
            Draw.fill.color = backgroundColor
            sprite(Draw.fill, v - V2(5f), textBounds + V2(10f))
            string("FPS: ${gameFps()}", v, font, V2(1, 1))
        }
    }
}
