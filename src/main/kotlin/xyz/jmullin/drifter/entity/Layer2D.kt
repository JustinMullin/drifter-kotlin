package xyz.jmullin.drifter.entity

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import xyz.jmullin.drifter.rendering.ShaderSet
import xyz.jmullin.drifter.rendering.Shaders
import xyz.jmullin.drifter.extensions.V2

/**
 * 2-dimensional layer implementation.  Contains Entity2Ds.
 *
 * @param viewportSize Size of the viewport to use in drawing the world.
 * @param autoCenter If true, the viewport will be auto-centered in the world.
 * @param shader If specified, the [[ShaderSet]] to use by default in rendering sprites.
 */
class Layer2D(override val viewportSize: Vector2, val autoCenter: Boolean, shader: ShaderSet = Shaders.default) : EntityContainer2D, Layer {
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
     * Main sprite batch.  This will be passed to children and will generally be used to render
     * any sprite drawn for this layer.
     */
    val batch = SpriteBatch(1000, shader.program)

    /**
     * Current shader set on the sprite batch.
     */
    var currentShader = shader

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
            batch.projectionMatrix = camera.combined
            batch.begin()

            currentShader.tick()

            renderChildren(batch)

            batch.end()
            batch.flush()
        }
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