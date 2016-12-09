package xyz.jmullin.drifter.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import xyz.jmullin.drifter.extensions.*
import xyz.jmullin.drifter.rendering.*

/**
 * 2-dimensional layer implementation.  Contains Entity2Ds.
 *
 * @param index Rendering index to use for draw ordering.
 * @param viewportSize Size of the viewport to use in drawing the world.
 * @param autoCenter If true, the viewport will be auto-centered in the world.
 * @param stages Rendering stages to draw with.
 * @param shader If specified, the [[ShaderSet]] to use by default in rendering sprites.
 */
class Layer2D(override val index: Int,
              override val viewportSize: Vector2,
              val autoCenter: Boolean,
              val stages: Collection<RenderStage>) : EntityContainer2D, Layer {
    // Self reference for containership
    override fun layer() = this

    var visible = true
    var active = true

    override var children = emptyList<Entity2D>()

    /**
     * Camera used to render the world.
     */
    var camera = OrthographicCamera(viewportSize.x, viewportSize.y)

    /**
     * Viewport for world-space rendering.
     */
    override var viewport: Viewport? = ExtendViewport(viewportSize.x, viewportSize.y, camera)

    /**
     * Given another layer, links the camera and viewport instances for those layers, syncing pan/zoom/etc.
     * This can simplify the setup of two parallel layers.
     *
     * @param layer Layer to link to.
     */
    fun linkTo(layer: Layer2D) {
        camera = layer.camera
        viewport = layer.viewport
    }

    /**
     * Draws this layer and any children (if the layer is visible).
     */
    override fun render() {
        if(visible) {
            stages.forEach { stage ->
                when(stage) {
                    is BlitStage -> renderBlitStage(stage)
                    is BufferStage -> renderBufferStage(stage)
                    is DrawStage -> renderDrawStage(stage)
                }
            }
        }
    }

    fun renderBlitStage(stage: BlitStage) {
        stage.batch.begin()
        stage.shader.tick()
        stage.shader.update()

        stage.sources.forEachIndexed { i, source ->
            val unit = stage.sources.size - i
            source.buffer.colorBufferTexture?.bind(unit)
            stage.shader.program?.setUniformi(source.tag, unit)
        }
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0)

        stage.batch.sprite(Draw.fill, V2(0f, gameH()), gameSize() * V2(1f, -1f))

        stage.batch.end()
    }

    fun renderBufferStage(stage: BufferStage) {
        camera.setToOrtho(false, stage.buffer.width.toFloat(), stage.buffer.height.toFloat())
        stage.batch.projectionMatrix = camera.combined
        stage.buffer.begin()

        Gdx.gl.glClearColor(stage.backgroundColor.r, stage.backgroundColor.g, stage.backgroundColor.b, stage.backgroundColor.a)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        Gdx.gl20.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        stage.batch.begin()
        stage.shader.tick()
        stage.shader.update()

        renderChildren(stage)

        stage.batch.end()
        stage.batch.flush()
        stage.buffer.end()
        camera.setToOrtho(false, viewportSize.x, viewportSize.y)
    }

    fun renderDrawStage(stage: DrawStage) {
        stage.batch.projectionMatrix = camera.combined
        stage.batch.begin()
        stage.shader.tick()
        stage.shader.update()

        renderChildren(stage)

        stage.batch.end()
        stage.batch.flush()
    }

    /**
     * Updates this layer and any children (if the layer is active).
     *
     * @param delta Time elapsed in seconds since the last update tick.
     */
    override fun update(delta: Float) {
        if(active) {
            camera.update()
            updateChildren(delta)
        }
    }

    override fun touchDown(v: Vector2, pointer: Int, button: Int): Boolean {
        // Ignore touch events if inactive
        return if(!active) false else super.touchDown(unproject(v), pointer, button)
    }

    override fun resize(newSize: Vector2) {
        viewport?.update(newSize.x.toInt(), newSize.y.toInt(), autoCenter)
    }

    override fun touchUp(v: Vector2, pointer: Int, button: Int): Boolean {
        // Ignore touch events if inactive
        return if(!active) false else super.touchUp(unproject(v), pointer, button)
    }

    override fun mouseMoved(v: Vector2): Boolean {
        // Ignore touch events if inactive
        return if (!active) false else {
            val localV = unproject(v)
            super.mouseMoved(localV)
            children.find { it.containsPoint(localV) } != null
        }
    }

    /**
     * Unprojects a screen coordinate into the world space of the viewport.
     *
     * @param v Point to unproject.
     * @return The corresponding world space coordinate.
     */
    fun unproject(v: Vector2) = viewport?.unproject(v.cpy()) ?: V2(0f)

    /**
     * Projects a world coordinate into screen space via the viewport.
     *
     * @param v Point to project.
     * @return The corresponding screen space coordinate.
     */
    fun project(v: Vector2) = viewport?.project(v.cpy()) ?: V2(0f)

    override fun dispose() {}
}