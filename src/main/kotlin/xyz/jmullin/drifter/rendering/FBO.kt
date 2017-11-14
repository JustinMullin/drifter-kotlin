package xyz.jmullin.drifter.rendering

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.Vector2
import xyz.jmullin.drifter.entity.Entity2D
import xyz.jmullin.drifter.entity.EntityContainer2D
import xyz.jmullin.drifter.extensions.div
import xyz.jmullin.drifter.extensions.xI
import xyz.jmullin.drifter.extensions.yI
import xyz.jmullin.drifter.rendering.shader.ShaderSet

class FBO(private val _size: Vector2,
          private val attachedStage: RenderStage,
          private val shader: ShaderSet,
          private val redraw: SpriteBatch.() -> Unit) : Entity2D() {

    private var filter = Texture.TextureFilter.Nearest

    var batch = SpriteBatch(1000, shader.program)

    private var dirty = true

    val camera = OrthographicCamera(size.x, size.y)
    private val buffer = FrameBuffer(Pixmap.Format.RGBA8888, _size.xI, _size.yI, false)

    val texture: Texture by lazy {
        buffer.colorBufferTexture.apply {
            setFilter(filter, filter)
        }
    }

    override fun create(container: EntityContainer2D) {
        size.set(_size)
        depth = Float.MIN_VALUE

        super.create(container)
    }

    override fun render(stage: RenderStage) {
        if(dirty) {
            stage.draw(attachedStage) {
                end()

                camera.setToOrtho(false, size.x, size.y)
                camera.position.set(size / 2f, 0f)
                camera.update()
                batch.projectionMatrix = camera.combined

                buffer.begin()

                Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
                Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT)
                Gdx.gl20.glEnable(GL20.GL_BLEND)
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

                batch.begin()
                this@FBO.shader.update()

                redraw(batch)

                batch.end()
                batch.flush()
                buffer.end()

                begin()
            }

            dirty = false
        }

        super.render(stage)
    }

    fun refresh() {
        dirty = true
    }

    companion object {
        fun Entity2D.fbo(size: Vector2, attachedStage: RenderStage, shaderSet: ShaderSet, redraw: SpriteBatch.() -> Unit): FBO {
            return add(FBO(size, attachedStage, shaderSet, redraw))
        }
    }
}