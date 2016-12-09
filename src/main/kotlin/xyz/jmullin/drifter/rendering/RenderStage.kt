package xyz.jmullin.drifter.rendering

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import xyz.jmullin.drifter.entity.Layer2D

open class RenderStage(val tag: String, val shader: ShaderSet = Shaders.default) {
    val batch = SpriteBatch(1000, shader.program)

    fun draw(targetStage: RenderStage, stage: SpriteBatch.() -> Unit): Unit {
        if(targetStage == this) {
            stage(batch)
        }
    }

    fun draw(stage: SpriteBatch.() -> Unit): Unit {
        stage(batch)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as RenderStage

        if (tag != other.tag) return false

        return true
    }

    override fun hashCode(): Int {
        return tag.hashCode()
    }
}

class BufferStage(tag: String,
                  format: Pixmap.Format,
                  val backgroundColor: Color,
                  size: Vector2,
                  shader: ShaderSet = Shaders.default) : RenderStage(tag, shader) {
    val buffer = FrameBuffer(format, size.x.toInt(), size.y.toInt(), true)
}

class BlitStage(tag: String, val sources: List<BufferStage>, shader: ShaderSet = Shaders.default) : RenderStage(tag, shader)

class DrawStage(tag: String, shader: ShaderSet = Shaders.default) : RenderStage(tag, shader)