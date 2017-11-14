package xyz.jmullin.drifter.debug

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Rectangle
import xyz.jmullin.drifter.entity.Entity2D
import xyz.jmullin.drifter.extensions.V2
import xyz.jmullin.drifter.extensions.drifter
import xyz.jmullin.drifter.extensions.flipY
import xyz.jmullin.drifter.extensions.gameBounds
import xyz.jmullin.drifter.rendering.BufferStage
import xyz.jmullin.drifter.rendering.RenderStage
import xyz.jmullin.drifter.rendering.string
import xyz.jmullin.drifter.rendering.texture

class BufferDisplay(private val attachedStage: RenderStage,
                    private val buffer: BufferStage,
                    private val rect: Rectangle = gameBounds(),
                    private val font: BitmapFont,
                    private vararg val keys: Int = intArrayOf(Input.Keys.F7)) : Entity2D() {

    override fun render(stage: RenderStage) {
        keys.forEachIndexed { i, key ->
            if(Gdx.input.isKeyPressed(key) && drifter().devMode) {
                stage.draw(attachedStage) {
                    val (tag, tex) = buffer.textures()[i]
                    texture(tex, rect.flipY())
                    string(tag, V2(5f), font, V2(1f))
                }
            }
        }

        super.render(stage)
    }
}