package xyz.jmullin.drifter.rendering

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.Vector2

open class RenderStage(val tag: String, val shader: ShaderSet = Shaders.default) {
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

class DrawStage(tag: String, val sources: List<BufferStage>, shader: ShaderSet = Shaders.default) : RenderStage(tag, shader) {
    val batch = SpriteBatch(10, shader.program)
}

class BufferStage(tag: String, format: Pixmap.Format, size: Vector2, shader: ShaderSet = Shaders.default) : RenderStage(tag, shader) {
    val buffer = FrameBuffer(format, size.x.toInt(), size.y.toInt(), true)
    val batch = SpriteBatch(1000, shader.program)


}