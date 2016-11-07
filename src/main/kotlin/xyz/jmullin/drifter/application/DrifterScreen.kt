package xyz.jmullin.drifter.application

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20.*
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider
import com.badlogic.gdx.math.Vector2
import xyz.jmullin.drifter.entity.Layer
import xyz.jmullin.drifter.entity.Layer2D
import xyz.jmullin.drifter.entity.Layer3D
import xyz.jmullin.drifter.extensions.V2
import xyz.jmullin.drifter.extensions.gameH
import xyz.jmullin.drifter.extensions.gameW
import xyz.jmullin.drifter.rendering.ShaderSet
import xyz.jmullin.drifter.rendering.Shaders

/**
 * Screen implementation for use with Drifter, facilitates creation and management of layers for
 * render/update of entities.
 *
 * @param background Background color to clear to on each frame.
 */
open class DrifterScreen(val background: Color = Color.BLACK) : DrifterInput, Screen {
    /**
     * Layers attached to this screen.
     */
    var layers = emptyList<Layer>()
        set(list) { field = list.sortedBy { it.index } }

    /**
     * Create and attach a new Layer2D to this screen.
     *
     * @param size Size of the new layer.
     * @param autoCenter If true, auto-center the layer's viewport.
     * @param shader If specified, the default shader for rendering this layer.
     * @return The created Layer.
     */
    fun newLayer2D(index: Int, size: Vector2, autoCenter: Boolean=false, shader: ShaderSet = Shaders.default): Layer2D {
        val layer = Layer2D(index, size, autoCenter, shader)
        layers += layer
        return layer
    }

    /**
     * Create and attach a new Layer3D to this screen.
     *
     * @param size Size of the new layer.
     * @return The created Layer.
     */
    fun newLayer3D(index: Int, size: Vector2, fov: Float = 67f, shaderProvider: ShaderProvider = DefaultShaderProvider()): Layer3D {
        val layer = Layer3D(index, size, fov, shaderProvider)
        layers += layer
        return layer
    }

    /**
     * Renders the screen and all attached layers, clearing the screen and setting up default blending modes.
     *
     * @param delta Time elapsed since the last frame.
     */
    override fun render(delta: Float) {
        Gdx.gl.glClearColor(background.r, background.g, background.b, background.a)
        Gdx.gl20.glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        Gdx.gl20.glEnable(GL_BLEND)
        Gdx.gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        layers.forEach { it.update(delta) }

        layers.forEach { it.render() }
    }

    /**
     * Updates the viewports of attached layers when the screen is resized.
     *
     * @param width New width of the screen.
     * @param height New height of the screen.
     */
    override fun resize(width: Int, height: Int) {
        layers.forEach { l -> l.resize(V2(width, height)) }
    }

    /**
     * Initializes the screen when shown.
     */
    override fun show() {
        resize(gameW(), gameH())

        val inputs = (layers + this).reversed().toTypedArray()

        Gdx.input.inputProcessor = InputMultiplexer(*inputs)
    }

    /**
     * Dispose screen resources.
     */
    override fun dispose() {
        layers.forEach { it.dispose() }
        layers = listOf()
    }

    override fun hide() {}
    override fun pause() {}
    override fun resume() {}
}