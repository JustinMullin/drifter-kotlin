package xyz.jmullin.drifter.application

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20.*
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputListener
import xyz.jmullin.drifter.entity.Layer
import xyz.jmullin.drifter.entity.Layer2D
import xyz.jmullin.drifter.entity.Layer3D
import xyz.jmullin.drifter.extensions.V2
import xyz.jmullin.drifter.rendering.BlitStage
import xyz.jmullin.drifter.rendering.RenderStage

/**
 * Screen implementation for use with Drifter, facilitates creation and management of layers for
 * render/update of entities.
 */
open class DrifterScreen : DrifterInput, Screen {
    open val backgroundColor = Color.BLACK

    /**
     * Input listeners attached to this screen.
     */
    var inputListeners = emptyList<InputListener>()

    /**
     * Layers attached to this screen.
     */
    var layers = emptyList<Layer>()
        set(list) { field = list.sortedBy { it.index } }

    /**
     * Create and attach a new Layer2D to this screen.
     *
     * @param index Rendering index of this layer.
     * @param size Size of the new layer.
     * @param autoCenter If true, auto-center the layer's viewport.
     * @param stages Collection of render stage tags to use.
     * @param shader If specified, the default shader for rendering this layer.
     * @return The created Layer.
     */
    fun newLayer2D(index: Int,
                   size: Vector2,
                   autoCenter: Boolean = false,
                   rootStage: RenderStage,
                   init: Layer2D.() -> Unit = {}): Layer2D {
        val stages = if(rootStage is BlitStage) {
            stageDependencies(rootStage)
        } else {
            listOf(rootStage)
        }

        val layer = Layer2D(index, size, autoCenter, stages)
        layers += layer
        init(layer)
        return layer
    }

    /**
     * Constructs a list of render stages by dependency order, so that stages depended on by others
     * are processed first.
     */
    private fun stageDependencies(root: RenderStage): List<RenderStage> {
        var stages = listOf(root)
        stages += root.dependencies()
        var next = root.dependencies().flatMap { it.dependencies() }
        while(next.isNotEmpty()) {
            stages += next
            next = next.flatMap { it.dependencies() }.filterNot(stages::contains)
        }
        return stages.reversed().distinct()
    }

    /**
     * Create and attach a new Layer3D to this screen.
     *
     * @param index Rendering index of this layer.
     * @param size Size of the new layer.
     * @param fov Field of view to render this layer at.
     * @return The created Layer.
     */
    fun newLayer3D(index: Int, size: Vector2, shaderProvider: ShaderProvider = DefaultShaderProvider()): Layer3D {
        val layer = Layer3D(index, size, shaderProvider)
        layers += layer
        return layer
    }

    /**
     * Attaches a layer to this screen.
     *
     * @param layer The Layer to attach.
     */
    fun <L : Layer> add(layer: L): L {
        layers += layer
        return layer
    }

    /**
     * Renders the screen and all attached layers, clearing the screen and setting up default blending modes.
     *
     * @param delta Time elapsed since the last frame.
     */
    override fun render(delta: Float) {
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, 1f)
        Gdx.gl20.glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        Gdx.gl20.glEnable(GL_BLEND)
        Gdx.gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        layers.forEach { it.update(delta) }

        layers.forEach(Layer::render)
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
        //resize(gameW(), gameH())

        val inputs = (layers + this).reversed().toTypedArray() + DebugInput()

        Gdx.input.inputProcessor = InputMultiplexer(*inputs)
    }

    /**
     * Dispose screen resources.
     */
    override fun dispose() {
        layers.forEach(Layer::dispose)
        layers = listOf()
    }

    override fun hide() {}
    override fun pause() {}
    override fun resume() {}

}
