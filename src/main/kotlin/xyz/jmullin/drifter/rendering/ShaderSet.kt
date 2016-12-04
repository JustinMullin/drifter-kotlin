package xyz.jmullin.drifter.rendering

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import xyz.jmullin.drifter.debug.*
import xyz.jmullin.drifter.extensions.game

/**
 * Given files to load shader definitions from, compiles and wraps a [[ShaderProgram]] and functionality
 * for reloading the program.  Define init() and tick() methods to set shader uniforms.
 *
 * @param fragmentShaderName Filename of the fragment shader to load.
 * @param vertexShaderName Filename of the vertex shader to load.
 */
open class ShaderSet(val fragmentShaderName: String, val vertexShaderName: String) {
    val vert = Gdx.files.internal("shader/$vertexShaderName.vert")!!
    val frag = Gdx.files.internal("shader/$fragmentShaderName.frag")!!

    /**
     * System ms time at which this shader was last compiled.
     */
    var lastCompileTime = 0L

    /**
     * The loaded shader program.
     */
    var program: ShaderProgram? = null

    init {
        compile()
    }

    /**
     * Compile the shader program from the specified source.
     */
    fun compile() {
        program = ShaderProgram(vert, frag).apply {
            if(isCompiled) {
                log("Shader ($frag, $vert) compiled successfully.")
            } else {
                log("Shader ($frag, $vert) failed to compile:\n${log.split("\n").map { "\t" + it }.joinToString("\n")}")
            }
        }
        lastCompileTime = System.currentTimeMillis()
    }

    /**
     * Reload the shader from source if the files have been changed since compilation.
     */
    fun refresh() {
        if (vert.lastModified() > lastCompileTime || frag.lastModified() > lastCompileTime) {
            compile()
            log("Reloaded shader $fragmentShaderName / $vertexShaderName.")
        }
    }

    fun update() {
        if(game().devMode) {
            refresh()
        }

        tick()
    }

    /**
     * Extend to set shader parameters at creation time.
     */
    open fun init() {}

    /**
     * Extend to set shader parameters on a tick-by-tick basis.
     */
    open fun tick() {}
}
