package xyz.jmullin.drifter.rendering

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import xyz.jmullin.drifter.gdx.FloatFrameBuffer
import com.badlogic.gdx.math.Vector2
import xyz.jmullin.drifter.entity.Entity2D
import xyz.jmullin.drifter.extensions.*
import xyz.jmullin.drifter.gdx.MultiTargetFrameBuffer
import xyz.jmullin.drifter.rendering.shader.ShaderSet
import xyz.jmullin.drifter.rendering.shader.Shaders

abstract class RenderStage(val tag: String, val shader: ShaderSet = Shaders.default) {
    var batch = SpriteBatch(1000, shader.program)

    abstract fun dependencies(): List<RenderStage>

    abstract fun begin()
    abstract fun end()

    fun draw(targetStage: RenderStage, stage: SpriteBatch.() -> Unit) {
        if(targetStage == this) {
            stage(batch)
        }
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

abstract class BufferStage(tag: String, shader: ShaderSet = Shaders.default) : RenderStage(tag, shader) {
    var sources = listOf<BufferStage>()
    var fill = false
    var clear = true

    fun attachFrom(vararg bufferStage: BufferStage): BufferStage {
        bufferStage.forEach { sources += it }
        return this
    }

    fun attachSelf(): BufferStage {
        sources += this
        return this
    }

    fun autoFill(): BufferStage {
        fill = true
        return this
    }

    override fun dependencies() = sources

    abstract val targets: List<TargetBuffer>
    abstract fun textures(): List<Pair<String, Texture>>

    fun texture(tag: String) = textures().find { it.first == tag }!!.second
}

data class TargetBuffer(val tag: String,
                        val filter: Texture.TextureFilter,
                        val backgroundColor: Color = Color.BLACK)

class SimpleBufferStage(tag: String,
                        filter: Texture.TextureFilter = Texture.TextureFilter.Nearest,
                        hdr: Boolean = false,
                        backgroundColor: Color = Color.BLACK,
                        size: Vector2 = gameSize(),
                        shader: ShaderSet = Shaders.default) : BufferStage(tag, shader) {

    private val buffer = if(hdr) {
        FloatFrameBuffer(size.xI, size.yI, true)
    } else {
        FrameBuffer(Pixmap.Format.RGBA8888, size.xI, size.yI, true)
    }

    override fun begin() { buffer.begin() }
    override fun end() { buffer.end() }

    override val targets = listOf(TargetBuffer(tag, filter, backgroundColor))

    val texture: Texture by lazy {
        buffer.colorBufferTexture.apply {
            setFilter(filter, filter)
        }
    }

    override fun textures() = listOf(tag to texture)
}

class FixedStage(tag: String, val texture: Texture) : BufferStage(tag) {
    override fun begin() {}
    override fun end() {}

    override val targets: List<TargetBuffer> get() = emptyList()
    override fun textures() = listOf(tag to texture)
}

class PingPongBufferStage(tag: String,
                          filter: Texture.TextureFilter,
                          hdr: Boolean,
                          val iterations: Int,
                          val initialSource: () -> Texture,
                          backgroundColor: Color,
                          size: Vector2,
                          shader: ShaderSet = Shaders.default) : BufferStage(tag, shader) {

    var ping = false

    private val buffers = (0..1).map {
        if(hdr) {
            FloatFrameBuffer(size.xI, size.yI, true)
        } else {
            FrameBuffer(Pixmap.Format.RGBA8888, size.xI, size.yI, true)
        }
    }

    val textureFront by lazy { buffers.first().colorBufferTexture.apply { setFilter(filter, filter) } }
    val textureBack by lazy { buffers.last().colorBufferTexture.apply { setFilter(filter, filter) } }

    val activeBuffer: FrameBuffer get() = (if(ping) buffers.first() else buffers.last())
    val inactiveBuffer: FrameBuffer get() = (if(ping) buffers.last() else buffers.first())

    val activeTexture: Texture get() = (if(ping) textureFront else textureBack)
    val inactiveTexture: Texture get() = (if(ping) textureBack else textureFront)

    override fun begin() { activeBuffer.begin() }
    override fun end() { activeBuffer.end() }

    fun pong() {
        ping = !ping
    }

    override val targets = listOf(TargetBuffer(tag, filter, backgroundColor))

    val texture: Texture get() = activeTexture

    override fun textures() = listOf(tag to texture)
}

class MultiTargetBufferStage(tag: String,
                             size: Vector2,
                             shader: ShaderSet = Shaders.default,
                             override val targets: List<TargetBuffer>) : BufferStage(tag, shader) {
    private val buffer = MultiTargetFrameBuffer(size.xI, size.yI, targets.map { it.tag })

    override fun begin() { buffer.begin() }
    override fun end() { buffer.end() }

    private val textures: List<Texture> by lazy {
        targets.zip(buffer.getColorBufferTextures()).map { (target, texture) ->
            texture.apply { setFilter(target.filter, target.filter) }
        }
    }

    override fun textures() = targets.zip(textures).map { it.first.tag to it.second }
}

class BlitStage(tag: String, val sources: List<BufferStage>, shader: ShaderSet = Shaders.default, val blitOp: ((SpriteBatch) -> Unit)? = null) : RenderStage(tag, shader) {
    override fun dependencies() = sources

    override fun begin() {}
    override fun end() {}
}

class DrawStage(tag: String, shader: ShaderSet = Shaders.default) : RenderStage(tag, shader) {
    override fun dependencies() = emptyList<RenderStage>()

    override fun begin() {}
    override fun end() {}
}
