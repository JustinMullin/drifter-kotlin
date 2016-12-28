package xyz.jmullin.drifter.rendering

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.Vector2
import xyz.jmullin.drifter.entity.Entity2D
import xyz.jmullin.drifter.extensions.*

open class RenderStage(val tag: String, val shader: ShaderSet = Shaders.default) {
    var batch = SpriteBatch(1000, shader.program)

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
    
    open fun mouseV(me: Entity2D) = V2(0f)
}

class BufferStage(tag: String,
                  format: Pixmap.Format,
                  val backgroundColor: Color,
                  size: Vector2,
                  shader: ShaderSet = Shaders.default) : RenderStage(tag, shader) {
    val buffer = FrameBuffer(format, size.x.toInt(), size.y.toInt(), true)

    override fun mouseV(me: Entity2D): Vector2 {
        val viewportSize = me.layer()?.viewportSize ?: V2(0f)
        val overdrawAspect = buffer.size / gameSize()
        val newViewSize = viewportSize * overdrawAspect
        val relativeOverdraw = ((overdrawAspect-1f)*viewportSize) / 2f

        //return (mouseV() / viewportSize) + relativeOverdraw
        return (me.unproject(rawMouseV()) / newViewSize) * viewportSize + relativeOverdraw
    }
}

class BlitStage(tag: String, val sources: List<BufferStage>, shader: ShaderSet = Shaders.default) : RenderStage(tag, shader)

class DrawStage(tag: String, shader: ShaderSet = Shaders.default) : RenderStage(tag, shader)