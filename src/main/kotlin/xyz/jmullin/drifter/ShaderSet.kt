package xyz.jmullin.drifter

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShaderProgram

/**
 * Given files to load shader definitions from, compiles and wraps a [[ShaderProgram]] and functionality
 * for reloading the program.  Define init() and tick() methods to set shader uniforms.
 *
 * @param fragmentShaderName Filename of the fragment shader to load.
 * @param vertexShaderName Filename of the vertex shader to load.
 */
class ShaderSet(val fragmentShaderName: String, val vertexShaderName: String) {
    val vert = Gdx.files.internal("shader/$vertexShaderName.vert")
    val frag = Gdx.files.internal("shader/$fragmentShaderName.frag")

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
        program = ShaderProgram(vert, frag)
        lastCompileTime = System.currentTimeMillis()

        //if(!program.isCompiled) {
        //println(program.getLog)
        //}
    }

    /**
     * Reload the shader from source; can be used to do live shader edits at runtime.
     */
    fun refresh() {
        if (vert.lastModified() > lastCompileTime) {
            compile()
            println("Reloaded shader $fragmentShaderName / $vertexShaderName.")
        }
    }

    /**
     * Extend to set shader parameters at creation time.
     */
    fun init() {}

    /**
     * Extend to set shader parameters on a tick-by-tick basis.
     */
    fun tick() {}
}
