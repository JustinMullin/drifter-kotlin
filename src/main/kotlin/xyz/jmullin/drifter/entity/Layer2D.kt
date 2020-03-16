package xyz.jmullin.drifter.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.glutils.ShaderProgram
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
 */
open class Layer2D(override val index: Int,
                   final override val viewportSize: Vector2,
                   private val autoCenter: Boolean,
                   private val stages: Collection<RenderStage>) : EntityContainer2D, Layer {

    override var paused: Boolean = false
    override var hidden: Boolean = false

    // Self reference for containership
    override fun layer() = this

    var visible = true
    var active = true

    override var children = emptyList<Entity2D>()

    /**
     * Camera used to render the world.
     */
    var camera = OrthographicCamera(viewportSize.x, viewportSize.y)

    // TODO: these need to be cleaned up
    var cameraPos = V2(0f)
    var cameraRot = 0f
    var actualCameraRot = 0f
    var cameraZoom = 1f

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
                    is PingPongBufferStage -> pingPongBufferStage(stage)
                    is BufferStage -> renderBufferStage(stage, stage.sources.flatMap { it.textures() })
                    is DrawStage -> renderDrawStage(stage)
                }
            }
        }
    }

    private fun renderBlitStage(stage: BlitStage) {
        stage.batch.shader = stage.shader.program
        stage.batch.begin()
        stage.shader.update()

        stage.sources.flatMap { it.textures() }
            .forEachIndexed { i, (tag, texture) ->
            val unit = stage.sources.size - i
            texture.bind(unit)
            stage.shader.program?.setUniformi(tag, unit)
        }
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0)

        stage.blitOp?.invoke(stage.batch) ?: {
            stage.batch.fill(gameBounds(), Color.WHITE)
        }()

        stage.batch.end()
    }

    private fun pingPongBufferStage(stage: PingPongBufferStage) {
        stage.ping = false

        val sourceTextures = stage.sources.flatMap { it.textures() }
        renderBufferStage(stage, sourceTextures + listOf("source" to stage.initialSource())) { it.setUniformi("ping", 0); it.setUniformi("pingPongStep", 0) }
        (0 until stage.iterations).forEachIndexed { i, _ ->
            stage.pong()
            renderBufferStage(stage, sourceTextures + listOf("source" to stage.inactiveTexture)) { it.setUniformi("ping", (i+1) % 2); it.setUniformi("pingPongStep", i+1) }
        }
    }

    private fun renderBufferStage(stage: BufferStage, sourceTextures: List<Pair<String, Texture>>,
                                  uniforms: (ShaderProgram) -> Unit = {}) {

        camera.setToOrtho(false, viewportSize.x, viewportSize.y)
        camera.view.setToRotation(V3(0f, 0f, 1f), cameraRot)
        camera.position.set(V3(cameraPos, 0f))
        camera.zoom = cameraZoom
        viewport?.setWorldSize(viewportSize.x, viewportSize.y)
        camera.update()
        stage.batch.projectionMatrix = camera.combined
        stage.begin()

        if(stage.clear) {
            stage.targets.forEachIndexed { i, target ->
                Gdx.gl30.glClearBufferfv(GL30.GL_COLOR, i, target.backgroundColor.toBuffer())
            }
        }
        Gdx.gl20.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        stage.batch.shader = stage.shader.program
        stage.batch.begin()
        stage.shader.update()

        stage.shader.program?.let(uniforms)

        sourceTextures.forEachIndexed { i, (tag, texture) ->
            val unit = sourceTextures.size - i
            texture.bind(unit)
            stage.shader.program?.setUniformi(tag, unit)
        }
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0)

        renderChildren(stage)

        if(stage.fill) {
            stage.batch.fill(gameBounds(), Color.MAGENTA)
        }

        stage.batch.end()
        stage.batch.flush()
        stage.end()
        viewport?.setWorldSize(viewportSize.x, viewportSize.y)
        camera.setToOrtho(false, viewportSize.x, viewportSize.y)
    }

    fun renderDrawStage(stage: DrawStage) {
        camera.position.set(V3(cameraPos, 0f))
        camera.update()
        camera.rotate(cameraRot - actualCameraRot)
        actualCameraRot = cameraRot
        camera.zoom = cameraZoom

        stage.batch.projectionMatrix = camera.combined
        stage.batch.shader = stage.shader.program
        stage.batch.begin()
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
        viewportSize.set(newSize.cpy())
        viewport?.update(newSize.x.toInt(), newSize.y.toInt(), autoCenter)
        camera.setToOrtho(false, viewportSize.x, viewportSize.y)
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

    override fun keyDown(keycode: Int): Boolean {
        children.forEach { it.keyDown(keycode) }
        return super.keyDown(keycode)
    }

    override fun keyTyped(character: Char): Boolean {
        children.forEach { it.keyTyped(character) }
        return super.keyTyped(character)
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
