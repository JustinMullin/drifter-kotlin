package xyz.jmullin.drifter.rendering.shader

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Matrix3
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import xyz.jmullin.drifter.debug.log
import xyz.jmullin.drifter.extensions.drifter
import xyz.jmullin.drifter.rendering.shader.delegate.UniformDelegate
import kotlin.coroutines.experimental.EmptyCoroutineContext.plus

/**
 * Given files to load shader definitions from, compiles and wraps a [[ShaderProgram]] and functionality
 * for reloading the program.  Define init() and tick() methods to set shader uniforms.
 *
 * @param fragmentShaderName Filename of the fragment shader to load.
 * @param vertexShaderName Filename of the vertex shader to load.
 */
open class ShaderSet(private val fragmentShaderName: String, private val vertexShaderName: String = "default") {
    var uniforms = emptyList<ShaderUniform<*>>()

    private val vert = Gdx.files.internal("shader/$vertexShaderName.vert")!!
    private val frag = Gdx.files.internal("shader/$fragmentShaderName.frag")!!

    /**
     * System ms time at which this shader was last compiled.
     */
    private var lastCompileTime = 0L

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
    private fun compile() {
        program = ShaderProgram(vert, frag).apply {
            if(isCompiled) {
                log("Shader ($frag, $vert) compiled successfully.")
            } else {
                log("Shader ($frag, $vert) failed to compile:\n${log.split("\n").joinToString("\n") { "\t" + it }}")
            }
        }
        lastCompileTime = System.currentTimeMillis()
    }

    /**
     * Reload the shader from source if the files have been changed since compilation.
     */
    private fun refresh() {
        if (vert.lastModified() > lastCompileTime || frag.lastModified() > lastCompileTime) {
            compile()
            log("Reloaded shader $fragmentShaderName / $vertexShaderName.")
        }
    }

    fun update() {
        if(drifter().devMode) {
            refresh()
        }

        uniforms.forEach(ShaderUniform<*>::setFromTick)
        tick()
    }

    /**
     * Extend to set shader parameters on a tick-by-tick basis.
     */
    open fun tick() {}
}

/**
 * Uniform delegates intended for use in member assignment.
 */
val ShaderSet.booleanUniform get() = UniformDelegate.make(this) { name -> BooleanUniform(program!!, name, null) }
val ShaderSet.intUniform get() = UniformDelegate.make(this) { name -> IntUniform(program!!, name, null) }
val ShaderSet.floatUniform get() = UniformDelegate.make(this) { name -> FloatUniform(program!!, name, null) }
val ShaderSet.vector2Uniform get() = UniformDelegate.make(this) { name -> Vector2Uniform(program!!, name, null) }
val ShaderSet.vector3Uniform get() = UniformDelegate.make(this) { name -> Vector3Uniform(program!!, name, null) }
val ShaderSet.matrix3Uniform get() = UniformDelegate.make(this) { name -> Matrix3Uniform(program!!, name, null) }
val ShaderSet.matrix4Uniform get() = UniformDelegate.make(this) { name -> Matrix4Uniform(program!!, name, null) }
val ShaderSet.colorUniform get() = UniformDelegate.make(this) { name -> ColorUniform(program!!, name, null) }

/**
 * Uniform registrars intended for use in side-effecting shader declarations.
 */
fun ShaderSet.booleanUniform(name: String, tick: (() -> Boolean)?) = BooleanUniform(program!!, name, tick).apply { uniforms += this }
fun ShaderSet.intUniform(name: String, tick: (() -> Int)?) = IntUniform(program!!, name, tick).apply { uniforms += this }
fun ShaderSet.floatUniform(name: String, tick: (() -> Float)?) = FloatUniform(program!!, name, tick).apply { uniforms += this }
fun ShaderSet.vector2Uniform(name: String, tick: (() -> Vector2)?) = Vector2Uniform(program!!, name, tick).apply { uniforms += this }
fun ShaderSet.vector3Uniform(name: String, tick: (() -> Vector3)?) = Vector3Uniform(program!!, name, tick).apply { uniforms += this }
fun ShaderSet.matrix3Uniform(name: String, tick: (() -> Matrix3)?) = Matrix3Uniform(program!!, name, tick).apply { uniforms += this }
fun ShaderSet.matrix4Uniform(name: String, tick: (() -> Matrix4)?) = Matrix4Uniform(program!!, name, tick).apply { uniforms += this }
fun ShaderSet.colorUniform(name: String, tick: (() -> Color)?) = ColorUniform(program!!, name, tick).apply { uniforms += this }

fun ShaderSet.booleanUniform(name: String, v: Boolean) = BooleanUniform(program!!, name, { v }).apply { uniforms += this }
fun ShaderSet.intUniform(name: String, v: Int) = IntUniform(program!!, name, { v }).apply { uniforms += this }
fun ShaderSet.floatUniform(name: String, v: Float) = FloatUniform(program!!, name, { v }).apply { uniforms += this }
fun ShaderSet.vector2Uniform(name: String, v: Vector2) = Vector2Uniform(program!!, name, { v }).apply { uniforms += this }
fun ShaderSet.vector3Uniform(name: String, v: Vector3) = Vector3Uniform(program!!, name, { v }).apply { uniforms += this }
fun ShaderSet.matrix3Uniform(name: String, v: Matrix3) = Matrix3Uniform(program!!, name, { v }).apply { uniforms += this }
fun ShaderSet.matrix4Uniform(name: String, v: Matrix4) = Matrix4Uniform(program!!, name, { v }).apply { uniforms += this }
fun ShaderSet.colorUniform(name: String, v: Color) = ColorUniform(program!!, name, { v }).apply { uniforms += this }