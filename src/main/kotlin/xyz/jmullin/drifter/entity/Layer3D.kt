package xyz.jmullin.drifter.entity

import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import xyz.jmullin.drifter.extensions.*

/**
 * 3-dimensional layer implementation.  Contains Entity3Ds.
 *
 * @param viewportSize Size of the viewport to use in drawing the world.
 */
class Layer3D(override val viewportSize: Vector2, fov: Float = 67f, shaderProvider: ShaderProvider = DefaultShaderProvider()) : EntityContainer3D, Layer {
    override var children = emptyList<Entity3D>()
    override var mouseLocked: Vector2? = null

    // Self reference for containership
    override fun layer() = this

    var visible = true
    var active = true

    /**
     * Camera used to render the world.
     */
    var camera = PerspectiveCamera(fov, viewportSize.x, viewportSize.y)

    /**
     * Viewport for world-space rendering.
     */
    override var viewport: Viewport? = ExtendViewport(viewportSize.x, viewportSize.y, camera)

    /**
     * Main model batch.  This will be passed to children and will generally be used to render
     * any model drawn for this layer.
     */
    val batch = ModelBatch(shaderProvider)

    /**
     * 3D environment for light/shadow config.
     */
    val environment = Environment()

    init {
        environment.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.0f))

        // Set camera parameters and run an initial update tick.
        camera.position.set(V3(1f))
        camera.lookAt(0f, 0f, 0f)
        camera.near = 0.1f
        camera.far = 1000f
        camera.update()
    }

    /**
     * Given another layer, links the camera and viewport instances for those layers, syncing pan/zoom/etc.
     * This can simplify the setup of two parallel layers.
     *
     * @param layer Layer to link to.
     */
    fun linkTo(layer: Layer3D) {
        camera = layer.camera
        viewport = layer.viewport
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

    /**
     * Draws this layer and any children (if the layer is visible).
     */
    override fun render() {
        if(visible) {
            camera.update()

            batch.begin(camera)
            renderChildren(batch, environment)
            batch.end()
        }
    }

    override fun resize(newSize: Vector2) {
        viewport?.update(newSize.x.toInt(), newSize.y.toInt(), false)
    }

    /**
     * Dispose layer resources.
     */
    override fun dispose() {
        batch.dispose()
    }
}